package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.admin.entity.SysMenu;
import top.hugo.admin.mapper.SysMenuMapper;
import top.hugo.admin.query.SysMenuQuery;
import top.hugo.admin.vo.SysMenuVo;
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
public class SysMenuService {
    private final SysMenuMapper sysMenuMapper;

    public TableDataInfo<SysMenuVo> selectPageSysMenuList(SysMenuQuery sysMenuQuery) {
        LambdaQueryWrapper<SysMenu> lqw = getQueryWrapper(sysMenuQuery);
        IPage<SysMenuVo> page = sysMenuMapper.selectVoPage(sysMenuQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysMenuQuery 平台信息
     * @return 平台信息集合
     */

    public List<SysMenuVo> selectSysMenuList(SysMenuQuery sysMenuQuery) {
        LambdaQueryWrapper<SysMenu> lqw = getQueryWrapper(sysMenuQuery);
        return sysMenuMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysMenu> getQueryWrapper(SysMenuQuery sysMenuQuery) {
        LambdaQueryWrapper<SysMenu> lqw = new LambdaQueryWrapper<SysMenu>();
        lqw.like(ObjectUtil.isNotEmpty(sysMenuQuery.getMenuName()), SysMenu::getMenuName, sysMenuQuery.getMenuName());
        lqw.like(ObjectUtil.isNotEmpty(sysMenuQuery.getPlatformId()), SysMenu::getPlatformId, sysMenuQuery.getPlatformId());
        lqw.like(ObjectUtil.isNotEmpty(sysMenuQuery.getStatus()), SysMenu::getStatus, sysMenuQuery.getStatus());
        lqw.like(ObjectUtil.isNotEmpty(sysMenuQuery.getMenuType()), SysMenu::getMenuType, sysMenuQuery.getMenuType());
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysMenuVo> selectSysMenuAll() {
        return sysMenuMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysMenuId 平台ID
     * @return 角色对象信息
     */

    public SysMenu selectSysMenuById(Long sysMenuId) {
        return sysMenuMapper.selectById(sysMenuId);
    }


    /**
     * 删除平台信息
     *
     * @param sysMenuId 平台ID
     * @return 结果
     */

    public int deleteSysMenuById(Long sysMenuId) {
        return sysMenuMapper.deleteById(sysMenuId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysMenuIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysMenuByIds(Long[] sysMenuIds) {
        return sysMenuMapper.deleteBatchIds(Arrays.asList(sysMenuIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysMenu 平台信息
     * @return 结果
     */

    public int insertSysMenu(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysMenu 平台信息
     * @return 结果
     */

    public int updateSysMenu(SysMenu sysMenu) {
        return sysMenuMapper.updateById(sysMenu);
    }

}