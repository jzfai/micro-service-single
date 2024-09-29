package top.hugo.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 平台实体类
 *
 * @author kuanghua
 * @since 2023-11-10 11:54:52
 */
@Data
@TableName(value = "sys_platform")
public class SysPlatform {
    //主键
    @TableId(type = IdType.AUTO)
    private Integer id;

    //平台的名字
    private String name;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //更新人
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    //删除标志（0代表存在 2代表删除）
    @TableLogic
    private Integer delFlag;


}
