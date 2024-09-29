package top.hugo.admin.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 *
 * @author 邝华
 */

@Data
public class LoginBody {

    /**
     * 用户名
     */
    @NotBlank(message = "{用户名不能为空}")
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "{密码不能为空}")
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

}
