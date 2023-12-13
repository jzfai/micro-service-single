package top.hugo.generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;
import top.hugo.generator.dto.TemplateFileDto;
import top.hugo.generator.entity.TemplateFile;
import top.hugo.generator.query.TemplateFileQuery;
import top.hugo.generator.service.TemplateFileService;
import top.hugo.generator.vo.TemplateFileVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * templateFile模块
 *
 * @author kuanghua
 * @since 2023-10-18 17:23:16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/generator/templateFile")
public class TemplateFileController {

    private final TemplateFileService templateFileService;

    /**
     * 获取templateFile列表(分页)
     */
    @GetMapping("/listPage")
    public TableDataInfo<TemplateFileVo> list(@Validated TemplateFileQuery templateFile) {
        TableDataInfo<TemplateFileVo> list = templateFileService.selectPageTemplateFileList(templateFile);
        return list;
    }

    /**
     * 导出templateFile列表
     */
    @GetMapping("/export")
    public void export(@Validated TemplateFileQuery templateFile, HttpServletResponse response) {
        List<TemplateFileVo> list = templateFileService.selectTemplateFileList(templateFile);
        EasyExcelUtils.exportExcel(list, "templateFile数据", TemplateFileVo.class, response);
    }

    /**
     * 获取详细信息
     *
     * @param templateFileId templateFileID
     */
    //@SaCheckPermission("system:templateFile:query")
    @GetMapping(value = "/{templateFileId}")
    public R<TemplateFileVo> getInfo(@PathVariable Long templateFileId) {
        return R.ok(templateFileService.selectTemplateFileById(templateFileId));
    }

    /**
     * 新增templateFile
     */
    //@SaCheckPermission("system:templateFile:add")
    //@Log(title = "templateFile管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody TemplateFileDto templateFileDto) {
        TemplateFile templateFile = BeanCopyUtils.copy(templateFileDto, TemplateFile.class);
        return R.result(templateFileService.insertTemplateFile(templateFile));
    }

    /**
     * 修改templateFile
     */
    //@SaCheckPermission("system:templateFile:edit")
    //@Log(title = "templateFile管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody TemplateFileDto templateFileDto) {
        TemplateFile templateFile = BeanCopyUtils.copy(templateFileDto, TemplateFile.class);
        return R.result(templateFileService.updateTemplateFile(templateFile));
    }


    /**
     * 删除templateFile
     *
     * @param templateFileId templateFileID串
     */
    @DeleteMapping("deleteById/{templateFileId}")
    public R<Void> deleteTemplateFileById(@PathVariable Long templateFileId) {
        return R.result(templateFileService.deleteTemplateFileById(templateFileId));
    }

    /**
     * 删除templateFile
     *
     * @param templateFileIds templateFileID串
     */
    //@SaCheckPermission("system:templateFile:remove")
    //@Log(title = "templateFile管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateFileIds}")
    public R<Void> remove(@PathVariable Long[] templateFileIds) {
        return R.result(templateFileService.deleteTemplateFileByIds(templateFileIds));
    }

    /**
     * 获取templateFile列表(所有)
     */
    @GetMapping("/selectTemplateFileList")
    public R<List<TemplateFileVo>> selectTemplateFileList(@Validated TemplateFileQuery templateFile) {
        List<TemplateFileVo> templateFileList = templateFileService.selectTemplateFileList(templateFile);
        return R.ok(templateFileList);
    }
}
