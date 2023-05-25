package top.hugo.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.hugo.system.modal.LoginBody;

/**
 * 用户注册对象
 *
 * @author kuanghua
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterBody extends LoginBody {

    private String userType;

}
