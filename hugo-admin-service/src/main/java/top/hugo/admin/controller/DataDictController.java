package top.hugo.admin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.admin.entity.DictData;
import top.hugo.admin.service.DataDictService;
import top.hugo.common.domain.R;

import java.util.List;

/**
 * 数据字典相关
 */
@RestController
@RequestMapping("DataDict")
@RequiredArgsConstructor
public class DataDictController {
    private final DataDictService dataDictService;


    /**
     * 获取字典对应的标签
     *
     * @param dictData 字典类型
     * @return
     */
    @GetMapping("selectDictDataList")
    public R<List<DictData>> selectDictDataList(DictData dictData) {
        List<DictData> dataList = dataDictService.selectDictDataList(dictData);
        return R.ok(dataList);
    }

    /**
     * 获取字典对应的标签
     *
     * @param dictType  字典类型
     * @param dictValue 值
     * @return
     */
    @GetMapping("getDictLabel")
    public R<String> getDictLabel(String dictType, String dictValue) {
        String dictLabel = dataDictService.getDictLabel(dictType, dictValue);
        return R.ok(dictLabel);
    }

    /**
     * 获取字典对应的值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return
     */
    @GetMapping("getDictValue")
    public R<String> getDictValue(String dictType, String dictLabel) {
        String value = dataDictService.getDictValue(dictType, dictLabel);
        return R.ok(value);
    }

    /**
     * 重置字典缓存数据
     */
    @GetMapping("reloadDictCache")
    public R<Object> reloadDictCache() {
        dataDictService.reloadDictCache();
        return R.ok();
    }
}
