package top.hugo.generator.controller;


import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.generator.dto.DatabaseConfigDto;
import top.hugo.generator.entity.DatabaseConfig;
import top.hugo.generator.query.DatabaseConfigQuery;
import top.hugo.generator.service.DatabaseConfigService;
import top.hugo.generator.vo.DatabaseConfigVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据库信息信息操作处理
 *
 * @author kuanghua
 * @since 2024-06-13 14:20:53
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/databaseConfig")
public class DatabaseConfigController {
    private final DatabaseConfigService databaseConfigService;

    /**
     * 获取databaseConfig列表
     *
     * @return
     */
    //@SaCheckPermission("system:databaseConfig:list")
    @PostMapping("/listPage")
    public TableDataInfo<DatabaseConfigVo> list(@RequestBody @Validated DatabaseConfigQuery databaseConfig) {
        return databaseConfigService.selectPageDatabaseConfigList(databaseConfig);
    }



    /**
     * 导出databaseConfig列表
     */
    //@Log(title = "databaseConfig管理", businessType = BusinessType.EXPORT)
    //@PostMapping("/export")
    //public void export(DatabaseConfigQuery databaseConfig, HttpServletResponse response) {
    //List< DatabaseConfigVo> list = databaseConfigService.selectDatabaseConfigList(databaseConfig);
    //EasyExcelUtils.exportExcel(list, "databaseConfig数据", DatabaseConfigVo.class, response);
    //}

    /**
     * 根据databaseConfig编号获取详细信息
     *
     * @param id
     */

    @GetMapping(value = "/{id}")
    public R<DatabaseConfig> getInfo(@PathVariable Long id) {
        return R.ok(databaseConfigService.selectDatabaseConfigById(id));
    }

    /**
     * 新增databaseConfig
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody DatabaseConfigDto databaseConfigDto) {
        DatabaseConfig databaseConfig = BeanCopyUtils.copy(databaseConfigDto, DatabaseConfig.class);
        return R.result(databaseConfigService.insertDatabaseConfig(databaseConfig));
    }

    /**
     * 修改databaseConfig
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody DatabaseConfigDto databaseConfigDto) {
        DatabaseConfig databaseConfig = BeanCopyUtils.copy(databaseConfigDto, DatabaseConfig.class);
        return R.result(databaseConfigService.updateDatabaseConfig(databaseConfig));
    }

    /**
     * 批量删除databaseConfig
     * @param ids databaseConfigID串
     */
    //@Log(title = "databaseConfig管理", businessType = BusinessType.DELETE)
    @DeleteMapping("deleteByIds")
    public R<Void> removeBatch(@RequestBody Long[] ids) {
        return R.result(databaseConfigService.deleteDatabaseConfigByIds(ids));
    }

    /**
     * 单个删除databaseConfig
     *
     * @param id
     */
    @DeleteMapping("deleteById")
    public R<Void> removeId(@RequestParam("id") Long id) {
        return R.result(databaseConfigService.deleteDatabaseConfigById(id));
    }

    /**
     * 获取databaseConfig选择框列表
     */
    @GetMapping("/selectDatabaseConfigAll")
    public R<List<DatabaseConfigVo>> selectDatabaseConfigAll() {
        List<DatabaseConfigVo> databaseConfigs = databaseConfigService.selectDatabaseConfigAll();
        return R.ok(databaseConfigs);
    }
}
