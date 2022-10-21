package top.kuanghua.basisfunc.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Title: PermissionReceiveBo
 * @Description:
 * @Auther: kuanghua
 * @create 2022-10-21 16:52
 */
@Data
@ApiModel("角色中权限")
public class PermissionReceiveBo {
    @ApiModelProperty(value = "平台id")
    private Integer plateFormId;
    @ApiModelProperty(value = "权限id数组")
    private List<String> PermissionList;
}
