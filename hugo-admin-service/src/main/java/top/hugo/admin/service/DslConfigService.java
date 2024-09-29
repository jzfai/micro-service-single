package top.hugo.admin.service;

import cn.hutool.core.util.ObjectUtil;
import top.hugo.admin.entity.DslConfig;
import top.hugo.admin.mapper.DslConfigMapper;
import top.hugo.admin.query.DslConfigQuery;
import top.hugo.admin.vo.DslConfigVo;
import top.hugo.domain.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
* dsl配置
*
* @author kuanghua
*/
@RequiredArgsConstructor
@Service
public class DslConfigService {
private final DslConfigMapper dslConfigMapper;

public TableDataInfo<DslConfigVo> selectPageDslConfigList(DslConfigQuery dslConfigQuery) {
  LambdaQueryWrapper<DslConfig> lqw = getQueryWrapper(dslConfigQuery);
  IPage<DslConfigVo> page = dslConfigMapper.selectVoPage(dslConfigQuery.build(), lqw);
  return TableDataInfo.build(page);
}


/**
* 查询dsl配置分页
*
* @return 
*/

public List<DslConfigVo> selectDslConfigList(DslConfigQuery dslConfigQuery) {
   LambdaQueryWrapper<DslConfig> lqw = getQueryWrapper(dslConfigQuery);
  return dslConfigMapper.selectVoList(lqw);
}


/*查询条件抽取*/
private static LambdaQueryWrapper<DslConfig> getQueryWrapper(DslConfigQuery dslConfigQuery) {
   LambdaQueryWrapper<DslConfig> lqw = new LambdaQueryWrapper<>();
    lqw.like(ObjectUtil.isNotEmpty(dslConfigQuery.getDictName()), DslConfig::getDictName, dslConfigQuery.getDictName());
    lqw.like(ObjectUtil.isNotEmpty(dslConfigQuery.getPlatName()), DslConfig::getPlatName, dslConfigQuery.getPlatName());
    lqw.orderByDesc(DslConfig::getCreateTime);
  return lqw;
}

/**
* 查询所有平台
*
* @return 平台列表
*/
public List<DslConfigVo> selectDslConfigAll() {
return dslConfigMapper.selectVoList();
}

/**
* 通过dsl配置ID查询
*
* @param dslConfigId dsl配置ID
* @return 角色对象信息
*/

public DslConfigVo selectDslConfigById(Long dslConfigId) {
return dslConfigMapper.selectVoById(dslConfigId);
}


/**
* 删除dsl配置
*
* @param dslConfigId 平台ID
* @return 结果
*/

public int deleteDslConfigById(Long dslConfigId) {
return dslConfigMapper.deleteById(dslConfigId);
}

/**
* 批量删除dsl配置
*
* @param dslConfigIds 需要删除的平台ID
* @return 结果
*/
public int deleteDslConfigByIds(Long[] dslConfigIds) {
return dslConfigMapper.deleteBatchIds(Arrays.asList(dslConfigIds));
}

/**
* 新增保存dsl配置
*
* @param dslConfig 平台信息
* @return 结果
*/

public int insertDslConfig(DslConfig dslConfig) {
return dslConfigMapper.insert(dslConfig);
}


/**
* 修改保存dsl配置
*
* @param dslConfig
* @return 结果
*/

public int updateDslConfig(DslConfig dslConfig) {
return dslConfigMapper.updateById(dslConfig);
}

}