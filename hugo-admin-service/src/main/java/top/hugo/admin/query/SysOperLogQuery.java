package top.hugo.admin.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

import java.util.Date;

/**
 * 操作日志记录请求接收类
 *
 * @author kuanghua
 * @since 2023-11-21 13:58:50
 */
@Data
public class SysOperLogQuery extends PageAndTimeRangeQuery {
    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer businessType;
    /**
     * 操作时间
     */
    private Date operTime;
    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private Integer operatorType;

    /**
     * 操作人
     */
    private String operName;
    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;
}
