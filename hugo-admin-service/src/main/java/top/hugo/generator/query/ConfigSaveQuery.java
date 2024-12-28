package top.hugo.generator.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

/**
 * 代码生成配置保存请求接收类
 *
 * @author kuanghua
 * @since 2023-10-18 11:42:26
 */
@Data
public class ConfigSaveQuery extends PageAndTimeRangeQuery {
    /**
     * 配置名称
     */
    private String name;
}
