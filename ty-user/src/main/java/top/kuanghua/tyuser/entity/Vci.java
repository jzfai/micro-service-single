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
 * VCI设备表(Vci)表实体类
 *
 * @author kuanghua
 * @since 2021-10-22 10:34:43
 */
@Data
@ApiModel("VCI设备表")
@TableName(value = "tb_vci")
public class Vci extends Model<Vci> {
    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "序列号，唯一")
    private String sn;
    @ApiModelProperty(value = "硬件版本")
    private String hardVersion;
    @ApiModelProperty(value = "状态：0：未出库、1：已入库")
    private Integer status;
    @ApiModelProperty(value = "供应商")
    private String supplier;
    @ApiModelProperty(value = "商品规格")
    private String productSpec;
    @ApiModelProperty(value = "入库单号")
    private String receiptNo;
    @ApiModelProperty(value = "入库时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
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

