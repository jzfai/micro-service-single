


package top.hugo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 权限实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data

@TableName(value = "t_permission")
public class Permission extends Model<Permission> {
    private Long id;
    private Long parentId;
    private Integer plateFormId;
    private Long parentNode;
    private Integer sort;
    @NotBlank(message = "code不能为空")
    private String code;
    private String intro;
    private String creator;
    private String editor;
    private String path;
    @NotBlank(message = "name不能为空")
    private String name;
    @NotNull(message = "category不能为空")
    private Integer category;
    private String component;
    private String title;
    private String elSvgIcon;
    private String icon;
    private String redirect;
    private Integer alwaysShow;
    private Integer hidden;
    private String extra;
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private Integer deleted;
}
