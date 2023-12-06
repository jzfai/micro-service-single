package top.hugo.admin.service;

import top.hugo.admin.entity.SysUser;
import top.hugo.admin.query.SysUserQuery;
import top.hugo.admin.vo.SysUserVo;
import top.hugo.domain.TableDataInfo;

import java.util.List;

/**
 * 用户信息表 服务层处理
 *
 * @author kuanghua
 * @since 2023-11-20 14:48:14
 */
interface ISysUserService {


    TableDataInfo<SysUserVo> selectPageSysUserList();


    /**
     * 查询用户信息表集合
     *
     * @param sysUser 用户信息表
     * @return 用户信息表集合
     */

    List<SysUserVo> selectSysUserList(SysUserQuery sysUser);

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    List<SysUserVo> selectSysUserAll();

    /**
     * 通过平台ID查询用户信息表
     *
     * @param sysUserId 平台ID
     * @return 角色对象信息
     */

    SysUser selectSysUserById(Long sysUserId);


    /**
     * 删除用户信息表
     *
     * @param sysUserId 平台ID
     * @return 结果
     */

    int deleteSysUserById(Long sysUserId);

    /**
     * 批量删除用户信息表
     *
     * @param sysUserIds 需要删除的平台ID
     * @return 结果
     */
    int deleteSysUserByIds(Long[] sysUserIds);

    /**
     * 新增保存用户信息表
     *
     * @param sysUser 用户信息表
     * @return 结果
     */

    int insertSysUser(SysUser sysUser);


    /**
     * 修改保存用户信息表
     *
     * @param sysUser 用户信息表
     * @return 结果
     */
    int updateSysUser(SysUser sysUser);

}