package top.hugo.admin.mapper;

import top.hugo.admin.entity.SysMenu;
import top.hugo.admin.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色权限mapper
 *
 * @author kuanghua
 * @since 2023-08-22 16:57:10
 */

public interface RbacMapper {
    List<SysRole> selectRolesByUserId(Long userId);

    List<SysMenu> selectMenuAll(int platformId);

    List<SysMenu> selectMenuByUserId(@Param("userId") Long userId, @Param("platformId") int platformId);

    List<SysMenu> selectBtnPermByUserId(@Param("userId") Long userId, @Param("platformId") int platformId);


    /*根据角色查询菜单*/
    List<SysMenu> selectMenuListByRoleId(@Param("roleId") Long roleId);

    List<SysMenu> selectMenuListByRolePlatformId(@Param("roleId") Long roleId, @Param("platformId") int platformId);
}