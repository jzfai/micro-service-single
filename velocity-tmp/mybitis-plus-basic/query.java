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
public class ${dbTableConfig.tableNameCase}Query {
#foreach($item in $queryConfig)
@SchemaProperty(value = "$item.desc",hidden=$item.isNotShowSwagger)
    #getValid($item)
private ${item.type} $item.field;
#end
}
