package top.hugo.admin.query;

import lombok.Data;
import top.hugo.domain.PageAndTimeRangeQuery;

/**
 * 平台请求接收类
 *
 * @author kuanghua
 * @since 2023-11-10 11:54:52
 */
@Data
public class SysPlatformQuery extends PageAndTimeRangeQuery {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 平台的名字
     */
    private String name;
}
