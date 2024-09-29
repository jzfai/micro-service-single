package top.hugo.generator.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;
/**
*  模板详情请求接收类
*
* @author 邝华
* @since 2024-04-28 15:46:13
*/
@Data
public class TemplateFileDetailQuery extends PageAndTimeRangeQuery {
  /**
   * 
  */
    private Integer id;
  /**
   * 模板文件名
  */
    private String name;
  /**
   * 所属模块(1=项目，2=模块，3=包)
  */
    private Integer modelType;
    /**
     * 包后缀
    */
    private String packageSuffix;
    /**
     * 模板id
    */
    private Integer templateId;
    /**
     * 模块名或包名
    */
    private String modelName;


    //是否参与项目生成;0=隐藏,1=正常
    private Integer status;
}
