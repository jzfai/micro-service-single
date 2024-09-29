package top.hugo.admin.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

/**
 * 岗位信息表请求接收类
 *
 * @author kuanghua
 * @since 2023-11-20 09:39:53
 */
@Data
public class SysPostQuery extends PageAndTimeRangeQuery {
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 状态（0正常 1停用）
     */
    private String status;
}
