






package top.kuanghua.basisfunc.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@TableName(value = "t_user")
public class User extends Model<User> {
    @ApiModelProperty(value = "用户ID",hidden=false)
    private Long id;
    @ApiModelProperty(value = "姓名",hidden=false)
        @NotBlank(message = "name不能为空")
    private String name;
    @ApiModelProperty(value = "头像图片地址",hidden=false)
    private String headImgUrl;
    @ApiModelProperty(value = "手机号码",hidden=false)
        @NotBlank(message = "phone不能为空")
    @Pattern(regexp = "^0?1[0-9]{10}$",message = "phone输入有误-手机号")
    private String phone;
    @ApiModelProperty(value = "密码加盐",hidden=true)
    private String salt;
    @ApiModelProperty(value = "登录密码",hidden=true)
    private String password;
    @ApiModelProperty(value = "角色id数组",hidden=false)
    private String roleId;
    @ApiModelProperty(value = "创建时间",hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;
    @ApiModelProperty(value = "修改时间",hidden = true)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @ApiModelProperty(value = "修改人",hidden=true)
    private String editor;
    @TableLogic
    @ApiModelProperty(value = "逻辑删除",hidden=true)
    private Integer deleted;

/**
* 获取主键值
*
* @return 主键值
*/
@Override
protected Serializable pkVal() {
return this.id;
}
}
