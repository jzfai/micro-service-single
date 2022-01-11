package top.kuanghua.integrationfront.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限表(Access)表实体类
 *
 * @author kuanghua
 * @since 2021-03-23 11:03:45
 */
@Data
@ApiModel("权限表")
@TableName(value = "tb_access")
public class Access extends Model<Access> {
    @ApiModelProperty(value = "主键id")
    private Integer id;
    @ApiModelProperty(value = "权限名称")
    private String name;
    @ApiModelProperty(value = "是否删除")
    private String isDelete;
    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;
    @ApiModelProperty(value = "父id")
    private Integer parentId;
    @ApiModelProperty(value = "是否是父类")
    private String isParent;
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
