


package top.hugo.admin.entity;

import top.hugo.admin.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
*  dsl配置实体类
*
* @author 邝华
* @since 2023-10-17 10:42:10
*/
@Data
@TableName(value = "dsl_config")
public class DslConfig extends BaseEntity {
    //数据源
    private String dataSource;
    //字典名称
    private String dictName;
    //主键id
    @TableId(type = IdType.AUTO )
    private Integer id;

    //平台名称
    private String platName;

    //平台名称
    private String name;


}
