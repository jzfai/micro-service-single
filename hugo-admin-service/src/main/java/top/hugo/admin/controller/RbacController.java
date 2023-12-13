package top.hugo.admin.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.admin.domain.LoginBody;
import top.hugo.admin.entity.SysMenu;
import top.hugo.admin.entity.SysUser;
import top.hugo.admin.service.RbacService;
import top.hugo.common.domain.R;
import top.hugo.satoken.helper.LoginHelper;

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
    @GetMapping("captchaImage")
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
        String token = rbacService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
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
     * @return
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


    /**
     * 根据角色查询菜单信息
     *
     * @param roleId 角色id
     * @return
     */
    @GetMapping("selectMenuListByRoleId")
    public R<List<SysMenu>> selectMenuListByRoleId(Long roleId) {
        List<SysMenu> sysMenus = rbacService.selectMenuListByRoleId(roleId);
        return R.ok(sysMenus);
    }


    /**
     * 根据角色平台查询菜单信息
     *
     * @param roleId 角色id
     * @return
     */
    @GetMapping("selectMenuListByRolePlatformId")
    public R<List<SysMenu>> selectMenuListByRolePlatformId(Long roleId, Integer platformId) {
        List<SysMenu> sysMenus = rbacService.selectMenuListByRolePlatformId(roleId, platformId);
        return R.ok(sysMenus);
    }
}

