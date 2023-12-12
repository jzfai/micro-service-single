package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hugo.admin.entity.SysRole;
import top.hugo.admin.entity.SysRoleMenu;
import top.hugo.admin.mapper.SysRoleMapper;
import top.hugo.admin.mapper.SysRoleMenuMapper;
import top.hugo.admin.query.SysRoleQuery;
import top.hugo.admin.vo.SysRoleVo;
import top.hugo.common.dto.SysRoleDto;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 平台信息 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysRoleService {
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;

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
     * @param sysRoleDto 平台信息
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    public int insertSysRole(SysRoleDto sysRoleDto) {
        SysRole sysRole = BeanCopyUtils.copy(sysRoleDto, SysRole.class);
        //删除role_menu表中role对应原有的menu字段
        updateRoleMenuId(sysRoleDto);
        return sysRoleMapper.insert(sysRole);
    }

    private void updateRoleMenuId(SysRoleDto sysRole) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, sysRole.getRoleId());
        sysRoleMenuMapper.delete(wrapper);
        //添加role_menu字段
        List<SysRoleMenu> roleMenuList = sysRole.getMenuIds().stream().map(mItem -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(sysRole.getRoleId());
            sysRoleMenu.setMenuId(mItem);
            return sysRoleMenu;
        }).collect(Collectors.toList());
        sysRoleMenuMapper.insertBatch(roleMenuList);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysRoleDto 平台信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateSysRole(SysRoleDto sysRoleDto) {
        updateRoleMenuId(sysRoleDto);
        SysRole sysRole = BeanCopyUtils.copy(sysRoleDto, SysRole.class);
        return sysRoleMapper.updateById(sysRole);
    }

}