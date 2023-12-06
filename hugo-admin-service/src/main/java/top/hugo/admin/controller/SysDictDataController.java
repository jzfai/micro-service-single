package top.hugo.admin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.admin.dto.SysDictDataDto;
import top.hugo.admin.entity.SysDictData;
import top.hugo.admin.query.SysDictDataQuery;
import top.hugo.admin.service.SysDictDataService;
import top.hugo.admin.vo.SysDictDataVo;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 字典数据表信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-13 15:52:45
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/sysDictData")
public class SysDictDataController {
    private final SysDictDataService sysDictDataService;

    /**
     * 获取sysDictData列表
     *
     * @return
     */
//@SaCheckPermission("system:sysDictData:list")
    @PostMapping("/list")
    public TableDataInfo<SysDictDataVo> list(@RequestBody @Validated SysDictDataQuery sysDictData) {
        return sysDictDataService.selectPageSysDictDataList(sysDictData);
    }

    /**
     * 导出sysDictData列表
     */
//@Log(title = "sysDictData管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysDictDataQuery sysDictData, HttpServletResponse response) {
        List<SysDictDataVo> list = sysDictDataService.selectSysDictDataList(sysDictData);
        EasyExcelUtils.exportExcel(list, "sysDictData数据", SysDictDataVo.class, response);
    }

    /**
     * 根据sysDictData编号获取详细信息
     *
     * @param sysDictDataId sysDictDataID
     */

    @GetMapping(value = "/{sysDictDataId}")
    public R<SysDictData> getInfo(@PathVariable Long sysDictDataId) {
        return R.ok(sysDictDataService.selectSysDictDataById(sysDictDataId));
    }

    /**
     * 新增sysDictData
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysDictDataDto sysDictDataDto) {
        SysDictData sysDictData = BeanCopyUtils.copy(sysDictDataDto, SysDictData.class);
        return R.result(sysDictDataService.insertSysDictData(sysDictData));
    }

    /**
     * 修改sysDictData
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysDictDataDto sysDictDataDto) {
        SysDictData sysDictData = BeanCopyUtils.copy(sysDictDataDto, SysDictData.class);
        return R.result(sysDictDataService.updateSysDictData(sysDictData));
    }

    /**
     * 删除sysDictData
     *
     * @param sysDictDataIds sysDictDataID串
     */
//@Log(title = "sysDictData管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysDictDataIds}")
    public R<Void> remove(@PathVariable Long[] sysDictDataIds) {
        return R.result(sysDictDataService.deleteSysDictDataByIds(sysDictDataIds));
    }

    /**
     * 获取sysDictData选择框列表
     */
    @GetMapping("/selectSysDictDataAll")
    public R<List<SysDictDataVo>> selectSysDictDataAll() {
        List<SysDictDataVo> sysDictDatas = sysDictDataService.selectSysDictDataAll();
        return R.ok(sysDictDatas);
    }
}
