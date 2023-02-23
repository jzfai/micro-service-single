package top.hugo.common.helper;

import cn.dev33.satoken.stp.StpUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import top.hugo.common.domain.model.LoginUser;
import top.hugo.common.enums.DeviceType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {
    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void loginByDevice(LoginUser loginUser, DeviceType deviceType) {
        // SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getLoginId(), deviceType.getDevice());
        // setLoginUser(loginUser);
    }
}
