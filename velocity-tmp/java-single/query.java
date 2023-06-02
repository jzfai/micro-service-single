#parse("utils.vm")
package ${basicConfig.packageName}.query;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
/**
*  ${dbTableConfig.tableDesc}请求接收类
*
* @author ${basicConfig.author}
* @since ${basicConfig.dataTime}
*/
@Data
public class ${dbTableConfig.tableNameCase}Query extends TimeRangeQuery {
#foreach($item in $queryConfig)
  /**
   * $item.desc
  */
  #getValid($item)
    private ${item.type} $item.field;
  #end
}
