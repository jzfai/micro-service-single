package top.kuanghua.tyuser.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌表(Brand)表实体类
 *
 * @author kuanghua
 * @since 2021-09-15 11:54:49
 */
@Data
@ApiModel("品牌表")
@TableName(value = "tb_brand")
public class Brand extends Model<Brand> {
    @ApiModelProperty(value = "品牌id")
    private Integer id;
    @ApiModelProperty(value = "品牌名称")
    private String name;
    @ApiModelProperty(value = "品牌图片地址")
    private String image;
    @ApiModelProperty(value = "品牌的首字母")
    private String letter;
    @ApiModelProperty(value = "排序")
    private Integer seq;
    @ApiModelProperty(value = "创建时间",hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ApiModelProperty(value = "更新时间",hidden = true)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
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
