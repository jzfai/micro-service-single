package top.hugo.admin.query;

import lombok.Data;
import top.hugo.domain.PageAndTimeRangeQuery;

/**
 * 角色信息表请求接收类
 *
 * @author kuanghua
 * @since 2023-11-20 14:36:16
 */
@Data
public class SysRoleQuery extends PageAndTimeRangeQuery {
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色状态（0正常 1停用）
     */
    private String status;
}
