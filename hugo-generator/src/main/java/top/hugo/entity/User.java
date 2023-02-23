package top.hugo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Data
@TableName(value = "t_user")
public class User extends Model<User> {
    private Long id;
    @NotBlank(message = "name不能为空")
    private String name;
    private String headImgUrl;
    @NotBlank(message = "phone不能为空")
    @Pattern(regexp = "^0?1[0-9]{10}$", message = "phone输入有误-手机号")
    private String phone;
    private String salt;
    private String password;
    private String roleId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private String creator;
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    private String editor;
    @TableLogic
    private Integer deleted;

}
