package top.hugo.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
*  数据库信息实体类
*
* @author 超级管理员
* @since 2024-06-13 14:20:53
*/

@Data
@TableName(value = "t_database_config")
public class DatabaseConfig {
    //id
    @TableId(type = IdType.AUTO )
    private Integer id;

    //数据库名
    private String name;
    //密码
    private String password;
    //数据库类型:1=mysql,2=mongo,3=starrocks,4=clickhouse
    private String type;
    //地址
    private String url;
    //用户名
    private String userName;

    //备注
    private String remark;

}
