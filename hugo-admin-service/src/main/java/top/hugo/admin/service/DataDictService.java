package top.hugo.admin.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.admin.entity.DictData;
import top.hugo.admin.mapper.DictDataMapper;
import top.hugo.common.utils.JacksonUtils;
import top.hugo.redis.utils.RedisUtils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据字典服务
 */
@Service
@RequiredArgsConstructor
public class DataDictService {

    private final DictDataMapper dictDataMapper;

    /**
     * 加载字典数据到redis中
     */
    public void loadingDictCache() {
        //先清空原有的redis缓存
        clearDictCache();
        LambdaQueryWrapper<DictData> qw = new LambdaQueryWrapper<>();
        qw.eq(DictData::getStatus, "0");
        List<DictData> dataList = dictDataMapper.selectList(qw);
        Map<String, List<DictData>> dictDataMap = dataList.stream().collect(Collectors.groupingBy(DictData::getDictType, LinkedHashMap::new, Collectors.toList()));
        dictDataMap.forEach((k, v) -> {
            List<DictData> dictList = v.stream().sorted(Comparator.comparing(DictData::getDictSort)).collect(Collectors.toList());
            RedisUtils.setCacheMapValue("dict", k, dictList);
        });
    }

    /**
     * 获取字典对应的标签
     */
    public String getDictLabel(String dictType, String dictValue) {
        List<DictData> dictDataList = getDictByRedis(dictType);
        Map<String, String> map = dictDataList.stream().collect(Collectors.toMap(DictData::getDictValue, DictData::getDictLabel, (l, r) -> l));
        return map.get(dictValue);
    }

    /**
     * 获取字典对应的值
     */
    public String getDictValue(String dictType, String dictLabel) {
        List<DictData> dictDataList = getDictByRedis(dictType);
        Map<String, String> map = dictDataList.stream().collect(Collectors.toMap(DictData::getDictLabel, DictData::getDictValue, (l, r) -> l));
        return map.get(dictLabel);
    }

    /*加载字典数据通过redis,空则查询存储到redis中*/
    public List<DictData> getDictByRedis(String key) {
        Object o = RedisUtils.getCacheMapValue("dict", key);
        if (ObjectUtil.isEmpty(o)) {
            throw new RuntimeException("redis中数据字典数据为空" + key);
        } else {
            return JacksonUtils.parseArray(o, DictData.class);
        }
    }

    /**
     * 清空字典缓存数据
     */
    public void clearDictCache() {
        RedisUtils.deleteObject("dict");
    }

    /**
     * 重新加载字典缓存数据
     */
    public void reloadDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 根据条件查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */

    public List<DictData> selectDictDataList(DictData dictData) {
        return dictDataMapper.selectList(new LambdaQueryWrapper<DictData>()
                .eq(ObjectUtil.isNotEmpty(dictData.getDictType()), DictData::getDictType, dictData.getDictType())
                .like(ObjectUtil.isNotEmpty(dictData.getDictLabel()), DictData::getDictLabel, dictData.getDictLabel())
                .eq(ObjectUtil.isNotEmpty(dictData.getStatus()), DictData::getStatus, dictData.getStatus())
                .orderByAsc(DictData::getDictSort));
    }

}