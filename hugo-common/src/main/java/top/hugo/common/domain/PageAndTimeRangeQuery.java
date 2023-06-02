package top.hugo.common.domain;

import lombok.Data;

/**
 * 查询积累
 *
 * @author kuanghua
 */

@Data
public class PageAndTimeRangeQuery extends PageQuery {
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
}
