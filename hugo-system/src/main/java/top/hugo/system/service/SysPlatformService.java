package top.hugo.system.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.helper.LoginHelper;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.JsonUtils;
import top.hugo.system.entity.SysPlatform;
import top.hugo.system.entity.SysRole;
import top.hugo.system.mapper.SysPlatformMapper;
import top.hugo.system.query.SysPlatformQuery;
import top.hugo.system.vo.SysPlatformVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 平台信息 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysPlatformService {

    private final SysPlatformMapper sysPlatformMapper;
    private final SysRoleService sysRoleService;

    public TableDataInfo<SysPlatformVo> selectPagePlatformList(SysPlatformQuery platform, PageQuery pageQuery) {
        LambdaQueryWrapper<SysPlatform> lqw = new LambdaQueryWrapper<SysPlatform>()
                .like(ObjectUtil.isNotEmpty(platform.getName()), SysPlatform::getName, platform.getName())
                .orderByDesc(SysPlatform::getCreateTime).orderByDesc(SysPlatform::getUpdateTime);
        IPage<SysPlatformVo> page = sysPlatformMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param platform 平台信息
     * @return 平台信息集合
     */

    public List<SysPlatformVo> selectPlatformList(SysPlatformQuery platform) {
        LambdaQueryWrapper<SysPlatform> lqw = new LambdaQueryWrapper<SysPlatform>()
                .like(ObjectUtil.isNotEmpty(platform.getName()), SysPlatform::getName, platform.getName());
        return sysPlatformMapper.selectVoList(lqw);
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysPlatformVo> selectPlatformAll() {
        return sysPlatformMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param platformId 平台ID
     * @return 角色对象信息
     */

    public SysPlatform selectPlatformById(Long platformId) {
        return sysPlatformMapper.selectById(platformId);
    }


    /**
     * 删除平台信息
     *
     * @param platformId 平台ID
     * @return 结果
     */

    public int deletePlatformById(Long platformId) {
        return sysPlatformMapper.deleteById(platformId);
    }

    /**
     * 批量删除平台信息
     *
     * @param platformIds 需要删除的平台ID
     * @return 结果
     */
    public int deletePlatformByIds(Long[] platformIds) {
        return sysPlatformMapper.deleteBatchIds(Arrays.asList(platformIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param platform 平台信息
     * @return 结果
     */

    public int insertPlatform(SysPlatform platform) {
        return sysPlatformMapper.insert(platform);
    }

    /**
     * 修改保存平台信息
     *
     * @param platform 平台信息
     * @return 结果
     */

    public int updatePlatform(SysPlatform platform) {
        return sysPlatformMapper.updateById(platform);
    }


    /**
     * 根据用户筛选角色，筛选出具有的平台权限
     *
     * @return
     */

    public List<SysPlatformVo> filterPlatformByUserId() {
        //获取用户id
        Long userId = LoginHelper.getUserId();
        //根据用户id获取角色信息
        List<SysRole> sysRoles = sysRoleService.selectRolesByUserId(userId);
        //过滤角色里的platform
        List<SysPlatformVo> sysPlatformVos = new ArrayList<>();
        if (LoginHelper.isAdmin()) {
            sysPlatformVos = selectPlatformAll();
        } else {
            HashSet<Integer> platformSet = new HashSet<>();
            sysRoles.forEach((fItem) -> {
                HashSet<Integer> hashSet = JsonUtils.parseObject(fItem.getPlatformIds(), HashSet.class);
                if (ObjectUtil.isNotEmpty(hashSet)) {
                    platformSet.addAll(hashSet);
                }
            });
            //查询得到platfrom数据
            sysPlatformVos = platformSet.stream().map(sysPlatformMapper::selectVoById).collect(Collectors.toList());
        }
        return sysPlatformVos;
    }
}
