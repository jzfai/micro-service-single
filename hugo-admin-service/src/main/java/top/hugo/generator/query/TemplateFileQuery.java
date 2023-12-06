package top.hugo.generator.query;

import lombok.Data;
import top.hugo.domain.PageAndTimeRangeQuery;

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
}
