package top.hugo.generator.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Title: 数据库信息字段
 * @Description:
 * @Auther: kuanghua
 * @create 2020/12/15 12:01
 */
@Data
public class DataBaseInfo {
    @NotBlank(message = "url不能为空")
    private String url;
    @NotBlank(message = "userName不能为空")
    private String userName;
    @NotBlank(message = "password不能为空")
    private String password;
    private String dbName;
    private String tbName;
    // 数据库配置主键ID
    private Integer dbConfigId;
}
