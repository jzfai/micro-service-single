package top.hugo.system.service;

import cn.dev33.satoken.context.SaHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.constant.CacheConstants;
import top.hugo.common.core.DictService;
import top.hugo.common.spring.SpringUtils;
import top.hugo.common.utils.StreamUtils;
import top.hugo.common.utils.StringUtils;
import top.hugo.system.entity.SysDictData;
import top.hugo.system.mapper.SysDictDataMapper;

import java.util.Arrays;
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
        // 优先从本地缓存获取
        List<SysDictData> datas = (List<SysDictData>) SaHolder.getStorage().get(CacheConstants.SYS_DICT_KEY + dictType);
        if (ObjectUtil.isNull(datas)) {
            datas = SpringUtils.getAopProxy(this).selectDictDataByType(dictType);
            SaHolder.getStorage().set(CacheConstants.SYS_DICT_KEY + dictType, datas);
        }

        Map<String, String> map = StreamUtils.toMap(datas, SysDictData::getDictValue, SysDictData::getDictLabel);
        if (StringUtils.containsAny(dictValue, separator)) {
            return Arrays.stream(dictValue.split(separator))
                    .map(v -> map.getOrDefault(v, StringUtils.EMPTY))
                    .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictValue, StringUtils.EMPTY);
        }
    }

    public String getDictValue(String dictType, String dictLabel, String separator) {
        // 优先从本地缓存获取
        List<SysDictData> datas = (List<SysDictData>) SaHolder.getStorage().get(CacheConstants.SYS_DICT_KEY + dictType);
        if (ObjectUtil.isNull(datas)) {
            datas = SpringUtils.getAopProxy(this).selectDictDataByType(dictType);
            SaHolder.getStorage().set(CacheConstants.SYS_DICT_KEY + dictType, datas);
        }

        Map<String, String> map = StreamUtils.toMap(datas, SysDictData::getDictLabel, SysDictData::getDictValue);
        if (StringUtils.containsAny(dictLabel, separator)) {
            return Arrays.stream(dictLabel.split(separator))
                    .map(l -> map.getOrDefault(l, StringUtils.EMPTY))
                    .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictLabel, StringUtils.EMPTY);
        }
    }
}
