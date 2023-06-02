#parse("utils.vm")
package ${basicConfig.packageName}.entity;
import java.util.Date;
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
public class ${dbTableConfig.tableNameCase}Vo  {
#foreach($item in $tableConfig)
@SchemaProperty(value = "$item.desc")
private ${item.type} $item.field;
#end
}
