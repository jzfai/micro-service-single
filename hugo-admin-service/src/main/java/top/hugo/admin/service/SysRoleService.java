package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.admin.entity.SysRole;
import top.hugo.admin.mapper.SysRoleMapper;
import top.hugo.admin.query.SysRoleQuery;
import top.hugo.admin.vo.SysRoleVo;
import top.hugo.domain.TableDataInfo;

import java.util.Arrays;
import java.util.List;

/**
 * 平台信息 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysRoleService {
    private final SysRoleMapper sysRoleMapper;

    public TableDataInfo<SysRoleVo> selectPageSysRoleList(SysRoleQuery sysRoleQuery) {
        LambdaQueryWrapper<SysRole> lqw = getQueryWrapper(sysRoleQuery);
        IPage<SysRoleVo> page = sysRoleMapper.selectVoPage(sysRoleQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysRoleQuery 平台信息
     * @return 平台信息集合
     */

    public List<SysRoleVo> selectSysRoleList(SysRoleQuery sysRoleQuery) {
        LambdaQueryWrapper<SysRole> lqw = getQueryWrapper(sysRoleQuery);
        return sysRoleMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysRole> getQueryWrapper(SysRoleQuery sysRoleQuery) {
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>();
        lqw.like(ObjectUtil.isNotEmpty(sysRoleQuery.getRoleName()), SysRole::getRoleName, sysRoleQuery.getRoleName());
        lqw.like(ObjectUtil.isNotEmpty(sysRoleQuery.getStatus()), SysRole::getStatus, sysRoleQuery.getStatus());
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysRoleVo> selectSysRoleAll() {
        return sysRoleMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysRoleId 平台ID
     * @return 角色对象信息
     */

    public SysRole selectSysRoleById(Long sysRoleId) {
        return sysRoleMapper.selectById(sysRoleId);
    }


    /**
     * 删除平台信息
     *
     * @param sysRoleId 平台ID
     * @return 结果
     */

    public int deleteSysRoleById(Long sysRoleId) {
        return sysRoleMapper.deleteById(sysRoleId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysRoleIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysRoleByIds(Long[] sysRoleIds) {
        return sysRoleMapper.deleteBatchIds(Arrays.asList(sysRoleIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysRole 平台信息
     * @return 结果
     */

    public int insertSysRole(SysRole sysRole) {
        return sysRoleMapper.insert(sysRole);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysRole 平台信息
     * @return 结果
     */

    public int updateSysRole(SysRole sysRole) {
        return sysRoleMapper.updateById(sysRole);
    }

}