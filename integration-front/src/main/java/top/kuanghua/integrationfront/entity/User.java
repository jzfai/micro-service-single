package top.kuanghua.integrationfront.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表;uploadFileBtn;downloadFilebtn(User)表实体类
 *
 * @author kuanghua
 * @since 2020-12-13 16:59:51
 */
@Data
@ApiModel("用户表")
@TableName(value = "tb_user")
//https://www.cnblogs.com/Nickc/p/12001764.html
public class User extends Model<User> {
    @ApiModelProperty(value = "id",hidden = false)
    private Integer id;
    @ApiModelProperty(value = "用户名")
    @Size(max = 20,min = 5,message = "创建人姓名长度需要在2-20字符长度之间")
    private String username;
    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotBlank(message = "电话不能为空")
    @ApiModelProperty(value = "电话")
    private String phone;
    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "创建时间",hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ApiModelProperty(value = "更新时间",hidden = true)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @ApiModelProperty(value = "是否删除",hidden = true)
    @TableLogic
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String deleted;
    @ApiModelProperty(value = "加密的盐",hidden = true)
    @TableField()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;
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