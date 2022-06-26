package top.kuanghua.integrationfront.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 售后维修表实体类
 *
 * @author 熊猫哥
 * @since 2022-06-25 10:32:12
 */
@Data
@ApiModel("售后维修表")
@TableName(value = "tb_repair")
public class Pairment {
    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "SN_ID")
    private String sn;
    @ApiModelProperty(value = "设备类型")
    private Integer equipType;
    @ApiModelProperty(value = "换件_ID")
    private String replacementId;
    @ApiModelProperty(value = "快递信息ID")
    private String expressId;
    @ApiModelProperty(value = "问题归类id")
    private String problemClassificationId;
    @ApiModelProperty(value = "目前状态")
    private Integer currentStatus;
    @ApiModelProperty(value = "问题描述")
    private String problemDescription;
    @ApiModelProperty(value = "反馈时间")
    private Date feedbackTime;
    @ApiModelProperty(value = "提出人")
    private String proposer;
    @ApiModelProperty(value = "联系方式")
    private String contactInfo;
    @ApiModelProperty(value = "回收日期")
    private Date recoveryDate;
    @ApiModelProperty(value = "返回日期")
    private Date returnDate;
    @ApiModelProperty(value = "返修分析的问题")
    private String problemsAnalyzed;
    @ApiModelProperty(value = "返修分析的原因")
    private String reason;
    @ApiModelProperty(value = "处理方法")
    private String processingMethod;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "新SN_ID")
    private String newSnId;
    @ApiModelProperty(value = "深度")
    private Integer depth;

}
