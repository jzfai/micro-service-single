


package top.kuanghua.basisfunc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 平台实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data
@ApiModel("平台")
@TableName(value = "t_plate_form")
public class PlateForm extends Model<PlateForm> {
    @ApiModelProperty(value = "主键", hidden = false)
    private Integer id;
    @ApiModelProperty(value = "平台名字", hidden = false)
    @NotBlank(message = "name不能为空")
    private String name;

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
