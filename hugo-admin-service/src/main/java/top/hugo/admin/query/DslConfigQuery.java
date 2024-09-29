


package top.hugo.admin.query;
import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

/**
*  dsl配置请求接收类
*
* @author 邝华
* @since 2023-10-17 10:42:10
*/
@Data
public class DslConfigQuery extends PageAndTimeRangeQuery {
  /**
   * 字典名称
  */
    private String dictName;
  /**
   * 平台名称
  */
    private String platName;




}
