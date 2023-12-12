package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色信息表实体类
 *
 * @author kuanghua
 * @since 2023-11-20 14:36:16
 */
@Data
@TableName(value = "sys_role_menu")
public class SysRoleMenu {
    //角色id
    @TableId
    private Long roleId;

    //菜单id
    private Long menuId;

}
