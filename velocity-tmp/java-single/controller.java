package top.hugo.system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.annotation.Log;
import top.hugo.common.controller.BaseController;
import top.hugo.common.domain.R;
import top.hugo.common.enums.BusinessType;
import top.hugo.common.excel.ExcelUtil;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.system.dto.${dbTableConfig.tableNameCase}Dto;
import top.hugo.system.entity.${dbTableConfig.tableNameCase};
import top.hugo.system.query.${dbTableConfig.tableNameCase}Query;
import top.hugo.system.service.${dbTableConfig.tableNameCase}Service;
import top.hugo.system.vo.${dbTableConfig.tableNameCase}Vo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* ${dbTableConfig.tableName}}信息操作处理
*
* @author kuanghua
*/
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/platform")
public class ${dbTableConfig.tableNameCase}Controller extends BaseController {
private final ${dbTableConfig.tableNameCase}Service platformService;

/**
* 获取${dbTableConfig.tableName}}列表
*
* @return
*/
//@SaCheckPermission("system:platform:list")
@PostMapping("/list")
public TableDataInfo< ${dbTableConfig.tableNameCase}Vo> list(@RequestBody @Validated ${dbTableConfig.tableNameCase}Query platform) {
return platformService.selectPagePlatformList(platform);
}

/**
* 导出${dbTableConfig.tableName}}列表
*/
@Log(title = "${dbTableConfig.tableName}}管理", businessType = BusinessType.EXPORT)
@PostMapping("/export")
public void export(${dbTableConfig.tableNameCase}Query platform, HttpServletResponse response) {
List< ${dbTableConfig.tableNameCase}Vo> list = platformService.selectPlatformList(platform);
ExcelUtil.exportExcel(list, "${dbTableConfig.tableName}}数据", ${dbTableConfig.tableNameCase}Vo.class, response);
}

/**
* 根据${dbTableConfig.tableName}}编号获取详细信息
*
* @param platformId ${dbTableConfig.tableName}}ID
*/
//@SaCheckPermission("system:platform:query")
@GetMapping(value = "/{platformId}")
public R< ${dbTableConfig.tableNameCase}> getInfo(@PathVariable Long platformId) {
return R.ok(platformService.selectPlatformById(platformId));
}

/**
* 新增${dbTableConfig.tableName}}
*/
//@SaCheckPermission("system:platform:add")
@Log(title = "${dbTableConfig.tableName}}管理", businessType = BusinessType.INSERT)
@PostMapping
public R< Void> add(@Validated @RequestBody ${dbTableConfig.tableNameCase}Dto platformDto) {
${dbTableConfig.tableNameCase} platform = BeanCopyUtils.copy(platformDto, ${dbTableConfig.tableNameCase}.class);
return toAjax(platformService.insertPlatform(platform));
}

/**
* 修改${dbTableConfig.tableName}} v
*/
//@SaCheckPermission("system:platform:edit")
@Log(title = "${dbTableConfig.tableName}}管理", businessType = BusinessType.UPDATE)
@PutMapping
public R< Void > edit(@Validated @RequestBody ${dbTableConfig.tableNameCase}Dto platformDto) {
${dbTableConfig.tableNameCase} platform = BeanCopyUtils.copy(platformDto, ${dbTableConfig.tableNameCase}.class);
return toAjax(platformService.updatePlatform(platform));
}

/**
* 删除${dbTableConfig.tableName}}
*
* @param platformIds ${dbTableConfig.tableName}}ID串
*/
//@SaCheckPermission("system:platform:remove")
@Log(title = "${dbTableConfig.tableName}}管理", businessType = BusinessType.DELETE)
@DeleteMapping("/{platformIds}")
public R< Void > remove(@PathVariable Long[] platformIds) {
return toAjax(platformService.deletePlatformByIds(platformIds));
}

/**
* 获取${dbTableConfig.tableName}}选择框列表
*/
@GetMapping("/selectPlatformAll")
public R< List< ${dbTableConfig.tableNameCase}Vo>> selectPlatformAll() {
List< ${dbTableConfig.tableNameCase}Vo> platforms = platformService.selectPlatformAll();
return R.ok(platforms);
}

/**
* 获取${dbTableConfig.tableName}}选择框列表
*
* @return
*/
@GetMapping("/filterPlatformByUserId")
public R< List< ${dbTableConfig.tableNameCase}Vo>> filterPlatformByUserId() {
List< ${dbTableConfig.tableNameCase}Vo > ${dbTableConfig.tableName}Vos = platformService.filterPlatformByUserId();
return R.ok(${dbTableConfig.tableName}Vos);
}

}