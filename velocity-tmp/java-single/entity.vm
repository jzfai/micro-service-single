#parse("utils.vm")
package ${basicConfig.packageName}.entity;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
/**
*  ${dbTableConfig.tableDesc}实体类
*
* @author ${basicConfig.author}
* @since ${basicConfig.dataTime}
*/
@Data
@TableName(value = "${dbTableConfig.originTableName}")
public class ${dbTableConfig.tableNameCase} {
#foreach($item in $tbData)
    #if($item.field=="id")
    //${item.desc}
    @TableId(type = #if($item.extra=="auto_increment")IdType.AUTO#{else}IdType.AUTO#end )
    private ${item.type} $item.field;

    #elseif($item.field=="createTime")
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private ${item.type} $item.field;
  
    #elseif($item.field=="updateTime")
    //更新时间
    @TableField(fill = FieldFill.UPDATE)  
    private ${item.type} $item.field;

    #elseif($item.field=="createBy")
    //${item.desc}
    @TableField(fill = FieldFill.INSERT)
    private ${item.type} $item.field;

    #elseif($item.field=="updateBy")
    //${item.desc}
    @TableField(fill = FieldFill.UPDATE)
    private ${item.type} $item.field;
  
    #elseif($item.field=="delFlag")
    //删除标志（0代表存在 2代表删除）
    @TableLogic
    private ${item.type} $item.delFlag;
  
    #else
    //${item.desc}
    private ${item.type} $item.field;
    #end
#end

}
