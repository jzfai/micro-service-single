package top.hugo.system.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.hugo.common.domain.TimeRangeQuery;

/**
 * 平台管理
 *
 * @author kuanghua
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysPlatformQuery extends TimeRangeQuery {
    /**
     * 平台id
     */
    private Integer id;
    /**
     * 平台名字
     */
    private String name;
}
