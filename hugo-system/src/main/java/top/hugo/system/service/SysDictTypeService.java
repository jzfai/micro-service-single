package top.hugo.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hugo.common.constant.CacheNames;
import top.hugo.common.constant.UserConstants;
import top.hugo.common.core.DictService;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.exception.ServiceException;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.spring.SpringUtils;
import top.hugo.common.utils.JsonUtils;
import top.hugo.common.utils.StreamUtils;
import top.hugo.common.utils.StringUtils;
import top.hugo.common.utils.redis.CacheUtils;
import top.hugo.system.entity.SysDictData;
import top.hugo.system.entity.SysDictType;
import top.hugo.system.mapper.SysDictDataMapper;
import top.hugo.system.mapper.SysDictTypeMapper;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class SysDictTypeService implements DictService {
    private final SysDictTypeMapper sysDictTypeMapper;
    private final SysDictDataMapper dictDataMapper;

    public TableDataInfo<SysDictType> selectPageDictTypeList(SysDictType dictType, PageQuery pageQuery) {
        LambdaQueryWrapper<SysDictType> lqw = new LambdaQueryWrapper<SysDictType>()
                .like(StringUtils.isNotBlank(dictType.getDictName()), SysDictType::getDictName, dictType.getDictName())
                .eq(StringUtils.isNotBlank(dictType.getStatus()), SysDictType::getStatus, dictType.getStatus())
                .like(StringUtils.isNotBlank(dictType.getDictType()), SysDictType::getDictType, dictType.getDictType())
                .between(ObjectUtil.isNotEmpty(dictType.getBeginTime()) && ObjectUtil.isNotEmpty(dictType.getEndTime()),
                        SysDictType::getCreateTime, dictType.getBeginTime(), dictType.getEndTime());
        Page<SysDictType> page = sysDictTypeMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        return sysDictTypeMapper.selectList(new LambdaQueryWrapper<SysDictType>()
                .like(StringUtils.isNotBlank(dictType.getDictName()), SysDictType::getDictName, dictType.getDictName())
                .eq(StringUtils.isNotBlank(dictType.getStatus()), SysDictType::getStatus, dictType.getStatus())
                .like(StringUtils.isNotBlank(dictType.getDictType()), SysDictType::getDictType, dictType.getDictType())
                .between(ObjectUtil.isNotEmpty(dictType.getBeginTime()) && ObjectUtil.isNotEmpty(dictType.getEndTime()),
                        SysDictType::getCreateTime, dictType.getBeginTime(), dictType.getEndTime()));
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    public String checkDictTypeUnique(SysDictType dict) {
        boolean exist = sysDictTypeMapper.exists(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dict.getDictType())
                .ne(ObjectUtil.isNotNull(dict.getDictId()), SysDictType::getDictId, dict.getDictId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
//    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#dict.dictType")
    public List<SysDictData> insertDictType(SysDictType dict) {
        int row = sysDictTypeMapper.insert(dict);
        if (row > 0) {
            return new ArrayList<>();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
//    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#dict.dictType")
    @Transactional(rollbackFor = Exception.class)
    public List<SysDictData> updateDictType(SysDictType dict) {
        SysDictType oldDict = sysDictTypeMapper.selectById(dict.getDictId());
        dictDataMapper.update(null, new LambdaUpdateWrapper<SysDictData>()
                .set(SysDictData::getDictType, dict.getDictType())
                .eq(SysDictData::getDictType, oldDict.getDictType()));
        int row = sysDictTypeMapper.updateById(dict);
        if (row > 0) {
            CacheUtils.evict(CacheNames.SYS_DICT, oldDict.getDictType());
            return dictDataMapper.selectDictDataByType(dict.getDictType());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    public SysDictType selectDictTypeById(Long dictId) {
        return sysDictTypeMapper.selectById(dictId);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.exists(new LambdaQueryWrapper<SysDictData>()
                    .eq(SysDictData::getDictType, dictType.getDictType()))) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            CacheUtils.evict(CacheNames.SYS_DICT, dictType.getDictType());
        }
        sysDictTypeMapper.deleteBatchIds(Arrays.asList(dictIds));
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeAll() {
        return sysDictTypeMapper.selectList();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    //@Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (CollUtil.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        return null;
    }

    public String getDictLabel(String dictType, String dictValue, String separator) {
        Map<String, String> map = StreamUtils.toMap(getDictByRedis(dictType), SysDictData::getDictValue, SysDictData::getDictLabel);
        if (StringUtils.containsAny(dictValue, separator)) {
            return Arrays.stream(dictValue.split(separator))
                    .map(v -> map.getOrDefault(v, StringUtils.EMPTY))
                    .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictValue, StringUtils.EMPTY);
        }
    }

    public String getDictValue(String dictType, String dictLabel, String separator) {

        Map<String, String> map = StreamUtils.toMap(getDictByRedis(dictType), SysDictData::getDictLabel, SysDictData::getDictValue);
        if (StringUtils.containsAny(dictLabel, separator)) {
            return Arrays.stream(dictLabel.split(separator))
                    .map(l -> map.getOrDefault(l, StringUtils.EMPTY))
                    .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictLabel, StringUtils.EMPTY);
        }
    }

    /*加载字典数据通过redis,空则查询存储到redis中*/
    public List<SysDictData> getDictByRedis(String key) {
        Object o = CacheUtils.get(CacheNames.SYS_DICT, key);
        if (ObjectUtil.isEmpty(o)) {
            // 优先从本地缓存获取
            List<SysDictData> sysDictData = SpringUtils.getAopProxy(this).selectDictDataByType(key);
            CacheUtils.put(CacheNames.SYS_DICT, key, sysDictData);
            return sysDictData;
        } else {
            return JsonUtils.parseArray(o, SysDictData.class);
        }
    }

    /**
     * 加载字典缓存数据   key:[]
     */
    public void loadingDictCache() {
        List<SysDictData> dictDataList = dictDataMapper.selectList(
                new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getStatus, UserConstants.DICT_NORMAL));
        Map<String, List<SysDictData>> dictDataMap = StreamUtils.groupByKey(dictDataList, SysDictData::getDictType);
        dictDataMap.forEach((k, v) -> {
            List<SysDictData> dictList = StreamUtils.sorted(v, Comparator.comparing(SysDictData::getDictSort));
            CacheUtils.put(CacheNames.SYS_DICT, k, dictList);
        });
    }

    /**
     * 清空字典缓存数据
     */
    public void clearDictCache() {
        CacheUtils.clear(CacheNames.SYS_DICT);
    }

    /**
     * 重置字典缓存数据
     */
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

}
