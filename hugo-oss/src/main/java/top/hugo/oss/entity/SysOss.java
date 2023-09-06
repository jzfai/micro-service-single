package top.hugo.oss.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.hugo.common.domain.BaseEntity;

/**
 * OSS对象存储表实体类
 *
 * @author kuanghua
 * @since 2023-09-06 11:14:58
 */
@Data
@TableName(value = "sys_oss")
public class SysOss extends BaseEntity {

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

}
