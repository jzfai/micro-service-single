package top.hugo.generator.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;
/**
*  数据库信息请求接收类
*
* @author 超级管理员
* @since 2024-06-13 14:20:53
*/
@Data
public class DatabaseConfigQuery extends PageAndTimeRangeQuery {
  /**
   * 数据库名
  */
    private String name;
  /**
   * 数据库类型
  */
    private String type;
}
