#parse("utils.vm")
package ${basicConfig.packageName}.dto;
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
public class ${dbTableConfig.tableNameCase}Dto {
#foreach($item in $formConfig)
  /**
   * $item.desc
  */
  #getValid($item)
    private ${item.type} $item.field;
  #end
}