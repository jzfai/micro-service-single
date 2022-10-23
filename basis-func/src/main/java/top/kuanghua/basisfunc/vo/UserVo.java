






package top.kuanghua.basisfunc.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
/**
*  用户实体类
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/
@Data
@ApiModel("用户")
public class UserVo  {
@ApiModelProperty(value = "用户ID")
private Long id;
@ApiModelProperty(value = "姓名")
private String name;
@ApiModelProperty(value = "头像图片地址")
private String headImgUrl;
@ApiModelProperty(value = "手机号码")
private String phone;
@ApiModelProperty(value = "角色id数组")
private String roleId;
@ApiModelProperty(value = "创建时间")
private Date createTime;
@ApiModelProperty(value = "创建人")
private String creator;
@ApiModelProperty(value = "修改时间")
private Date updateTime;
@ApiModelProperty(value = "修改人")
private String editor;
@ApiModelProperty(value = "逻辑删除")
private Integer deleted;
}
