package top.hugo.admin.service;

import top.hugo.admin.entity.SysRole;
import top.hugo.admin.query.SysRoleQuery;
import top.hugo.admin.vo.SysRoleVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 角色信息表 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-20 14:36:16
 */
interface ISysRoleService {


    TableDataInfo<SysRoleVo> selectPageSysRoleList();


    /**
     * 查询角色信息表集合
     *
     * @param sysRole 角色信息表
     * @return 角色信息表集合
     */

    List<SysRoleVo> selectSysRoleList(SysRoleQuery sysRole);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysRoleVo> selectSysRoleAll();

    /**
     * 通过平台ID查询角色信息表
     *
     * @param sysRoleId 平台ID
     * @return 角色对象信息
     */

    SysRole selectSysRoleById(Long sysRoleId);


    /**
     * 删除角色信息表
     *
     * @param sysRoleId 平台ID
     * @return 结果
     */

    int deleteSysRoleById(Long sysRoleId);

    /**
     * 批量删除角色信息表
     *
     * @param sysRoleIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysRoleByIds(Long[] sysRoleIds);

    /**
     * 新增保存角色信息表
     *
     * @param sysRole 角色信息表
     * @return 结果
     */

    int insertSysRole(SysRole sysRole);


    /**
     * 修改保存角色信息表
     *
     * @param sysRole 角色信息表
     * @return 结果
     */
    int updateSysRole(SysRole sysRole);

}