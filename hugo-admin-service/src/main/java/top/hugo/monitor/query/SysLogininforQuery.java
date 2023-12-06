package top.hugo.monitor.query;

import lombok.Data;
import top.hugo.domain.PageAndTimeRangeQuery;

import java.util.Date;

/**
 * 系统访问记录请求接收类
 *
 * @author kuanghua
 * @since 2023-11-17 10:02:07
 */
@Data
public class SysLogininforQuery extends PageAndTimeRangeQuery {
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 登录IP地址
     */
    private String ipaddr;
    /**
     * 访问时间
     */
    private Date loginTime;
    /**
     * 登录状态（0成功 1失败）
     */
    private String status;
    /**
     * 用户账号
     */
    private String userName;
}
