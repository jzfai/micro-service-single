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
 * error_log_collection(ErrorCollection)表实体类
 *
 * @author kuanghua
 * @since 2021-10-08 11:37:25
 */
@Data
@ApiModel("错误日志相关")
@TableName(value = "tb_error_collection")
public class ErrorCollection extends Model<ErrorCollection> {
    @ApiModelProperty(value = "id主键")
    private Long id;
    @ApiModelProperty(value = "错误日志")
    private String errorLog;
    @ApiModelProperty(value = "页面路径")
    private String pageUrl;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间",hidden = true)
    private Date createTime;
    @ApiModelProperty(value = "浏览器类型",hidden = true)
    private String browserType;
}
