package top.kuanghua.integrationfront.entity;

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
 * 角色表(Role)表实体类
 *
 * @author kuanghua
 * @since 2021-01-18 12:12:37
 */
@Data
@ApiModel("角色表")
@TableName(value = "tb_role")
public class Role extends Model<Role> {
    @ApiModelProperty(value = "角色id")
    private Integer id;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;
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