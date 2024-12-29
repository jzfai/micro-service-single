package top.hugo.admin.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import top.hugo.admin.dto.DslConfigDto;
import top.hugo.admin.entity.DslConfig;
import top.hugo.admin.query.DslConfigQuery;
import top.hugo.admin.service.DslConfigService;
import top.hugo.admin.vo.DslConfigVo;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * dsl配置
 *
 * @author kuanghua
 * @since 2023-10-17 10:42:10
 */
@Validated
@RequiredArgsConstructor
@RestController
@SaIgnore
@RequestMapping("/system/dslConfig")
public class DslConfigController {
    private final DslConfigService dslConfigService;

    /**
     * 获取dslConfig列表(分页)
     *
     * @return
     */
    @GetMapping("/listPage")
    public TableDataInfo<DslConfigVo> list(@Validated DslConfigQuery dslConfig) {
        TableDataInfo<DslConfigVo> list = dslConfigService.selectPageDslConfigList(dslConfig);
        return list;
    }

    /**
     * 导出dslConfig列表
     */
    @GetMapping("/export")
    public void export(@Validated DslConfigQuery dslConfig, HttpServletResponse response) {
        List<DslConfigVo> list = dslConfigService.selectDslConfigList(dslConfig);
        EasyExcelUtils.exportExcel(list, "dslConfig数据", DslConfigVo.class, response);
    }

    /**
     * 获取详细信息
     *
     * @param dslConfigId dslConfigID
     */
//@SaCheckPermission("system:dslConfig:query")
    @GetMapping(value = "/{dslConfigId}")
    public R<DslConfigVo> getInfo(@PathVariable Long dslConfigId) {
        return R.ok(dslConfigService.selectDslConfigById(dslConfigId));
    }

    /**
     * 新增dslConfig
     */
  //@SaCheckPermission("system:dslConfig:add")
  //@Log(title = "dslConfig管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody DslConfigDto dslConfigDto) {
        DslConfig dslConfig = BeanCopyUtils.copy(dslConfigDto, DslConfig.class);
        return R.result(dslConfigService.insertDslConfig(dslConfig));
    }

    /**
     * 修改dslConfig
     */
    //@SaCheckPermission("system:dslConfig:edit")
    //@Log(title = "dslConfig管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody DslConfigDto dslConfigDto) {
        DslConfig dslConfig = BeanCopyUtils.copy(dslConfigDto, DslConfig.class);
        return R.result(dslConfigService.updateDslConfig(dslConfig));
    }

    /**
     * 删除dslConfig
     *
     * @param dslConfigIds dslConfigID串
     */
    //@SaCheckPermission("system:dslConfig:remove")
    //@Log(title = "dslConfig管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dslConfigIds}")
    public R<Void> remove(@PathVariable Long[] dslConfigIds) {
        return R.result(dslConfigService.deleteDslConfigByIds(dslConfigIds));
    }
    /**
     * 获取dslConfig列表(所有)
     */
    @GetMapping("/selectDslConfigList")
    public R<List<DslConfigVo>> selectDslConfigList(@Validated DslConfigQuery dslConfig) {
        List<DslConfigVo> dslConfigList = dslConfigService.selectDslConfigList(dslConfig);
        return R.ok(dslConfigList);
    }
}
