package top.hugo.admin.query;

import lombok.Data;
import top.hugo.domain.PageAndTimeRangeQuery;

/**
 * 字典类型表请求接收类
 *
 * @author kuanghua
 * @since 2023-11-13 10:58:29
 */
@Data
public class SysDictypeQuery extends PageAndTimeRangeQuery {
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
}
