package top.hugo.generator.vo;

import lombok.Data;

/**
 * 数据库信息返回实体类
 *
 * @author 超级管理员
 * @since 2024-06-13 14:20:53
 */
@Data
public class DatabaseConfigVo {
    /**
     * id
     */
    private Integer id;
    /**
     * 数据库名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    //用户名
    private String userName;

    //备注
    private String remark;
    /**
     * 数据库类型
     */
    private String type;
    /**
     * 地址
     */
    private String url;
}
