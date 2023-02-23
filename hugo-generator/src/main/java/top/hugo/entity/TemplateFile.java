package top.hugo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
    @TableId(type = IdType.AUTO)
    @NotNull(message = "id不能为空")
    private Integer id;
    @NotBlank(message = "name不能为空")
    private String name;
    @NotBlank(message = "fileArr不能为空")
    private String fileArr;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer deleted;
}
