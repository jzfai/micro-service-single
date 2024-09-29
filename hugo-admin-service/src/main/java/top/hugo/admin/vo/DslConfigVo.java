


package top.hugo.admin.vo;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*  dsl配置返回实体类
*
* @author 邝华
* @since 2023-10-17 10:42:10
*/
@Data
public class DslConfigVo implements Serializable {
private static final long serialVersionUID = 1L;
  /**
   * 主键id
   */
  @ExcelProperty(value = "主键id")
  private Integer id;
  /**
   * 数据源
   */
  @ExcelProperty(value = "数据源")
  private String dataSource;
  /**
   * 字典名称
   */
  @ExcelProperty(value = "字典名称")
  private String dictName;
  /**
   * 平台名称
   */
  @ExcelProperty(value = "平台名称")
  private String platName;


  //dsl名字
  private String name;

}
