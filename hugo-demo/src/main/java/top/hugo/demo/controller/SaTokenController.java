package top.hugo.demo.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("sa-token")
public class SaTokenController {
    /**
     * 登录方法
     */
    @SaIgnore
    @PostMapping("login")
    public Map<String, Object> login() {
        Map<String, Object> ajax = new HashMap<>();
        SaLoginModel saLoginModel = SaLoginConfig.setExtra("user", "fai");
        StpUtil.login(1, saLoginModel);
        ajax.put("token", StpUtil.getTokenValue());
        return ajax;
    }

    /**
     * 退出登录
     */
    @PostMapping("logout")
    public String logout() {
        StpUtil.logout();
        return "退出成功";
    }

    /**
     * 角色测试
     */
    @SaCheckRole("role:list")
    @GetMapping("RoleTest")
    public String RoleTest() {
        return "RoleTest";
    }

    /**
     * 权限测试
     */
    @SaCheckPermission("permission:list")
    @GetMapping("PermissionTest")
    public String PermissionTest() {
        return "RoleTest";
    }
}