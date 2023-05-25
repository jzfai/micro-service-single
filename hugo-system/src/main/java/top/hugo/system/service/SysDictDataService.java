package top.hugo.system.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.constant.CacheNames;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.exception.ServiceException;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.redis.CacheUtils;
import top.hugo.system.entity.SysDictData;
import top.hugo.system.mapper.SysDictDataMapper;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class SysDictDataService {

    private final SysDictDataMapper baseMapper;


    public TableDataInfo<SysDictData> selectPageDictDataList(SysDictData dictData, PageQuery pageQuery) {
        LambdaQueryWrapper<SysDictData> lqw = new LambdaQueryWrapper<SysDictData>()
                .eq(ObjectUtil.isNotEmpty(dictData.getDictType()), SysDictData::getDictType, dictData.getDictType())
                .like(ObjectUtil.isNotEmpty(dictData.getDictLabel()), SysDictData::getDictLabel, dictData.getDictLabel())
                .eq(ObjectUtil.isNotEmpty(dictData.getStatus()), SysDictData::getStatus, dictData.getStatus())
                .orderByAsc(SysDictData::getDictSort);
        Page<SysDictData> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */

    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return baseMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                .eq(ObjectUtil.isNotEmpty(dictData.getDictType()), SysDictData::getDictType, dictData.getDictType())
                .like(ObjectUtil.isNotEmpty(dictData.getDictLabel()), SysDictData::getDictLabel, dictData.getDictLabel())
                .eq(ObjectUtil.isNotEmpty(dictData.getStatus()), SysDictData::getStatus, dictData.getStatus())
                .orderByAsc(SysDictData::getDictSort));
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */

    public String selectDictLabel(String dictType, String dictValue) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysDictData>()
                        .select(SysDictData::getDictLabel)
                        .eq(SysDictData::getDictType, dictType)
                        .eq(SysDictData::getDictValue, dictValue))
                .getDictLabel();
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */

    public SysDictData selectDictDataById(Long dictCode) {
        return baseMapper.selectById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */

    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            baseMapper.deleteById(dictCode);
            CacheUtils.evict(CacheNames.SYS_DICT, data.getDictType());
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    //@CachePut(cacheNames = CacheNames.SYS_DICT, key = "#data.dictType")
    public List<SysDictData> insertDictData(SysDictData data) {
        int row = baseMapper.insert(data);
        if (row > 0) {
            return baseMapper.selectDictDataByType(data.getDictType());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    //@CachePut(cacheNames = CacheNames.SYS_DICT, key = "#data.dictType")
    public List<SysDictData> updateDictData(SysDictData data) {
        int row = baseMapper.updateById(data);
        if (row > 0) {
            return baseMapper.selectDictDataByType(data.getDictType());
        }
        throw new ServiceException("操作失败");
    }

}
