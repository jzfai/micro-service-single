


package top.kuanghua.basisfunc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 权限实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data
@ApiModel("权限")
@TableName(value = "t_permission")
public class Permission extends Model<Permission> {
    @ApiModelProperty(value = "权限ID", hidden = false)
    private Long id;
    @ApiModelProperty(value = "所属父级权限ID", hidden = false)
    private Long parentId;
    @ApiModelProperty(value = "平台id", hidden = false)
    private Integer plateFormId;
    @ApiModelProperty(value = "是否时父元素;0:不是,1:是", hidden = false)
    private Long parentNode;
    @ApiModelProperty(value = "排序", hidden = false)
    private Integer sort;
    @ApiModelProperty(value = "权限唯一CODE代码", hidden = false)
    @NotBlank(message = "code不能为空")
    private String code;
    @ApiModelProperty(value = "权限介绍", hidden = false)
    private String intro;
    @ApiModelProperty(value = "创建人", hidden = true)
    private String creator;
    @ApiModelProperty(value = "修改人", hidden = true)
    private String editor;
    @ApiModelProperty(value = "路由路径", hidden = false)
    private String path;
    @ApiModelProperty(value = "权限名称", hidden = false)
    @NotBlank(message = "name不能为空")
    private String name;
    @ApiModelProperty(value = "权限类别;1:路由,2:内页,3:按钮", hidden = false)
    @NotNull(message = "category不能为空")
    private Integer category;
    @ApiModelProperty(value = "组件", hidden = false)
    private String component;
    @ApiModelProperty(value = "页面标题", hidden = false)
    private String title;
    @ApiModelProperty(value = "element的icon图标", hidden = false)
    private String elSvgIcon;
    @ApiModelProperty(value = "自定义的svg图标", hidden = false)
    private String icon;
    @ApiModelProperty(value = "重定向路径", hidden = false)
    private String redirect;
    @ApiModelProperty(value = "当只有一个菜单是否显示子项;0:隐藏,1:显示", hidden = false)
    private Integer alwaysShow;
    @ApiModelProperty(value = "子项是否隐藏;0:显示,1:隐藏", hidden = false)
    private Integer hidden;
    @ApiModelProperty(value = "路由项额外字段", hidden = false)
    private String extra;
    @ApiModelProperty(value = "修改时间", hidden = true)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "是否激活", hidden = true)
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
