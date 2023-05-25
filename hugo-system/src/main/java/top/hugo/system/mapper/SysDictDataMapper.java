package top.hugo.system.mapper;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import top.hugo.common.constant.UserConstants;
import top.hugo.common.mapper.BaseMapperPlus;
import top.hugo.system.entity.SysDictData;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author kuanghua
 */
public interface SysDictDataMapper extends BaseMapperPlus<SysDictDataMapper, SysDictData, SysDictData> {

    default List<SysDictData> selectDictDataByType(String dictType) {
        return selectList(
                new LambdaQueryWrapper<SysDictData>()
                        .eq(SysDictData::getStatus, UserConstants.DICT_NORMAL)
                        .eq(SysDictData::getDictType, dictType)
                        .orderByAsc(SysDictData::getDictSort));
    }
}
