package top.hugo.admin.helper;


import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import top.hugo.admin.domain.LoginUser;
import top.hugo.common.utils.JacksonUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {
    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";
    public static final Long ADMIN_ID = 1L;


    public static LoginUser getUserInfo() {
        ObjectMapper jackson = new ObjectMapper();
        return JacksonUtils.parseObject(StpUtil.getExtra("user"), LoginUser.class);
    }


    public static Long getUserId() {
        return getUserInfo().getUserId();
    }

    public static Integer getPlatformId() {
        return getUserInfo().getPlatformId();
    }

    /**
     * 是否为管理员
     *
     * @return 结果
     */
    public static boolean isAdmin() {
        return ADMIN_ID.equals(getUserInfo().getUserId());
    }

    public static boolean isAdmin(Long userId) {
        return ADMIN_ID.equals(userId);
    }

    public static String getUsername() {
        return getUserInfo().getUsername();
    }
}
