package top.hugo.admin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.admin.dto.SysDeptDto;
import top.hugo.admin.entity.SysDept;
import top.hugo.admin.query.SysDeptQuery;
import top.hugo.admin.service.SysDeptService;
import top.hugo.admin.vo.SysDeptVo;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 部门表信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-20 09:38:20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dept")
public class SysDeptController {
    private final SysDeptService sysDeptService;

    /**
     * 获取sysDept列表
     *
     * @return
     */
    //@SaCheckPermission("system:sysDept:list")
    @PostMapping("/listPage")
    public TableDataInfo<SysDeptVo> listPage(@RequestBody @Validated SysDeptQuery sysDept) {
        return sysDeptService.selectPageSysDeptList(sysDept);
    }


    /**
     * 获取sysDept列表
     *
     * @return
     */
    //@SaCheckPermission("system:sysDept:list")
    @PostMapping("/list")
    public R<List<SysDeptVo>> list(@RequestBody @Validated SysDeptQuery sysDept) {
        return R.ok(sysDeptService.selectSysDeptList(sysDept));
    }

    /**
     * 导出sysDept列表
     */
    //@Log(title = "sysDept管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysDeptQuery sysDept, HttpServletResponse response) {
        List<SysDeptVo> list = sysDeptService.selectSysDeptList(sysDept);
        EasyExcelUtils.exportExcel(list, "sysDept数据", SysDeptVo.class, response);
    }

    /**
     * 根据sysDept编号获取详细信息
     *
     * @param sysDeptId sysDeptID
     */

    @GetMapping(value = "/{sysDeptId}")
    public R<SysDept> getInfo(@PathVariable Long sysDeptId) {
        return R.ok(sysDeptService.selectSysDeptById(sysDeptId));
    }

    /**
     * 新增sysDept
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysDeptDto sysDeptDto) {
        SysDept sysDept = BeanCopyUtils.copy(sysDeptDto, SysDept.class);
        return R.result(sysDeptService.insertSysDept(sysDept));
    }

    /**
     * 修改sysDept
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysDeptDto sysDeptDto) {
        SysDept sysDept = BeanCopyUtils.copy(sysDeptDto, SysDept.class);
        return R.result(sysDeptService.updateSysDept(sysDept));
    }

    /**
     * 删除sysDept
     *
     * @param sysDeptIds sysDeptID串
     */
//@Log(title = "sysDept管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysDeptIds}")
    public R<Void> remove(@PathVariable Long[] sysDeptIds) {
        return R.result(sysDeptService.deleteSysDeptByIds(sysDeptIds));
    }

    /**
     * 获取sysDept选择框列表
     */
    @GetMapping("/selectSysDeptAll")
    public R<List<SysDeptVo>> selectSysDeptAll() {
        List<SysDeptVo> sysDepts = sysDeptService.selectSysDeptAll();
        return R.ok(sysDepts);
    }
}
