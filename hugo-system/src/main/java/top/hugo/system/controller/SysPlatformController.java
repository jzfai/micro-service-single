package top.hugo.system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.annotation.Log;
import top.hugo.common.controller.BaseController;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.domain.R;
import top.hugo.common.enums.BusinessType;
import top.hugo.common.excel.ExcelUtil;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.system.dto.SysPlatformDto;
import top.hugo.system.entity.SysPlatform;
import top.hugo.system.query.SysPlatformQuery;
import top.hugo.system.service.SysPlatformService;
import top.hugo.system.vo.SysPlatformVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 平台信息操作处理
 *
 * @author kuanghua
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/platform")
public class SysPlatformController extends BaseController {

    private final SysPlatformService platformService;

    /**
     * 获取平台列表
     */
    //@SaCheckPermission("system:platform:list")
    @GetMapping("/list")
    public TableDataInfo<SysPlatformVo> list(SysPlatformQuery platform, PageQuery pageQuery) {
        return platformService.selectPagePlatformList(platform, pageQuery);
    }

    /**
     * 导出平台列表
     */
    @Log(title = "平台管理", businessType = BusinessType.EXPORT)
    //@SaCheckPermission("system:platform:export")
    @PostMapping("/export")
    public void export(SysPlatformQuery platform, HttpServletResponse response) {
        List<SysPlatformVo> list = platformService.selectPlatformList(platform);
        ExcelUtil.exportExcel(list, "平台数据", SysPlatformVo.class, response);
    }

    /**
     * 根据平台编号获取详细信息
     *
     * @param platformId 平台ID
     */
    //@SaCheckPermission("system:platform:query")
    @GetMapping(value = "/{platformId}")
    public R<SysPlatform> getInfo(@PathVariable Long platformId) {
        return R.ok(platformService.selectPlatformById(platformId));
    }

    /**
     * 新增平台
     */
    //@SaCheckPermission("system:platform:add")
    @Log(title = "平台管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysPlatformDto platformDto) {
        SysPlatform platform = BeanCopyUtils.copy(platformDto, SysPlatform.class);
        return toAjax(platformService.insertPlatform(platform));
    }

    /**
     * 修改平台
     */
    //@SaCheckPermission("system:platform:edit")
    @Log(title = "平台管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysPlatformDto platformDto) {
        SysPlatform platform = BeanCopyUtils.copy(platformDto, SysPlatform.class);
        return toAjax(platformService.updatePlatform(platform));
    }

    /**
     * 删除平台
     *
     * @param platformIds 平台ID串
     */
    //@SaCheckPermission("system:platform:remove")
    @Log(title = "平台管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{platformIds}")
    public R<Void> remove(@PathVariable Long[] platformIds) {
        return toAjax(platformService.deletePlatformByIds(platformIds));
    }

    /**
     * 获取平台选择框列表
     */
    @GetMapping("/selectPlatformAll")
    public R<List<SysPlatformVo>> selectPlatformAll() {
        List<SysPlatformVo> platforms = platformService.selectPlatformAll();
        return R.ok(platforms);
    }

    /**
     * 获取平台选择框列表
     *
     * @return
     */
    @GetMapping("/filterPlatformByUserId")
    public R<List<SysPlatformVo>> filterPlatformByUserId() {
        List<SysPlatformVo> sysPlatformVos = platformService.filterPlatformByUserId();
        return R.ok(sysPlatformVos);
    }
}
