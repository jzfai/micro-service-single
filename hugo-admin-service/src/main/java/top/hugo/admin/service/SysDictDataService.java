package top.hugo.admin.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.admin.entity.SysDictData;
import top.hugo.admin.mapper.SysDictDataMapper;
import top.hugo.admin.query.SysDictDataQuery;
import top.hugo.admin.vo.SysDictDataVo;
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
public class SysDictDataService {
    private final SysDictDataMapper sysDictDataMapper;

    public TableDataInfo<SysDictDataVo> selectPageSysDictDataList(SysDictDataQuery sysDictDataQuery) {
        LambdaQueryWrapper<SysDictData> lqw = getQueryWrapper(sysDictDataQuery);
        IPage<SysDictDataVo> page = sysDictDataMapper.selectVoPage(sysDictDataQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    /**
     * 查询平台信息集合
     *
     * @param sysDictDataQuery 平台信息
     * @return 平台信息集合
     */

    public List<SysDictDataVo> selectSysDictDataList(SysDictDataQuery sysDictDataQuery) {
        LambdaQueryWrapper<SysDictData> lqw = getQueryWrapper(sysDictDataQuery);
        return sysDictDataMapper.selectVoList(lqw);
    }


    /**
     * 查询wrapper封装
     *
     * @return
     */
    private static LambdaQueryWrapper<SysDictData> getQueryWrapper(SysDictDataQuery sysDictDataQuery) {
        LambdaQueryWrapper<SysDictData> lqw = new LambdaQueryWrapper<SysDictData>();
        lqw.like(ObjectUtil.isNotEmpty(sysDictDataQuery.getDictCode()), SysDictData::getDictCode, sysDictDataQuery.getDictCode());
        lqw.like(ObjectUtil.isNotEmpty(sysDictDataQuery.getDictLabel()), SysDictData::getDictLabel, sysDictDataQuery.getDictLabel());
        lqw.like(ObjectUtil.isNotEmpty(sysDictDataQuery.getStatus()), SysDictData::getStatus, sysDictDataQuery.getStatus());
        lqw.like(ObjectUtil.isNotEmpty(sysDictDataQuery.getDictType()), SysDictData::getDictType, sysDictDataQuery.getDictType());
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<SysDictDataVo> selectSysDictDataAll() {
        return sysDictDataMapper.selectVoList();
    }

    /**
     * 通过平台ID查询平台信息
     *
     * @param sysDictDataId 平台ID
     * @return 角色对象信息
     */

    public SysDictData selectSysDictDataById(Long sysDictDataId) {
        return sysDictDataMapper.selectById(sysDictDataId);
    }


    /**
     * 删除平台信息
     *
     * @param sysDictDataId 平台ID
     * @return 结果
     */

    public int deleteSysDictDataById(Long sysDictDataId) {
        return sysDictDataMapper.deleteById(sysDictDataId);
    }

    /**
     * 批量删除平台信息
     *
     * @param sysDictDataIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteSysDictDataByIds(Long[] sysDictDataIds) {
        return sysDictDataMapper.deleteBatchIds(Arrays.asList(sysDictDataIds));
    }

    /**
     * 新增保存平台信息
     *
     * @param sysDictData 平台信息
     * @return 结果
     */

    public int insertSysDictData(SysDictData sysDictData) {
        return sysDictDataMapper.insert(sysDictData);
    }


    /**
     * 修改保存平台信息
     *
     * @param sysDictData 平台信息
     * @return 结果
     */

    public int updateSysDictData(SysDictData sysDictData) {
        return sysDictDataMapper.updateById(sysDictData);
    }

}