


package top.kuanghua.basisfunc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 角色实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data
@ApiModel("角色")
@TableName(value = "t_role")
public class Role extends Model<Role> {
    @ApiModelProperty(value = "角色ID", hidden = false)
    private Long id;
    @ApiModelProperty(value = "所属父级角色ID", hidden = false)
    private Long parentId;
    @ApiModelProperty(value = "角色唯一CODE代码", hidden = false)
    private String code;
    @ApiModelProperty(value = "角色名称", hidden = false)
    @NotBlank(message = "userId不能为空")
    private String name;
    @ApiModelProperty(value = "角色介绍", hidden = false)
    @NotBlank(message = "userId不能为空")
    private String intro;
    @ApiModelProperty(value = "权限id(字符数组)", hidden = false)
    @NotBlank(message = "userId不能为空")
    private String permissionId;
    @ApiModelProperty(value = "逻辑删除", hidden = false)
    private Integer deleted;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
