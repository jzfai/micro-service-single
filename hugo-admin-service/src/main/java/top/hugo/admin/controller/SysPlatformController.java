package top.hugo.admin.controller;


import top.hugo.admin.dto.SysPlatformDto;
import top.hugo.admin.entity.SysPlatform;
import top.hugo.admin.query.SysPlatformQuery;
import top.hugo.admin.service.SysPlatformService;
import top.hugo.admin.vo.SysPlatformVo;
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
 * 平台信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-10 11:54:52
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/platform")
public class SysPlatformController {
    private final SysPlatformService sysPlatformService;

    /**
     * 获取sysPlatform列表
     *
     * @return
     */
//@SaCheckPermission("system:sysPlatform:list")
    @PostMapping("/list")
    public TableDataInfo<SysPlatformVo> list(@RequestBody @Validated SysPlatformQuery sysPlatform) {
        return sysPlatformService.selectPageSysPlatformList(sysPlatform);
    }

    /**
     * 导出sysPlatform列表
     */
//@Log(title = "sysPlatform管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysPlatformQuery sysPlatform, HttpServletResponse response) {
        List<SysPlatformVo> list = sysPlatformService.selectSysPlatformList(sysPlatform);
        EasyExcelUtils.exportExcel(list, "sysPlatform数据", SysPlatformVo.class, response);
    }

    /**
     * 根据sysPlatform编号获取详细信息
     *
     * @param sysPlatformId sysPlatformID
     */

    @GetMapping(value = "/{sysPlatformId}")
    public R<SysPlatform> getInfo(@PathVariable Long sysPlatformId) {
        return R.ok(sysPlatformService.selectSysPlatformById(sysPlatformId));
    }

    /**
     * 新增sysPlatform
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysPlatformDto sysPlatformDto) {
        SysPlatform sysPlatform = BeanCopyUtils.copy(sysPlatformDto, SysPlatform.class);
        return R.result(sysPlatformService.insertSysPlatform(sysPlatform));
    }

    /**
     * 修改sysPlatform
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysPlatformDto sysPlatformDto) {
        SysPlatform sysPlatform = BeanCopyUtils.copy(sysPlatformDto, SysPlatform.class);
        return R.result(sysPlatformService.updateSysPlatform(sysPlatform));
    }

    /**
     * 删除sysPlatform
     *
     * @param sysPlatformIds sysPlatformID串
     */
    //@Log(title = "sysPlatform管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysPlatformIds}")
    public R<Void> remove(@PathVariable Long[] sysPlatformIds) {
        return R.result(sysPlatformService.deleteSysPlatformByIds(sysPlatformIds));
    }

    /**
     * 获取sysPlatform选择框列表
     */
    @GetMapping("/selectSysPlatformAll")
    public R<List<SysPlatformVo>> selectSysPlatformAll() {
        List<SysPlatformVo> sysPlatforms = sysPlatformService.selectSysPlatformAll();
        return R.ok(sysPlatforms);
    }
}
