package top.kuanghua.basisfunc.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*  代码生成配置保存实体类
*
* @author 熊猫哥
* @since 2022-07-20 10:10:28
*/
@Data
@ApiModel("代码生成配置保存")
@TableName(value = "t_config_save")
public class GeneratorConfigSave extends Model< GeneratorConfigSave > {
    @ApiModelProperty(value = "")
    private Integer id;
    @ApiModelProperty(value = "选中的字段配置")
    private String name;
    @ApiModelProperty(value = "生成的配置")
    private String generatorConfig;
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
