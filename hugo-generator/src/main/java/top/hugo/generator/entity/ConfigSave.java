package top.hugo.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 代码生成配置保存实体类
 *
 * @author 熊猫哥
 * @since 2022-07-20 10:10:28
 */
@Data
@TableName(value = "t_config_save")
public class ConfigSave extends Model<ConfigSave> {
    /**
     * 配置id
     */
    private Integer id;

    /**
     * 配置名
     */
    @NotBlank(message = "配置名不能为空")
    private String name;

    /**
     * Json数据（string）
     */
    @NotBlank(message = "Json数据不能为空")
    private String generatorConfig;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    /**
     * 是否删除(不用传)
     */
    @TableLogic
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String deleted;
}
