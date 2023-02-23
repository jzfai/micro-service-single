package top.hugo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 品牌表实体类
 *
 * @author 熊猫哥
 * @since 2022-12-16 14:42:22
 */
@Data
@TableName(value = "tb_brand")
public class Brand extends Model<Brand> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "name不能为空")
    private String name;
    @NotBlank(message = "image不能为空")
    private String image;
    @NotBlank(message = "letter不能为空")
    private String letter;
    @NotBlank(message = "seq不能为空")
    private String seq;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @TableLogic
    private Integer deleted;

}
