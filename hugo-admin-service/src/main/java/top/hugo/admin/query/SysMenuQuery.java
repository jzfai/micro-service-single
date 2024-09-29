package top.hugo.admin.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

/**
 * 菜单权限表请求接收类
 *
 * @author kuanghua
 * @since 2023-11-16 10:14:25
 */
@Data
public class SysMenuQuery extends PageAndTimeRangeQuery {
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 平台id
     */
    private Integer platformId;
    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;
}
