package ${basicConfig.packageName}.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${basicConfig.packageName}.entity.${dbTableConfig.tableNameCase};
/**
*  ${dbTableConfig.tableDesc}Service
*
* @author ${basicConfig.author}
* @since ${basicConfig.dataTime}
*/
@Service
public class ${dbTableConfig.tableNameCase}Service {

    @Resource
    private ${dbTableConfig.tableNameCase}Mapper ${dbTableConfig.tableName}Mapper;

      /*
     * 查询列表分页
     * */
    public Page< ${dbTableConfig.tableNameCase} > selectPage(Integer pageNum, Integer pageSize, QueryWrapper< ${dbTableConfig.tableNameCase} > queryWrapper) {
    return this.${dbTableConfig.tableName}Mapper.selectPage(new Page< ${dbTableConfig.tableNameCase} >(pageNum, pageSize), queryWrapper);
    }

     /*
     * 根据id查询明细
     * */
    public ${dbTableConfig.tableNameCase} selectById(${dbTableConfig.uniKeyType} id) {
    return this.${dbTableConfig.tableName}Mapper.selectById(id);
    }

    /*
     * 根据批量id查询列表
     * */
    public List< ${dbTableConfig.tableNameCase} > selectBatchIds(List< ${dbTableConfig.uniKeyType} > idList) {
    return this.${dbTableConfig.tableName}Mapper.selectBatchIds(idList);
    }

      /*
     * 新增
     * */
    public int insert(${dbTableConfig.tableNameCase} ${dbTableConfig.tableName}) {
      this.${dbTableConfig.tableName}Mapper.insert(${dbTableConfig.tableName}); 
      return ${dbTableConfig.tableName}.getId();
    }
     /*
     * 根据id主键更新
     * */
    public int updateById(${dbTableConfig.tableNameCase} ${dbTableConfig.tableName}) {
    return this.${dbTableConfig.tableName}Mapper.updateById(${dbTableConfig.tableName});
    }

    /*
     * 单个删除
     * */
    public int deleteById(${dbTableConfig.uniKeyType} id) {
    return this.${dbTableConfig.tableName}Mapper.deleteById(id);
    }

    /*
    * 批量删除
    * */
    public int deleteBatchIds(List< ${dbTableConfig.uniKeyType} > idList) {
    return this.${dbTableConfig.tableName}Mapper.deleteBatchIds(idList);
    }
}
