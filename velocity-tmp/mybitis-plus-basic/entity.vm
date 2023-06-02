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
@Schema("${dbTableConfig.tableDesc}")
@TableName(value = "${dbTableConfig.originTableName}")
public class ${dbTableConfig.tableNameCase} extends Model< ${dbTableConfig.tableNameCase} > {
#foreach($item in $formConfig)
    #if($item.field=="id")
    @TableId(type = IdType.AUTO)
    @SchemaProperty(value = "$item.desc",hidden=$item.isNotShowSwagger,required = $item.isNeedInput)
    private ${item.type} $item.field;
    #elseif($item.field=="createTime")
    @SchemaProperty(value = "$item.desc",hidden=$item.isNotShowSwagger,required = $item.isNeedInput)
    @TableField(fill = FieldFill.INSERT)
    private ${item.type} $item.field;
    #elseif($item.field=="updateTime")
    @SchemaProperty(value = "$item.desc",hidden=$item.isNotShowSwagger,required = $item.isNeedInput)
    @TableField(fill = FieldFill.UPDATE)
    private ${item.type} $item.field;
    #elseif($item.field=="deleted")
    @TableLogic
    @SchemaProperty(value = "$item.desc",hidden=$item.isNotShowSwagger,required = $item.isNeedInput)
    private ${item.type} $item.field;
    #else
    @SchemaProperty(value = "$item.desc",hidden=$item.isNotShowSwagger,required = $item.isNeedInput)
    #getValid($item)
    private ${item.type} $item.field;
    #end
#end

/**
* 获取主键值
*
* @return 主键值
*/
@Override
protected Serializable pkVal() {
return this.${dbTableConfig.uniKey};
}
}
