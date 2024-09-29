package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 部门表实体类
 *
 * @author kuanghua
 * @since 2023-11-20 09:38:20
 */
@Data
@TableName(value = "sys_dept")
public class SysDept {

    //祖级列表
    private String ancestors;

    //创建者
    @TableField(fill = FieldFill.INSERT)
    private String createBy;


    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    //删除标志（0代表存在 2代表删除）
    @TableLogic
    private String delFlag;

    @TableId(type = IdType.AUTO)
    //部门id
    private Long deptId;

    //部门名称
    private String deptName;

    //邮箱
    private String email;

    //负责人
    private String leader;

    //显示顺序
    private Integer orderNum;

    //父部门id
    private Long parentId;

    //联系电话
    private String phone;

    //部门状态（0正常 1停用）
    private String status;

    //更新者
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;


    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}
