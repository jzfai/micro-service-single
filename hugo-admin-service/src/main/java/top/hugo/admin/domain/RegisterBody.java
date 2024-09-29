package top.hugo.admin.domain;

import top.hugo.admin.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterBody extends SysUser {
    private String code;
    private String uuid;
}
