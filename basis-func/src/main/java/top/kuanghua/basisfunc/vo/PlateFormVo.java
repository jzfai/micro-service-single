






package top.kuanghua.basisfunc.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
/**
*  平台实体类
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/
@Data
@ApiModel("平台")
public class PlateFormVo  {
@ApiModelProperty(value = "主键")
private Integer id;
@ApiModelProperty(value = "平台名字")
private String name;
}
