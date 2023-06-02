#parse("utils.vm")
package ${basicConfig.packageName}.vo;
import java.util.Date;
/**
*  ${dbTableConfig.tableDesc}返回实体类
*
* @author ${basicConfig.author}
* @since ${basicConfig.dataTime}
*/
@Data
public class ${dbTableConfig.tableNameCase}Vo  {
#foreach($item in $tableConfig)
  /**
   * ${item.desc}
   */
  private ${item.type} $item.field;
#end
}
