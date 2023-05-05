package top.hugo.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.common.constant.Constants;
import top.hugo.common.domain.R;
import top.hugo.common.utils.JsonUtils;
import top.hugo.system.entity.SysMenu;
import top.hugo.system.helper.modal.LoginBody;
import top.hugo.system.helper.modal.LoginUser;
import top.hugo.system.service.SysLoginService;
import top.hugo.system.service.SysMenuService;
import top.hugo.system.vo.RouterVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证码操作处理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final SysLoginService loginService;
    private final SysMenuService menuService;

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
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

    /**
     * 退出登录
     */
    @PostMapping("logout")
    public R<String> logout() {
        loginService.logout();
        return R.ok("退出成功");
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("getInfo")
    public R<HashMap<String, Object>> getUserInfo() {
        LoginUser user = JsonUtils.parseObject(StpUtil.getExtra("user"), LoginUser.class);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("user", user);
        return R.ok(hm);
    }


    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public R<List<RouterVo>> getRouters() {
        LoginUser user = JsonUtils.parseObject(StpUtil.getExtra("user"), LoginUser.class);
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return R.ok(menuService.buildMenus(menus));
    }
}

