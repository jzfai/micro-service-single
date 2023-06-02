package top.hugo.system.service;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.helper.LoginHelper;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.JsonUtils;
import top.hugo.system.entity.${dbTableConfig.tableNameCase};
import top.hugo.system.entity.SysRole;
import top.hugo.system.mapper.${dbTableConfig.tableNameCase}Mapper;
import top.hugo.system.query.${dbTableConfig.tableNameCase}Query;
import top.hugo.system.vo.${dbTableConfig.tableNameCase}Vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
* 平台信息 服务层处理
*
* @author kuanghua
*/
@RequiredArgsConstructor
@Service
public class ${dbTableConfig.tableNameCase}Service {
private final ${dbTableConfig.tableNameCase}Mapper ${dbTableConfig.tableNameCase}Mapper;
private final SysRoleService sysRoleService;

public TableDataInfo< ${dbTableConfig.tableNameCase}Vo > selectPage${dbTableConfig.tableNameCase}List(${dbTableConfig.tableNameCase}Query ${dbTableConfig.tableName}) {
LambdaQueryWrapper<${dbTableConfig.tableNameCase}> lqw = new LambdaQueryWrapper<${dbTableConfig.tableNameCase}>()
.like(ObjectUtil.isNotEmpty(${dbTableConfig.tableName}.getName()), ${dbTableConfig.tableNameCase}::getName, ${dbTableConfig.tableName}.getName())
.orderByDesc(${dbTableConfig.tableNameCase}::getCreateTime).orderByDesc(${dbTableConfig.tableNameCase}::getUpdateTime);
IPage< ${dbTableConfig.tableNameCase}Vo > page = ${dbTableConfig.tableNameCase}Mapper.selectVoPage(${dbTableConfig.tableName}.build(), lqw);
return TableDataInfo.build(page);
}


/**
* 查询平台信息集合
*
* @param ${dbTableConfig.tableName} 平台信息
* @return 平台信息集合
*/

public List< ${dbTableConfig.tableNameCase}Vo > select${dbTableConfig.tableNameCase}List(${dbTableConfig.tableNameCase}Query ${dbTableConfig.tableName}) {
LambdaQueryWrapper< ${dbTableConfig.tableNameCase} > lqw = new LambdaQueryWrapper< ${dbTableConfig.tableNameCase} >()
.like(ObjectUtil.isNotEmpty(${dbTableConfig.tableName}.getName()), ${dbTableConfig.tableNameCase}::getName, ${dbTableConfig.tableName}.getName());
return ${dbTableConfig.tableNameCase}Mapper.selectVoList(lqw);
}

/**
* 查询所有平台
*
* @return 平台列表
*/
public List< ${dbTableConfig.tableNameCase}Vo > select${dbTableConfig.tableNameCase}All() {
return ${dbTableConfig.tableNameCase}Mapper.selectVoList();
}

/**
* 通过平台ID查询平台信息
*
* @param ${dbTableConfig.tableName}Id 平台ID
* @return 角色对象信息
*/

public ${dbTableConfig.tableNameCase} select${dbTableConfig.tableNameCase}ById(Long ${dbTableConfig.tableName}Id) {
return ${dbTableConfig.tableNameCase}Mapper.selectById(${dbTableConfig.tableName}Id);
}


/**
* 删除平台信息
*
* @param ${dbTableConfig.tableName}Id 平台ID
* @return 结果
*/

public int delete${dbTableConfig.tableNameCase}ById(Long ${dbTableConfig.tableName}Id) {
return ${dbTableConfig.tableNameCase}Mapper.deleteById(${dbTableConfig.tableName}Id);
}

/**
* 批量删除平台信息
*
* @param ${dbTableConfig.tableName}Ids 需要删除的平台ID
* @return 结果
*/
public int delete${dbTableConfig.tableNameCase}ByIds(Long[] ${dbTableConfig.tableName}Ids) {
return ${dbTableConfig.tableNameCase}Mapper.deleteBatchIds(Arrays.asList(${dbTableConfig.tableName}Ids));
}

/**
* 新增保存平台信息
*
* @param ${dbTableConfig.tableName} 平台信息
* @return 结果
*/

public int insert${dbTableConfig.tableNameCase}(${dbTableConfig.tableNameCase} ${dbTableConfig.tableName}) {
return ${dbTableConfig.tableNameCase}Mapper.insert(${dbTableConfig.tableName});
}


/**
* 修改保存平台信息
*
* @param ${dbTableConfig.tableName} 平台信息
* @return 结果
*/

public int update${dbTableConfig.tableNameCase}(${dbTableConfig.tableNameCase} ${dbTableConfig.tableName}) {
return ${dbTableConfig.tableNameCase}Mapper.updateById(${dbTableConfig.tableName});
}

}