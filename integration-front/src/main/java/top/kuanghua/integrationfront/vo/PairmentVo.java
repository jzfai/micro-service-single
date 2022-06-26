package top.kuanghua.integrationfront.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 多表中实体类的注释实体类
 *
 * @author 熊猫哥
 * @since 2022-06-25 10:32:12
 */
@Data
@ApiModel("多表中实体类的注释")
public class PairmentVo {
    @ApiModelProperty(value = "目前状态")
    private Integer currentStatus;
    @ApiModelProperty(value = "回收日期")
    private Date recoveryDate;
    @ApiModelProperty(value = "联系方式")
    private String contactInfo;
    @ApiModelProperty(value = "设备类型")
    private Integer equipType;
    @ApiModelProperty(value = "深度")
    private Integer depth;

}
