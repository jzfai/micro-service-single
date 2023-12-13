package top.hugo.generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;
import top.hugo.generator.dto.ConfigSaveDto;
import top.hugo.generator.entity.ConfigSave;
import top.hugo.generator.query.ConfigSaveQuery;
import top.hugo.generator.service.ConfigSaveService;
import top.hugo.generator.vo.ConfigSaveVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 代码生成配置保存
 *
 * @author kuanghua
 * @since 2023-10-18 11:42:26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/generator/configSave")
public class ConfigSaveController {
    private final ConfigSaveService configSaveService;

    /**
     * 获取configSave列表(分页)
     *
     * @return
     */
    @GetMapping("/listPage")
    public TableDataInfo<ConfigSaveVo> list(@Validated ConfigSaveQuery configSave) {
        TableDataInfo<ConfigSaveVo> list = configSaveService.selectPageConfigSaveList(configSave);
        return list;
    }

    /**
     * 导出configSave列表
     */
    @GetMapping("/export")
    public void export(@Validated ConfigSaveQuery configSave, HttpServletResponse response) {
        List<ConfigSaveVo> list = configSaveService.selectConfigSaveList(configSave);
        EasyExcelUtils.exportExcel(list, "configSave数据", ConfigSaveVo.class, response);
    }

    /**
     * 获取详细信息
     *
     * @param configSaveId configSaveID
     */
    //@SaCheckPermission("system:configSave:query")
    @GetMapping(value = "/{configSaveId}")
    public R<ConfigSaveVo> getInfo(@PathVariable Long configSaveId) {
        return R.ok(configSaveService.selectConfigSaveById(configSaveId));
    }

    /**
     * 新增configSave
     */
    //@SaCheckPermission("system:configSave:add")
    //@Log(title = "configSave管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody ConfigSaveDto configSaveDto) {
        ConfigSave configSave = BeanCopyUtils.copy(configSaveDto, ConfigSave.class);
        return R.result(configSaveService.insertConfigSave(configSave));
    }

    /**
     * 修改configSave
     */
    //@SaCheckPermission("system:configSave:edit")
    //@Log(title = "configSave管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody ConfigSaveDto configSaveDto) {
        ConfigSave configSave = BeanCopyUtils.copy(configSaveDto, ConfigSave.class);
        return R.result(configSaveService.updateConfigSave(configSave));
    }


    @DeleteMapping("/{configSaveIds}")
    public R<Void> remove(@PathVariable Long configSaveIds) {
        return R.result(configSaveService.deleteConfigSaveById(configSaveIds));
    }


    /**
     * 删除configSave
     *
     * @param configSaveIds configSaveID串
     */
    //@SaCheckPermission("system:configSave:remove")
    //@Log(title = "configSave管理", businessType = BusinessType.DELETE)
    @DeleteMapping("deleteBatchIds")
    public R<Void> remove(@RequestBody Long[] configSaveIds) {
        return R.result(configSaveService.deleteConfigSaveByIds(configSaveIds));
    }

    /**
     * 获取configSave列表(所有)
     */
    @GetMapping("/selectConfigSaveList")
    public R<List<ConfigSaveVo>> selectConfigSaveList(@Validated ConfigSaveQuery configSave) {
        List<ConfigSaveVo> configSaveList = configSaveService.selectConfigSaveList(configSave);
        return R.ok(configSaveList);
    }
}
