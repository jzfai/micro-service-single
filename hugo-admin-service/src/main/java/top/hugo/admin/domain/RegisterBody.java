package top.hugo.admin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.hugo.admin.entity.SysUser;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterBody extends SysUser {
    private String code;
    private String uuid;
}
