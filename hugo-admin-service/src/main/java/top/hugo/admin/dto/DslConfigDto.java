package top.hugo.admin.dto;

import lombok.Data;

/**
 * dsl配置请求接收类
 *
 * @author 邝华
 * @since 2023-10-17 10:42:10
 */
@Data
public class DslConfigDto {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 数据源
     */
    private String dataSource;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 平台名称
     */
    private String platName;





}