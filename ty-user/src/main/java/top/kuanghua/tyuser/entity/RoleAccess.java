package top.kuanghua.tyuser.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限表(RoleAccess)表实体类
 *
 * @author kuanghua
 * @since 2021-01-18 12:12:38
 */
@Data
@ApiModel("角色权限表")
@TableName(value = "tb_role_access")
public class RoleAccess extends Model<RoleAccess> {
    @ApiModelProperty(value = "主键id")
    private Integer id;
    @ApiModelProperty(value = "角色id")
    private Integer roleId;
    @ApiModelProperty(value = "权限id")
    private Integer accessId;
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;
    @ApiModelProperty(value = "父id:(0,顶级父类")
    private Integer parentId;
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