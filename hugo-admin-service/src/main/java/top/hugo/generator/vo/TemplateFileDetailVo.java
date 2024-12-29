package top.hugo.generator.vo;
import lombok.Data;
/**
*  模板详情返回实体类
*
* @author 邝华
* @since 2024-04-28 10:42:26
*/
@Data
public class TemplateFileDetailVo  {
  private Integer id;

  //模板文件名
  private String name;
  //模块名
  private String modelName;
  //生成的文件名
  private String packageSuffix;

  //生成的文件名
  private Integer templateId;

  //所属模块(1=项目，2=模块，3=包)
  private Integer modelType;

  //源类型
  private Integer resourceType;

  //文件名前缀
  private String fileNamePre;

  //备注
  private String remark;


  //是否注入基础名
  private Integer injectBasicName;


  //是否参与项目生成;0=隐藏,1=正常
  private Integer status;
}
