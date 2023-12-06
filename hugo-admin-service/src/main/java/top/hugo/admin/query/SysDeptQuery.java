package top.hugo.admin.query;

import lombok.Data;
import top.hugo.domain.PageAndTimeRangeQuery;

/**
 * 部门表请求接收类
 *
 * @author kuanghua
 * @since 2023-11-20 09:38:20
 */
@Data
public class SysDeptQuery extends PageAndTimeRangeQuery {
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门状态（0正常 1停用）
     */
    private String status;
}
