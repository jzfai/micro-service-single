package top.hugo.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * OSS对象存储表实体类
 *
 * @author kuanghua
 * @since 2023-09-06 11:14:58
 */
@Data
@TableName(value = "sys_oss")
public class SysOss {

    //文件名
    private String fileName;
    //文件后缀名
    private String fileSuffix;
    //原名
    private String originalName;
    //对象存储主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long ossId;

    //服务商
    private String service;

    //URL地址
    private String url;
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //更新者
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
