package top.hugo.generator.service;


import cn.hutool.core.util.ObjectUtil;
import top.hugo.domain.TableDataInfo;
import top.hugo.generator.entity.DatabaseConfig;
import top.hugo.generator.mapper.DatabaseConfigMapper;
import top.hugo.generator.query.DatabaseConfigQuery;
import top.hugo.generator.vo.DatabaseConfigVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
/**
* 数据库信息 服务层处理
*
* @author kuanghua
*/
@RequiredArgsConstructor
@Service
public class DatabaseConfigService {
private final DatabaseConfigMapper databaseConfigMapper;

public TableDataInfo<DatabaseConfigVo> selectPageDatabaseConfigList(DatabaseConfigQuery databaseConfigQuery) {
  LambdaQueryWrapper<DatabaseConfig> lqw = getQueryWrapper(databaseConfigQuery);
IPage< DatabaseConfigVo > page = databaseConfigMapper.selectVoPage(databaseConfigQuery.build(), lqw);
return TableDataInfo.build(page);
}


/**
* 查询数据库信息集合
*
* @param databaseConfigQuery 数据库信息
* @return 数据库信息集合
*/

public List< DatabaseConfigVo > selectDatabaseConfigList(DatabaseConfigQuery databaseConfigQuery) {
LambdaQueryWrapper< DatabaseConfig> lqw = getQueryWrapper(databaseConfigQuery);
return databaseConfigMapper.selectVoList(lqw);
}


/**
 * 查询wrapper封装
 * @return
 */
private static LambdaQueryWrapper<DatabaseConfig> getQueryWrapper(DatabaseConfigQuery databaseConfigQuery) {
   LambdaQueryWrapper< DatabaseConfig> lqw = new LambdaQueryWrapper<DatabaseConfig>();
    lqw.like(ObjectUtil.isNotEmpty(databaseConfigQuery.getName()), DatabaseConfig::getName, databaseConfigQuery.getName());
    lqw.like(ObjectUtil.isNotEmpty(databaseConfigQuery.getType()), DatabaseConfig::getType, databaseConfigQuery.getType());
  return lqw;
}

/**
* 查询所有平台
*
* @return 平台列表
*/
public List< DatabaseConfigVo > selectDatabaseConfigAll() {
return databaseConfigMapper.selectVoList();
}

/**
* 通过平台ID查询数据库信息
*
* @param databaseConfigId 平台ID
* @return 角色对象信息
*/

public DatabaseConfig selectDatabaseConfigById(Long databaseConfigId) {
return databaseConfigMapper.selectById(databaseConfigId);
}


/**
* 删除数据库信息
*
* @param databaseConfigId 平台ID
* @return 结果
*/

public int deleteDatabaseConfigById(Long databaseConfigId) {
return databaseConfigMapper.deleteById(databaseConfigId);
}

/**
* 批量删除数据库信息
*
* @param databaseConfigIds 需要删除的平台ID
* @return 结果
*/
public int deleteDatabaseConfigByIds(Long[] databaseConfigIds) {
return databaseConfigMapper.deleteBatchIds(Arrays.asList(databaseConfigIds));
}

/**
* 新增保存数据库信息
*
* @param databaseConfig 数据库信息
* @return 结果
*/

public int insertDatabaseConfig(DatabaseConfig databaseConfig) {
return databaseConfigMapper.insert(databaseConfig);
}


/**
* 修改保存数据库信息
*
* @param databaseConfig 数据库信息
* @return 结果
*/

public int updateDatabaseConfig(DatabaseConfig databaseConfig) {
return databaseConfigMapper.updateById(databaseConfig);
}

}
