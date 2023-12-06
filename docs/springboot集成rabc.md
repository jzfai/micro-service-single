## springboot 集成rabc

导入rabc相关sql



RbacMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="top.hugo.admin.mapper.RbacMapper">
    <!--  菜单  -->
    <sql id="selectMenuVo">
        select distinct m.*
        from sys_menu m
                 left join sys_role_menu rm on m.menu_id = rm.menu_id
                 left join sys_user_role sur on rm.role_id = sur.role_id
                 left join sys_role r on r.role_id = sur.role_id
    </sql>
    <select id="selectMenuAll" resultType="SysMenu">
        <include refid="selectMenuVo"/>
        where m.menu_type!="F" and m.platform_id=#{platformId} order by m.order_num
    </select>
    <select id="selectMenuByUserId" resultType="SysMenu">
        <include refid="selectMenuVo"/>
        where sur.user_id = #{userId} and m.menu_type!="F" and m.platform_id=#{platformId} order by m.order_num
    </select>
    <select id="selectBtnPermByUserId" resultType="SysMenu">
        <include refid="selectMenuVo"/>
        where sur.user_id = #{userId} and m.menu_type = "F" and m.platform_id=#{platformId} order by m.order_num
    </select>


    <!--角色筛选-->
    <sql id="selectRole">
        select sr.*
        from sys_role sr
                 left join sys_user_role sur on sur.role_id = sr.role_id
    </sql>
    <select id="selectRolesByUserId" resultType="SysRole">
        <include refid="selectRole"/>
        WHERE sr.del_flag = '0' and sur.user_id = #{userId}
    </select>
</mapper>
```



RbacMapper

```java
package top.hugo.admin.mapper;

import org.apache.ibatis.annotations.Param;
import top.hugo.admin.entity.SysMenu;

import java.util.List;

/**
 * 用户角色权限mapper
 *
 * @author kuanghua
 * @since 2023-08-22 16:57:10
 */

public interface RbacMapper {

    List<SysMenu> selectMenuAll(int platformId);

    List<SysMenu> selectMenuByUserId(@Param("userId") Long userId, @Param("platformId") int platformId);

    List<SysMenu> selectBtnPermByUserId(@Param("userId") Long userId, @Param("platformId") Integer platformId);
}
```

RbacService

```java
package top.hugo.admin.service;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import top.hugo.admin.entity.SysMenu;
import top.hugo.admin.entity.SysRole;
import top.hugo.admin.entity.SysUser;
import top.hugo.admin.helper.LoginHelper;
import top.hugo.admin.mapper.RbacMapper;
import top.hugo.admin.mapper.SysRoleMapper;
import top.hugo.admin.mapper.SysUserMapper;
import top.hugo.redis.utils.RedisUtils;

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
        // 生成token
        StpUtil.login(user.getUserId(), SaLoginConfig.setExtra("user", user));
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
                throw new RuntimeException("登录重试超出限制(锁定登录)-" + maxRetryCount + "-" + lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                throw new RuntimeException("登录重试限制计数提示-" + errorNumber);
            }
        }
        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
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
        AbstractCaptcha captcha = new CircleCaptcha(width, height);
        captcha.setGenerator(new MathGenerator(1));
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
        List<SysRole> sysRoles = sysRoleMapper.selectRolesByUserId(userId);
        return sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
    }
}

```



RbacController

```java
package top.hugo.admin.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.admin.domain.LoginBody;
import top.hugo.admin.entity.SysMenu;
import top.hugo.admin.entity.SysUser;
import top.hugo.admin.service.RbacService;
import top.hugo.admin.utils.LoginHelper;
import top.hugo.common.domain.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录和用户信息，菜单相关接口
 *
 * @author kuanghua
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("rbac")
@SaIgnore
public class RbacController {
    private final RbacService rbacService;

    /**
     * 获取验证码
     *
     * @param width  验证码宽度
     * @param height 验证码高度
     * @author 熊猫哥
     * @date 2023-09-07 17:55
     */
    @SaIgnore
    @GetMapping("getCode")
    public R<Map<String, Object>> generatorCode(int width, int height) {
        Map<String, Object> code = rbacService.generatorCode(width, height);
        return R.ok(code);
    }

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     */
    @SaIgnore
    @PostMapping("login")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBody loginBody) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = rbacService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        ajax.put("token", token);
        return R.ok(ajax);
    }

    /**
     * 退出登录
     */
    @PostMapping("logout")
    public R<String> logout() {
        rbacService.logout();
        return R.ok("退出成功");
    }

    /**
     * 注册
     */
    @PostMapping("register")
    public R<Void> register(String code, String uuid, SysUser sysUser) {
        return R.result(rbacService.register(code, uuid, sysUser));
    }

    /**
     * 获取用户信息
     *
     * @param platformId 平台id
     */
    @GetMapping("getInfo")
    public R<HashMap<String, Object>> getUserInfo(Integer platformId) {
        HashMap<String, Object> userInfo = rbacService.getUserInfo(platformId);
        return R.ok(userInfo);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getMenu")
    public R<List<SysMenu>> getMenu(Integer platformId) {
        List<SysMenu> menus = rbacService.selectMenuByUserId(LoginHelper.getUserId(), platformId);
        return R.ok(menus);
    }
}
```





