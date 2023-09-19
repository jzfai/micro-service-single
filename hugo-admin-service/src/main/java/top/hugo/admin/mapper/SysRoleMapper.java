package top.hugo.admin.mapper;

import top.hugo.admin.entity.SysRole;
import top.hugo.db.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author kuanghua
 */
public interface SysRoleMapper extends BaseMapperPlus<SysRoleMapper, SysRole, SysRole> {
    
    //new
    List<SysRole> selectRolesByUserId(Long userId);
}
