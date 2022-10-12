

package top.kuanghua.integrationfront.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 代码生成配置保存实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data
@ApiModel("代码生成配置保存")
public class ConfigSaveVo extends Model<ConfigSaveVo> {
    @ApiModelProperty(value = "主键id", hidden = false)
    private Integer id;
    @ApiModelProperty(value = "选中的字段配置", hidden = false)
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
