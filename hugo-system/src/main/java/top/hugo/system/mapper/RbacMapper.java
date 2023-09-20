package top.hugo.system.mapper;


import org.apache.ibatis.annotations.Param;
import top.hugo.system.entity.SysMenu;
import top.hugo.system.entity.SysRole;

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

    List<SysMenu> selectBtnPermByUserId(@Param("userId") Long userId, @Param("platformId") Integer platformId);
}