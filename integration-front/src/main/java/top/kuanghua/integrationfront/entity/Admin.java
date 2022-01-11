package top.kuanghua.integrationfront.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户列表(Admin)表实体类
 *
 * @author kuanghua
 * @since 2021-03-19 10:45:42
 */
@Data
@ApiModel("用户列表")
@TableName(value = "tb_admin")
public class Admin extends Model<Admin> {
    private Integer id;
    private String status;
    private Date addTime;
    @ApiModelProperty(value = "超级管理员(0.不是,1.是)")
    private String isSuper;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "是否删除")
    private String isDelete;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "角色id")
    private Integer roleId;

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
