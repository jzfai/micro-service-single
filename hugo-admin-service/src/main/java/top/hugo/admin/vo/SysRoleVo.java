package top.hugo.admin.vo;

import lombok.Data;

import java.util.Date;

/**
 * 角色信息表返回实体类
 *
 * @author kuanghua
 * @since 2023-11-20 14:36:16
 */
@Data
public class SysRoleVo {
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    private String dataScope;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 部门树选择项是否关联显示
     */
    private Integer deptCheckStrictly;
    /**
     * 菜单树选择项是否关联显示
     */
    private Integer menuCheckStrictly;
    /**
     * 平台id(,号分割)
     */
    private String platformIds;
    /**
     * 备注
     */
    private String remark;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 角色权限字符串
     */
    private String roleKey;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 显示顺序
     */
    private Integer roleSort;
    /**
     * 角色状态（0正常 1停用）
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
}
