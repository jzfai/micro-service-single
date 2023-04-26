package top.hugo.system.helper;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import top.hugo.common.constant.UserConstants;
import top.hugo.system.entity.SysUser;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {
    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";


    public static SysUser getUserInfo() {
        ObjectMapper jackson = new ObjectMapper();
        SysUser sysUser = null;
        try {
            sysUser = jackson.readValue(jackson.writeValueAsString(StpUtil.getExtra("user")), SysUser.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sysUser;
    }


    public static Long getUserId() {
        return getUserInfo().getUserId();
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(getUserInfo().getUserId());
    }


}
