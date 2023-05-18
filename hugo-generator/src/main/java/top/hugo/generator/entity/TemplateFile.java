package top.hugo.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

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
public class TemplateFile extends Model<TemplateFile> {
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
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否删除（不用传）
     */
    private Integer deleted;
}
