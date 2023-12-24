package top.hugo.admin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.admin.dto.SysDictypeDto;
import top.hugo.admin.entity.SysDictype;
import top.hugo.common.enums.BusinessType;
import top.hugo.admin.query.SysDictypeQuery;
import top.hugo.admin.service.SysDictypeService;
import top.hugo.admin.vo.SysDictypeVo;
import top.hugo.common.annotation.Log;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 字典类型表信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-13 10:58:29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/sysDictype")
public class SysDictypeController {
    private final SysDictypeService sysDictypeService;

    /**
     * 获取sysDictype列表
     *
     * @return
     */
//@SaCheckPermission("system:sysDictype:list")
    @PostMapping("/list")
    @Log(title = "字典类型",businessType = BusinessType.OTHER)
    public TableDataInfo<SysDictypeVo> list(@RequestBody @Validated SysDictypeQuery sysDictype) {
        return sysDictypeService.selectPageSysDictypeList(sysDictype);
    }

    /**
     * 导出sysDictype列表
     */
    @PostMapping("/export")
    @Log(title = "字典类型",businessType = BusinessType.EXPORT)
    public void export(SysDictypeQuery sysDictype, HttpServletResponse response) {
        List<SysDictypeVo> list = sysDictypeService.selectSysDictypeList(sysDictype);
        EasyExcelUtils.exportExcel(list, "sysDictype数据", SysDictypeVo.class, response);
    }

    /**
     * 根据sysDictype编号获取详细信息
     *
     * @param sysDictypeId sysDictypeID
     */

    @GetMapping(value = "/{sysDictypeId}")
    public R<SysDictype> getInfo(@PathVariable Long sysDictypeId) {
        return R.ok(sysDictypeService.selectSysDictypeById(sysDictypeId));
    }

    /**
     * 新增sysDictype
     */
    @PostMapping
    @Log(title = "字典类型",businessType = BusinessType.INSERT)
    public R<Void> add(@Validated @RequestBody SysDictypeDto sysDictypeDto) {
        SysDictype sysDictype = BeanCopyUtils.copy(sysDictypeDto, SysDictype.class);
        return R.result(sysDictypeService.insertSysDictype(sysDictype));
    }

    /**
     * 修改sysDictype
     */
    @PutMapping
    @Log(title = "字典类型",businessType = BusinessType.UPDATE)
    public R<Void> edit(@Validated @RequestBody SysDictypeDto sysDictypeDto) {
        SysDictype sysDictype = BeanCopyUtils.copy(sysDictypeDto, SysDictype.class);
        return R.result(sysDictypeService.updateSysDictype(sysDictype));
    }

    /**
     * 删除sysDictype
     *
     * @param sysDictypeIds sysDictypeID串
     */
//@Log(title = "sysDictype管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysDictypeIds}")
    @Log(title = "字典类型",businessType = BusinessType.DELETE)
    public R<Void> remove(@PathVariable Long[] sysDictypeIds) {
        return R.result(sysDictypeService.deleteSysDictypeByIds(sysDictypeIds));
    }

    /**
     * 获取sysDictype选择框列表
     */
    @GetMapping("/selectSysDictypeAll")
    public R<List<SysDictypeVo>> selectSysDictypeAll() {
        List<SysDictypeVo> sysDictypes = sysDictypeService.selectSysDictypeAll();
        return R.ok(sysDictypes);
    }
}
