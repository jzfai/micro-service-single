package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.admin.entity.SysPlatform;
import top.hugo.admin.mapper.SysPlatformMapper;
import top.hugo.admin.query.SysPlatformQuery;
import top.hugo.admin.vo.SysPlatformVo;
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
public class SysPlatformService {
    private final SysPlatformMapper sysPlatformMapper;

    public TableDataInfo<SysPlatformVo> selectPageSysPlatformList(SysPlatformQuery sysPlatformQuery) {
        LambdaQueryWrapper<SysPlatform> lqw = new LambdaQueryWrapper<SysPlatform>();
        lqw.like(ObjectUtil.isNotEmpty(sysPlatformQuery.getName()), SysPlatform::getName, sysPlatformQuery.getName());
        lqw.orderByDesc(SysPlatform::getCreateTime).orderByDesc(SysPlatform::getUpdateTime);
        IPage<SysPlatformVo> page = sysPlatformMapper.selectVoPage(sysPlatformQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysPlatformQuery 平台信息
     * @return 平台信息集合
     */
    public List<SysPlatformVo> selectSysPlatformList(SysPlatformQuery sysPlatformQuery) {
        LambdaQueryWrapper<SysPlatform> lqw = new LambdaQueryWrapper<SysPlatform>();
        lqw.like(ObjectUtil.isNotEmpty(sysPlatformQuery.getId()), SysPlatform::getId, sysPlatformQuery.getId());
        lqw.like(ObjectUtil.isNotEmpty(sysPlatformQuery.getName()), SysPlatform::getName, sysPlatformQuery.getName());
        return sysPlatformMapper.selectVoList(lqw);
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysPlatformVo> selectSysPlatformAll() {
        return sysPlatformMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysPlatformId 平台ID
     * @return 角色对象信息
     */

    public SysPlatform selectSysPlatformById(Long sysPlatformId) {
        return sysPlatformMapper.selectById(sysPlatformId);
    }


    /**
     * 删除平台信息
     *
     * @param sysPlatformId 平台ID
     * @return 结果
     */

    public int deleteSysPlatformById(Long sysPlatformId) {
        return sysPlatformMapper.deleteById(sysPlatformId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysPlatformIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysPlatformByIds(Long[] sysPlatformIds) {
        return sysPlatformMapper.deleteBatchIds(Arrays.asList(sysPlatformIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysPlatform 平台信息
     * @return 结果
     */

    public int insertSysPlatform(SysPlatform sysPlatform) {
        return sysPlatformMapper.insert(sysPlatform);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysPlatform 平台信息
     * @return 结果
     */

    public int updateSysPlatform(SysPlatform sysPlatform) {
        return sysPlatformMapper.updateById(sysPlatform);
    }

}