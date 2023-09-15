package top.hugo.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 平台管理实体类
 *
 * @author 邝华
 * @since 2022-12-16 14:42:22
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
}