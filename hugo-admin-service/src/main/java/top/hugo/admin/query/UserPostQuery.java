package top.hugo.admin.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

import java.util.Date;

/**
 * @author kuanghua
 * @since 2024-01-11 17:22:30
 */
@Data
public class UserPostQuery extends PageAndTimeRangeQuery {

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 部门id
     */
    private Long deptId;
}
