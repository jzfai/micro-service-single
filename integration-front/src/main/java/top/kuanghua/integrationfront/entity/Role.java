package top.kuanghua.integrationfront.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

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
    @ApiModelProperty(value = "角色ID（修改时需要传递）", hidden = false)
    @NotEmpty(message = "id不能为空}")
    private Long id;
    @ApiModelProperty(value = "所属父级角色ID（不传为顶级父id）", hidden = false)
    @NotEmpty(message = "parentId不能为空}")
    @Pattern(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", message = "parentId输入有误-身份证")
    private Long parentId;
    @ApiModelProperty(value = "角色唯一CODE代码", hidden = false)
    private String code;
    @ApiModelProperty(value = "角色介绍", hidden = false)
    private String intro;
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ApiModelProperty(value = "创建人", hidden = true)
    private String creator;
    @ApiModelProperty(value = "修改时间", hidden = true)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @ApiModelProperty(value = "修改人", hidden = true)
    private String editor;
    @ApiModelProperty(value = "逻辑删除", hidden = true)
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
