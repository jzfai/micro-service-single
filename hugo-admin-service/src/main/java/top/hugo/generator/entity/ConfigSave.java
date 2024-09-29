package top.hugo.generator.entity;

import top.hugo.admin.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 代码生成配置保存实体类
 *
 * @author kuanghua
 * @since 2023-10-18 11:42:26
 */
@Data
@TableName(value = "t_config_save")
public class ConfigSave extends BaseEntity {

    //生成的配置
    private String generatorConfig;
    //配置
    @TableId(type = IdType.AUTO)
    private Integer id;

    //选中的字段配置
    private String name;


}
