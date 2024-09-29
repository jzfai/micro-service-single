package top.hugo.admin.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息表返回实体类
 *
 * @author kuanghua
 * @since 2023-11-20 14:48:14
 */
@Data
public class SysUserVo {
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 部门ID
     */
    private Long deptId;


    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 最后登录时间
     */
    private Date loginDate;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 备注
     */
    private String remark;
    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 部门id
     */
    private String postIds;
    /**
     * 部门名
     */
    private String postNames;
    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;
}
