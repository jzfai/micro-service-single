package top.hugo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
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
    private Integer id;
    private String name;
    private String generatorConfig;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableLogic
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String deleted;
}
