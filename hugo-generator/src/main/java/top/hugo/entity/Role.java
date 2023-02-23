


package top.hugo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 角色实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data
@TableName(value = "t_role")
public class Role extends Model<Role> {
    private Long id;
    private Long parentId;
    private String code;
    @NotBlank(message = "userId不能为空")
    private String name;
    @NotBlank(message = "userId不能为空")
    private String intro;
    @NotBlank(message = "userId不能为空")
    private String permissionId;
    private Integer deleted;

}
