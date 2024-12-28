package top.hugo.admin.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

/**
 * 字典数据表请求接收类
 *
 * @author kuanghua
 * @since 2023-11-13 15:53:03
 */
@Data
public class SysDictDataQuery extends PageAndTimeRangeQuery {
    /**
     * 字典编码
     */
    private Long dictCode;
    /**
     * 字典标签
     */
    private String dictLabel;
    //状态（0正常 1停用）
    private String status;
    /**
     * 字典类型
     */
    private String dictType;
}
