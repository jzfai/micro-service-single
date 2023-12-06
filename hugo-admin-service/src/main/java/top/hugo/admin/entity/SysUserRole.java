package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户和角色关联表实体类
 *
 * @author kuanghua
 * @since 2023-11-22 15:22:26
 */
@Data
@TableName(value = "sys_user_role")
public class SysUserRole {
    //用户ID
    @TableId
    private Long userId;
    //角色ID
    private Long roleId;
}
