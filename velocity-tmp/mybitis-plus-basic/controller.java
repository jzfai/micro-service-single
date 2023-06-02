#parse("utils.vm")
package ${basicConfig.packageName}.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import ${basicConfig.packageName}.entity.${dbTableConfig.tableNameCase};
/**
*  ${dbTableConfig.tableDesc}Controller
*
* @author ${basicConfig.author}
* @since ${basicConfig.dataTime}
*/
@Tag(tags = "${dbTableConfig.tableDesc}(${dbTableConfig.tableNameCase})")
@RestController
@RequestMapping("${dbTableConfig.tableName}")
@Validated
public class ${dbTableConfig.tableNameCase}Controller {

@Resource
private ${dbTableConfig.tableNameCase}Service ${dbTableConfig.tableName}Service;

/**
* 分页查询所有数据
*
##* @param ${dbTableConfig.tableName} 查询实体
* @return ResResult
*/
@GetMapping("selectPage")
@Operation(value = "分页查询所有数据")

public ResResult< List< ${dbTableConfig.tableNameCase} > > selectPage(@Validated ${dbTableConfig.tableNameCase}Query ${dbTableConfig.tableName}Query,SelfCommonParams commonParams) {
QueryWrapper <${dbTableConfig.tableNameCase}> queryWrapper = new QueryWrapper<>();
#foreach($item in $queryConfig)
    #if(${item.originField}=="create_time")
    //${item.desc}
    queryWrapper.orderByDesc("$item.originField");
    if (ObjSelfUtils.isNotEmpty(commonParams.getStartTime())) {
    queryWrapper.between("create_time", commonParams.getStartTime(), commonParams.getEndTime());
    }
    #else
    //${item.desc}
    if (ObjSelfUtils.isNotEmpty(${dbTableConfig.tableName}Query.get${item.fieldCase}())) {
    queryWrapper.like("${item.originField}", ${dbTableConfig.tableName}Query.get${item.fieldCase}());
    }
    #end
#end

queryWrapper.select("$selectString");

Page <${dbTableConfig.tableNameCase}> ${dbTableConfig.tableName}Page = this.${dbTableConfig.tableName}Service.selectPage(commonParams.getPageNum(), commonParams.getPageSize(),queryWrapper);
return new ResResult().success(${dbTableConfig.tableName}Page);
}

/**
* 通过主键查询单条数据
*
* @param id 主键
* @return 单条数据
*/
@GetMapping("selectById")
@Operation(value = "通过id主键查询单条数据")
public ResResult<${dbTableConfig.tableNameCase}> selectById(@RequestParam("id") ${dbTableConfig.uniKeyType} id) {
return new ResResult().success(this.${dbTableConfig.tableName}Service.selectById(id));
}

/**
* @Description: 根据id数组查询列表
* @Param: idList id数组
* @return: ids列表数据
*/
@Operation(value = "根据id数组查询列表")
@PostMapping("selectBatchIds")
public ResResult< List<${dbTableConfig.tableNameCase}> > selectBatchIds(@RequestParam("idList") List< ${dbTableConfig.uniKeyType}  > idList) {
return new ResResult().success(this.${dbTableConfig.tableName}Service.selectBatchIds(idList));
}


/**
* 新增数据
*
* @param ${dbTableConfig.tableName} 实体对象
* @return 新增结果
*/
@Operation(value = "新增数据")
@PostMapping("insert")
public ResResult insert(@Validated @RequestBody ${dbTableConfig.tableNameCase} ${dbTableConfig.tableName}) {
return new ResResult().success(this.${dbTableConfig.tableName}Service.insert(${dbTableConfig.tableName}));
}

/**
* 修改数据
*
* @param ${dbTableConfig.tableName} 实体对象
* @return 修改结果
*/
@Operation(value = "根据id修改数据")
@PutMapping("updateById")
public ResResult updateById(@Validated @RequestBody ${dbTableConfig.tableNameCase} ${dbTableConfig.tableName}) {
return new ResResult().success(this.${dbTableConfig.tableName}Service.updateById(${dbTableConfig.tableName}));
}

/**
* 删除数据
*
* @param idList 主键结合
* @return 删除结果
*/
@Operation(value = "根据id数组删除数据")
@DeleteMapping("deleteBatchIds")
public ResResult deleteBatchIds(@RequestBody List< ${dbTableConfig.uniKeyType} > idList) {
return new ResResult().success(this.${dbTableConfig.tableName}Service.deleteBatchIds(idList));
}

@Operation("根据id删除数据")
@DeleteMapping("deleteById")
public ResResult deleteById(@RequestParam("id") ${dbTableConfig.uniKeyType} id) {
return new ResResult().success(this.${dbTableConfig.tableName}Service.deleteById(id));
}
}
