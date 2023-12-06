package top.hugo.admin.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import top.hugo.admin.captcha.UnsignedMathGenerator;
import top.hugo.admin.entity.SysMenu;
import top.hugo.admin.entity.SysRole;
import top.hugo.admin.entity.SysUser;
import top.hugo.admin.mapper.RbacMapper;
import top.hugo.admin.mapper.SysRoleMapper;
import top.hugo.admin.mapper.SysUserMapper;
import top.hugo.common.domain.LoginUser;
import top.hugo.common.enums.DeviceType;
import top.hugo.common.event.LogininforEvent;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.common.utils.ServletUtils;
import top.hugo.redis.utils.RedisUtils;
import top.hugo.satoken.helper.LoginHelper;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RbacService {
    private final SysUserMapper userMapper;
    private final RbacMapper rbacMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysPermissionService permissionService;
    private final HttpServletRequest request;
    @Value("${user.password.maxRetryCount}")
    private Integer maxRetryCount;
    @Value("${user.password.lockTime}")
    private Integer lockTime;
    @Value("${sa-token.token-prefix}")
    private String tokenPrefix;


    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        //验证码校验
        validateCaptcha(code, uuid, request);

        //获取用户信息
        SysUser user = loadUserByUsername(username);
        //检查用户登录状态,登录次数，密码是否正确等
        checkLogin(username, () -> BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        //LoginUser loginUser = buildLoginUser(user);
        LoginUser loginUser = BeanCopyUtils.copy(user, LoginUser.class);
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        return StpUtil.getTokenValue();
    }

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param uuid 唯一标识
     * @return
     */
    public String validateCaptcha(String code, String uuid, HttpServletRequest request) {
        String verifyKey = "captcha_codes:" + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            throw new RuntimeException("验证码不能为空");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new RuntimeException("验证码不正确");
        }
        return captcha;
    }

    //获取和判断用户信息
    private SysUser loadUserByUsername(String username) {
        //查询用户信息
        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(SysUser::getUserName, username);
        SysUser user = userMapper.selectOne(userLambdaQueryWrapper);
        //判断用户是否有效
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new RuntimeException("user.not.exists");
        } else if ("2".equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new RuntimeException("user.blocked");
        }
        return user;
    }


    /**
     * 登录校验
     */
    private void checkLogin(String username, Supplier<Boolean> supplier) {
        String errorKey = "pwd_err_cnt:" + username;
        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            throw new RuntimeException("登录重试超出限制提示-" + maxRetryCount + "-" + lockTime);
        }
        //supplier.get()-> true:判断密码正确
        if (!supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                recordLoginInfo(username, "1", String.format("锁定登录--%s", errorNumber));
                throw new RuntimeException("登录重试超出限制(锁定登录)-" + maxRetryCount + "-" + lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                recordLoginInfo(username, "1", String.format("用户名或密码错误--%s", errorNumber));
                throw new RuntimeException("登录重试限制计数提示-" + errorNumber);
            }
        }
        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
        recordLoginInfo(username, "0", "登录成功");
    }


    /**
     * 生成验证码
     *
     * @param width  验证码宽度
     * @param height 验证码高度
     * @return
     */
    public Map<String, Object> generatorCode(int width, int height) {

        //验证码生成
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(width, height, 4, 2);
        captcha.setGenerator(new UnsignedMathGenerator(1));
        captcha.createCode();
        String code = captcha.getCode();

        //验证码计算
        ExpressionParser parser = new SpelExpressionParser();
        //移除“=”号，计算二维码的值
        Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
        code = exp.getValue(String.class);


        //保存到redis并组装数据返回前端
        String uuid = IdUtil.simpleUUID();
        String verifyKey = "captcha_codes:" + uuid;

        //设定验证码有效期
        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(5));
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("captchaEnabled", true);
        ajax.put("uuid", uuid);
        ajax.put("img", captcha.getImageBase64());
        return ajax;
    }


    /**
     * 注册
     *
     * @return
     */
    public int register(String code, String uuid, SysUser sysUser) {
        //检验验证码
        validateCaptcha(code, uuid, request);

        //检查用户名和手机号是否唯一
        boolean b = checkUserNameUnique(sysUser);
        if (b) throw new RuntimeException("用户已存在");

        //转换密码
        sysUser.setPassword(BCrypt.hashpw(sysUser.getPassword()));
        return userMapper.insert(sysUser);
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return true 用户已存在
     */
    public boolean checkUserNameUnique(SysUser user) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUser::getUserName, user.getUserName());
        qw.ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId());
        return userMapper.exists(qw);
    }

    /**
     * 退出登录
     */
    public void logout() {
        StpUtil.logout();
    }


    /**
     * 根据用户查询菜单列表
     *
     * @return
     * @author 熊猫哥
     * @date 2023-09-07 17:25
     */
    public List<SysMenu> selectMenuByUserId(Long userId, Integer platformId) {
        //1.根据用户和平台获取顶级父元素列表
        List<SysMenu> menus = null;
        //如果是admin则返回改平台下的所有菜单
        if (0 == userId) {
            menus = rbacMapper.selectMenuAll(platformId);
        } else {
            menus = rbacMapper.selectMenuByUserId(userId, platformId);
        }
        return menus;
    }


    /**
     * 获取用户信息
     *
     * @return
     * @author 熊猫哥
     * @date 2023-09-07 17:40
     */
    public HashMap<String, Object> getUserInfo(Integer platformId) {
        HashMap<String, Object> hm = new HashMap<>();
        Set<String> menuPermission = permissionService.getMenuPermission(platformId);
        Set<String> btnPermission = permissionService.getMenuBtnPermission(platformId);
        hm.put("menuPermission", menuPermission);
        hm.put("btnPermission", btnPermission);
        hm.put("permission", menuPermission.addAll(btnPermission));
        hm.put("roles", selectRolesByUserId(LoginHelper.getUserId()));
        hm.put("user", userMapper.selectById(LoginHelper.getUserId()));
        return hm;
    }


    /**
     * 根据用户id查询角色集合
     *
     * @return
     * @author 熊猫哥
     * @date 2023-09-07 17:46
     */
    public List<String> selectRolesByUserId(Long userId) {
        List<SysRole> sysRoles = rbacMapper.selectRolesByUserId(userId);
        return sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
    }


    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    private void recordLoginInfo(String username, String status, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtil.getApplicationContext().publishEvent(logininforEvent);
    }
}
