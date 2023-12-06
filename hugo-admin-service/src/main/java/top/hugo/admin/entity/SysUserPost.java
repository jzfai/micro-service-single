package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户与岗位关联表实体类
 *
 * @author kuanghua
 * @since 2023-11-22 15:19:43
 */
@Data
@TableName(value = "sys_user_post")
public class SysUserPost {
    //用户ID
    @TableId
    private Long userId;
    //岗位ID
    private Long postId;
}
