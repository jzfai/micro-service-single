

package top.kuanghua.integrationfront.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 代码生成配置保存实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data
@ApiModel("代码生成配置保存")
@TableName(value = "t_config_save")
public class ConfigSave extends Model<ConfigSave> {
    @ApiModelProperty(value = "选中的字段配置", hidden = false)
    private String name;
    @ApiModelProperty(value = "生成的配置", hidden = false)
    private String generatorConfig;
    @ApiModelProperty(value = "主键id", hidden = false)
    private Integer id;

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
