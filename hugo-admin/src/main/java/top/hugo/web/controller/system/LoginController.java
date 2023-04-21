package top.hugo.web.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.constant.Constants;
import top.hugo.common.domain.R;
import top.hugo.common.domain.model.LoginBody;
import top.hugo.system.service.SysLoginService;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码操作处理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/author")
public class LoginController {

    private final SysLoginService loginService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     */
    @SaIgnore
    @PostMapping("/login")
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
    @SaIgnore
    @PostMapping("/logout")
    public R<Void> logout() {
        loginService.logout();
        return R.ok("退出成功");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/userInfo")
    public R<SaTokenInfo> getUserInfo() {
        SaTokenInfo userInfo = loginService.getUserInfo();
        return R.ok(userInfo);
    }
}

