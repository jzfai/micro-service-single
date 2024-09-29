package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 岗位信息表实体类
 *
 * @author kuanghua
 * @since 2023-11-20 09:39:53
 */
@Data
@TableName(value = "sys_post")
public class SysPost {

    //创建者
    @TableField(fill = FieldFill.INSERT)
    private String createBy;


    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    //岗位编码
    private String postCode;
    @TableId(type = IdType.AUTO)

    //岗位ID
    private Long postId;

    //岗位名称
    private String postName;

    //显示顺序
    private Integer postSort;

    //备注
    private String remark;

    //状态（0正常 1停用）
    private String status;

    //更新者
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;


    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}
