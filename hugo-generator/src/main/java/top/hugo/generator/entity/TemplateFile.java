package top.hugo.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import top.hugo.common.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 实体类
 *
 * @author 邝华
 * @since 2022-12-07 13:51:06
 */
@Data
@TableName(value = "t_template_file")
public class TemplateFile extends BaseEntity {
    /**
     * id主键
     */
    @TableId(type = IdType.AUTO)
    @NotNull(message = "id不能为空")
    private Integer id;


    /**
     * 模板名
     */
    @NotBlank(message = "name不能为空")
    private String name;


    /**
     * 模板文件数组（string）
     */
    @NotBlank(message = "fileArr不能为空")
    private String fileArr;


    /**
     * 是否删除（不用传）
     */
    private Integer deleted;
}
