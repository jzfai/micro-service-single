package top.hugo.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.hugo.admin.domain.BaseEntity;

/**
 * 实体类
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Data
@TableName(value = "t_template_file")
public class TemplateFile extends BaseEntity {

    //逻辑删除:0=未删除,1=已删除
    private Integer deleted;
    //文件数组
    private String fileArr;
    //主键id
    @TableId(type = IdType.AUTO)
    private Integer id;

    //文件存储名
    private String name;


}
