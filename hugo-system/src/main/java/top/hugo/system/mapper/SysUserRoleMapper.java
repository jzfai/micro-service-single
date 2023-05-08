package top.hugo.system.mapper;


import top.hugo.common.mapper.BaseMapperPlus;
import top.hugo.system.entity.SysUserRole;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author hugo
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRoleMapper, SysUserRole, SysUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
