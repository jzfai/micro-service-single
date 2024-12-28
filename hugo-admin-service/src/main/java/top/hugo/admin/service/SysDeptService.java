package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import top.hugo.admin.entity.SysDept;
import top.hugo.admin.mapper.SysDeptMapper;
import top.hugo.admin.query.SysDeptQuery;
import top.hugo.admin.vo.SysDeptVo;
import top.hugo.domain.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 平台信息 服务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysDeptService {
    private final SysDeptMapper sysDeptMapper;

    public TableDataInfo<SysDeptVo> selectPageSysDeptList(SysDeptQuery sysDeptQuery) {
        LambdaQueryWrapper<SysDept> lqw = getQueryWrapper(sysDeptQuery);
        IPage<SysDeptVo> page = sysDeptMapper.selectVoPage(sysDeptQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysDeptQuery 平台信息
     * @return 平台信息集合
     */

    public List<SysDeptVo> selectSysDeptList(SysDeptQuery sysDeptQuery) {
        LambdaQueryWrapper<SysDept> lqw = getQueryWrapper(sysDeptQuery);
        return sysDeptMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysDept> getQueryWrapper(SysDeptQuery sysDeptQuery) {
        LambdaQueryWrapper<SysDept> lqw = new LambdaQueryWrapper<SysDept>();
        lqw.like(ObjectUtil.isNotEmpty(sysDeptQuery.getDeptName()), SysDept::getDeptName, sysDeptQuery.getDeptName());
        lqw.like(ObjectUtil.isNotEmpty(sysDeptQuery.getStatus()), SysDept::getStatus, sysDeptQuery.getStatus());
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysDeptVo> selectSysDeptAll() {
        return sysDeptMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysDeptId 平台ID
     * @return 角色对象信息
     */

    public SysDept selectSysDeptById(Long sysDeptId) {
        return sysDeptMapper.selectById(sysDeptId);
    }


    /**
     * 删除平台信息
     *
     * @param sysDeptId 平台ID
     * @return 结果
     */

    public int deleteSysDeptById(Long sysDeptId) {
        return sysDeptMapper.deleteById(sysDeptId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysDeptIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysDeptByIds(Long[] sysDeptIds) {
        return sysDeptMapper.deleteBatchIds(Arrays.asList(sysDeptIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysDept 平台信息
     * @return 结果
     */

    public int insertSysDept(SysDept sysDept) {
        return sysDeptMapper.insert(sysDept);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysDept 平台信息
     * @return 结果
     */

    public int updateSysDept(SysDept sysDept) {
        return sysDeptMapper.updateById(sysDept);
    }

}