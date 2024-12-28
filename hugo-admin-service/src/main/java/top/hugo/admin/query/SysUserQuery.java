package top.hugo.admin.query;

import top.hugo.domain.PageAndTimeRangeQuery;
import lombok.Data;

/**
 * 用户信息表请求接收类
 *
 * @author kuanghua
 * @since 2023-11-20 14:48:14
 */
@Data
public class SysUserQuery extends PageAndTimeRangeQuery {
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;
}
