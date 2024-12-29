package top.hugo.generator.controller;


import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;
import top.hugo.generator.dto.TemplateFileDetailDto;
import top.hugo.generator.entity.TemplateFileDetail;
import top.hugo.generator.query.TemplateFileDetailQuery;
import top.hugo.generator.service.TemplateFileDetailService;
import top.hugo.generator.vo.TemplateFileDetailVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 模板详情信息操作处理
 *
 * @author kuanghua|
 * @since 2024-04-28 15:46:13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("generator/templateFileDetail")
public class TemplateFileDetailController {
    private final TemplateFileDetailService templateFileDetailService;

    /**
     * 获取templateFileDetail列表
     *
     * @return
     */
//@SaCheckPermission("system:templateFileDetail:list")
    @PostMapping("/list")
    public TableDataInfo<TemplateFileDetailVo> list(@RequestBody @Validated TemplateFileDetailQuery templateFileDetail) {
        return templateFileDetailService.selectPageTemplateFileDetailList(templateFileDetail);
    }



    /**
     * 导出templateFileDetail列表
     */
//@Log(title = "templateFileDetail管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(TemplateFileDetailQuery templateFileDetail, HttpServletResponse response) {
        List<TemplateFileDetailVo> list = templateFileDetailService.selectTemplateFileDetailList(templateFileDetail);
        EasyExcelUtils.exportExcel(list, "templateFileDetail数据", TemplateFileDetailVo.class, response);
    }

    /**
     * 根据templateFileDetail编号获取详细信息
     *
     * @param templateFileDetailId templateFileDetailID
     */

    @GetMapping(value = "/{templateFileDetailId}")
    public R<TemplateFileDetail> getInfo(@PathVariable Long templateFileDetailId) {
        return R.ok(templateFileDetailService.selectTemplateFileDetailById(templateFileDetailId));
    }

    /**
     * 新增templateFileDetail
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody TemplateFileDetailDto templateFileDetailDto) {
        TemplateFileDetail templateFileDetail = BeanCopyUtils.copy(templateFileDetailDto, TemplateFileDetail.class);
        return R.result(templateFileDetailService.insertTemplateFileDetail(templateFileDetail));
    }

    /**
     * 修改templateFileDetail
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody TemplateFileDetailDto templateFileDetailDto) {
        TemplateFileDetail templateFileDetail = BeanCopyUtils.copy(templateFileDetailDto, TemplateFileDetail.class);
        return R.result(templateFileDetailService.updateTemplateFileDetail(templateFileDetail));
    }

    /**
     * 删除templateFileDetail
     *
     * @param templateFileDetailIds templateFileDetailID串
     */
//@Log(title = "templateFileDetail管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateFileDetailIds}")
    public R<Void> remove(@PathVariable Long[] templateFileDetailIds) {
        return R.result(templateFileDetailService.deleteTemplateFileDetailByIds(templateFileDetailIds));
    }

    /**
     * 获取templateFileDetail选择框列表
     */
    @PostMapping("/selectTemplateFileDetailAll")
    public R<List<TemplateFileDetailVo>> selectTemplateFileDetailAll(@RequestBody @Validated TemplateFileDetailQuery templateFileDetail) {
        List<TemplateFileDetailVo> templateFileDetails = templateFileDetailService.selectTemplateFileDetailAll(templateFileDetail);
        return R.ok(templateFileDetails);
    }
}
