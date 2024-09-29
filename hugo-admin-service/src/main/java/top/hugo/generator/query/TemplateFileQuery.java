package top.hugo.generator.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

/**
 * 请求接收类
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Data
public class TemplateFileQuery extends PageAndTimeRangeQuery {
    /**
     * 文件存储名
     */
    private String name;


    /**
     * 项目类型:1=前端,2基础层,3应用层
     */
    private Integer projectType;



    /**
     * 基础层id
     */
    private Integer baseLayerId;
}
