






package top.kuanghua.basisfunc.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.kuanghua.basisfunc.entity.Permission;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
/**
*  权限实体类
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/
@Data
@ApiModel("权限")
public class PermissionVo  extends Permission {
    @ApiModelProperty(value = "子元素", hidden = false)
    private List<PermissionVo> Children;
}
