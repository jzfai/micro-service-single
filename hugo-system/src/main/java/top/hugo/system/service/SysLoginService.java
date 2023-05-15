package top.hugo.system.service;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.hugo.common.constant.CacheConstants;
import top.hugo.common.constant.Constants;
import top.hugo.common.enums.LoginType;
import top.hugo.common.enums.UserStatus;
import top.hugo.common.event.LogininforEvent;
import top.hugo.common.exception.user.CaptchaException;
import top.hugo.common.exception.user.CaptchaExpireException;
import top.hugo.common.exception.user.UserException;
import top.hugo.common.utils.DateUtils;
import top.hugo.common.utils.MessageUtils;
import top.hugo.common.utils.RedisUtils;
import top.hugo.common.utils.ServletUtils;
import top.hugo.common.dto.RoleDTO;
import top.hugo.system.entity.SysUser;
import top.hugo.common.helper.LoginHelper;
import top.hugo.common.domain.LoginUser;
import top.hugo.system.mapper.SysUserMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Slf4j
@Service
public class SysLoginService {

    private final SysUserMapper userMapper;
    private final SysConfigService configService;
    private final SysPermissionService permissionService;
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
        HttpServletRequest request = ServletUtils.getRequest();
        //验证码
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid, request);
        }
        //检查用户状态，获取用户信息
        SysUser user = loadUserByUsername(username);
        //检查用户登录状态,登录次数，密码是否正确等
        checkLogin(LoginType.PASSWORD, username, () -> BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        StpUtil.login(user.getUserId(), SaLoginConfig.setExtra("user", loginUser));
        recordLoginInfo(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordUserLoginInfo(user.getUserId(), username);
        return StpUtil.getTokenValue();
    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUsername(user.getUserName());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(permissionService.getMenuPermission(user));
        loginUser.setRolePermission(permissionService.getRolePermission(user));
        loginUser.setDeptName(ObjectUtil.isNull(user.getDept()) ? "" : user.getDept().getDeptName());
        List<RoleDTO> roles = BeanUtil.copyToList(user.getRoles(), RoleDTO.class);
        loginUser.setRoles(roles);
        return loginUser;
    }

    /**
     * 登录校验
     */
    private void checkLogin(LoginType loginType, String username, Supplier<Boolean> supplier) {
        HttpServletRequest request = ServletUtils.getRequest();
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;
        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }
        //supplier.get()-> true:判断密码正确
        if (!supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                //syncService.recordLoginInfo(username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime), request);
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                // asyncService.recordLoginInfo(username, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber), request);
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }
        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
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
            throw new UserException("user.not.exists", username);
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new UserException("user.blocked", username);
        }
        return user;
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
//        SpringUtils.context().publishEvent(logininforEvent);
    }

    /**
     * 记录用户登录信息
     *
     * @param userId 用户ID
     */
    public void recordUserLoginInfo(Long userId, String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(ServletUtils.getClientIP());
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysUser.setUpdateBy(username);
        userMapper.updateById(sysUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid, HttpServletRequest request) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

    /**
     * 退出登录
     */
    public void logout() {
        try {
            StpUtil.logout();
            LoginUser userInfo = LoginHelper.getUserInfo();
            recordLoginInfo(userInfo.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
        } catch (NotLoginException ignored) {

        }
    }
}
