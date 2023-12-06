package top.hugo.admin.service;

import top.hugo.admin.entity.SysMenu;
import top.hugo.admin.query.SysMenuQuery;
import top.hugo.admin.vo.SysMenuVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 菜单权限表 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-16 10:14:25
 */
interface ISysMenuService {


    TableDataInfo<SysMenuVo> selectPageSysMenuList();


    /**
     * 查询菜单权限表集合
     *
     * @param sysMenu 菜单权限表
     * @return 菜单权限表集合
     */

    List<SysMenuVo> selectSysMenuList(SysMenuQuery sysMenu);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysMenuVo> selectSysMenuAll();

    /**
     * 通过平台ID查询菜单权限表
     *
     * @param sysMenuId 平台ID
     * @return 角色对象信息
     */

    SysMenu selectSysMenuById(Long sysMenuId);


    /**
     * 删除菜单权限表
     *
     * @param sysMenuId 平台ID
     * @return 结果
     */

    int deleteSysMenuById(Long sysMenuId);

    /**
     * 批量删除菜单权限表
     *
     * @param sysMenuIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysMenuByIds(Long[] sysMenuIds);

    /**
     * 新增保存菜单权限表
     *
     * @param sysMenu 菜单权限表
     * @return 结果
     */

    int insertSysMenu(SysMenu sysMenu);


    /**
     * 修改保存菜单权限表
     *
     * @param sysMenu 菜单权限表
     * @return 结果
     */
    int updateSysMenu(SysMenu sysMenu);

}