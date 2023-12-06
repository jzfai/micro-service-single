## springboot集成数据字典

数据字典或枚举通过统一管理，减少冗余代码，增加开发效率



## 数据库表

dict_type


```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type`  (
  `dict_id` bigint(20) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;
```



dict_data

```sql
DROP TABLE IF EXISTS `dict_data`;
CREATE TABLE `dict_data`  (
  `dict_code` bigint(20) NOT NULL COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;
```



## 代码生成

低代码平台，生成    dict_type, dict_data   表的增删改查代码



字典相关service

DataDictService


```java
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
```



vo 中增加 @ExcelDictFormat(dictType = "sys_normal_disable")

```java
  @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
  @ExcelDictFormat(dictType = "sys_normal_disable")
  private String status;
```



@ExcelDictFormat注解

```java
package top.hugo.admin.annotation;

import java.lang.annotation.*;

/**
 * 字典格式化
 *
 * @author kuanghua
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelDictFormat {

    /**
     * 如果是字典类型，请设置字典的type值 (如: sys_user_sex)
     */
    String dictType() default "";
}

```



excel转换 ExcelDictConvert

```java
package top.hugo.admin.excel;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;
import top.hugo.admin.annotation.ExcelDictFormat;
import top.hugo.admin.service.TypeDataService;

@Slf4j
public class ExcelDictConvert implements Converter<Object> {
    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //获取 ExcelDictFormat 注解信息
        ExcelDictFormat anno = AnnotationUtil.getAnnotation(contentProperty.getField(), ExcelDictFormat.class);
        //调用TypeDataService.getDictLabel()转换label
        String dictValue = SpringUtil.getBean(TypeDataService.class).getDictValue(anno.dictType(), cellData.getStringValue());
        return Convert.convert(contentProperty.getField().getType(), dictValue);
    }

    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (ObjectUtil.isNull(value)) {
            return new WriteCellData<>("");
        }
        //获取 ExcelDictFormat 注解信息
        ExcelDictFormat anno = AnnotationUtil.getAnnotation(contentProperty.getField(), ExcelDictFormat.class);
        //调用TypeDataService.getDictLabel()转换label
        String dictLabel = SpringUtil.getBean(TypeDataService.class).getDictLabel(anno.dictType(), Convert.toStr(value));
        return new WriteCellData<>(dictLabel);
    }
}
```

>转换处理字典枚举



初始化加载 字典数据进内存

```java
package top.hugo.admin.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.hugo.admin.service.DataDictService;

@Slf4j
@RequiredArgsConstructor
@Component
public class TypeDataToRedisRunner implements ApplicationRunner {
    private final DataDictService dataDictService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        dataDictService.loadingDictCache();
        log.info("加载字典缓存数据成功");
    }
}
```



controller

DataDictController

```java
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

```





vue3中如果需要使用  顶层 async

```vue
  <Suspense>
    <!-- 在 #fallback 插槽中显示 “正在加载中” -->
    <template #fallback>
      Loading...
    </template>
  </Suspense>
```



## 前端部分

use-data-dict.ts

```vue
import useDictStore from "@/store/dict";
import axiosReq from "@/utils/axios-req";

const dictDataReq=(dictType)=>{
  return axiosReq({
    url: '/dataDict/selectDictDataList',
    method: 'get',
    reqLoading: false,
    params: {dictType}
  })
}
//字典枚举，根据value返回选中的项
export const getItem= async (dictType,value,key) =>{
  if(getDict(dictType).length){
    const itemObj = getDict(dictType).find((item:any)=>{
      return item.dictValue==value
    })
    if(key&&itemObj?.key){
      return itemObj[key]
    }else{
      return  itemObj
    }
  }else{
    const {data}=await dictDataReq(dictType)
    const itemObj = data.find((item:any)=>{
      return item.dictValue==value
    })
    if(key&&itemObj?.key){
      return itemObj[key]
    }else{
      return  itemObj
    }
  }
}
export const getLabel=(dictType,value)=>{
  return getItem(dictType,value,"dictLabel")
}

export const getValue=(dictType,value)=>{
  return getItem(dictType,value,"dictValue")
}

//如果 store中有值取 store，没有请求设置 store
const {getDict,setDict} = useDictStore();
export const useDict=(dictTypes)=>{
  const dictTypeMap:any=reactive({})
  dictTypes?.forEach((item)=>{
    dictTypeMap[item]=[]
    if(getDict(item).length){
      dictTypeMap[item]=getDict(item)
    }else{
      dictDataReq(item).then(resp=>{
        const dillData=resp.data.map((p)=>{
          p.label= p.dictLabel
          p.value= p.dictValue
          p.elTagType= p.listClass
          p.elTagClass= p.cssClass
          return p;
        })
        dictTypeMap[item]=dillData;
        setDict(item,dillData)
      })
    }
  })
  return toRefs(dictTypeMap)
}
```



数据字典store

dict

```js
/*数据字典*/
import { defineStore } from 'pinia'
const useDictStore = defineStore('dict', {
  state: () => ({
    dict: {}
  }),
  actions: {
    // 获取字典
    getDict(_key) {
     return this.dict[_key]||[]
    },
    // 设置字典
    setDict(_key, value) {
      this.dict[_key]=value
    },
    // 删除字典
    deleteDict(_key) {
      this.dict[_key]=[]
    },
    // 清空字典
    cleanDict() {
      this.dict ={}
    },
  },
  // persist: {
  //   storage: localStorage,
  //   paths: ['dict']
  // },
})
export default useDictStore
```

如果使用

```vue
import { useDict } from '@/hooks/use-data-dict'
// eslint-disable-next-line camelcase
const { sys_normal_disable } = useDict(['sys_normal_disable'])
```

