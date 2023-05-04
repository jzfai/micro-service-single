package top.hugo.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.constant.CacheNames;
import top.hugo.common.constant.UserConstants;
import top.hugo.common.core.DictService;
import top.hugo.common.spring.SpringUtils;
import top.hugo.common.utils.JsonUtils;
import top.hugo.common.utils.StreamUtils;
import top.hugo.common.utils.StringUtils;
import top.hugo.common.utils.redis.CacheUtils;
import top.hugo.system.entity.SysDictData;
import top.hugo.system.mapper.SysDictDataMapper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class SysDictTypeService implements DictService {
    private final SysDictDataMapper sysDictDataMapper;

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    //@Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = sysDictDataMapper.selectDictDataByType(dictType);
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
        List<SysDictData> dictDataList = sysDictDataMapper.selectList(
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
