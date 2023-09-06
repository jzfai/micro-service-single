package top.hugo.oss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.common.domain.R;
import top.hugo.common.excel.EasyExcelUtils;
import top.hugo.common.page.TableDataInfo;
import top.hugo.oss.query.SysOssQuery;
import top.hugo.oss.service.SysOssService;
import top.hugo.oss.vo.SysOssVo;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * OSS对象存储表
 *
 * @author kuanghua
 * @since 2023-09-06 11:14:58
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/oss")
public class SysOssController {
    private final SysOssService sysOssService;

    /**
     * 获取sysOss列表(分页)
     *
     * @return
     */
    @GetMapping("/listPage")
    public TableDataInfo<SysOssVo> list(@Validated SysOssQuery sysOss) {
        TableDataInfo<SysOssVo> list = sysOssService.selectPageSysOssList(sysOss);
        return list;
    }

    /**
     * 导出sysOss列表
     */
    @GetMapping("/export")
    public void export(@Validated SysOssQuery sysOss, HttpServletResponse response) {
        List<SysOssVo> list = sysOssService.selectSysOssList(sysOss);
        EasyExcelUtils.exportExcel(list, "sysOss数据", SysOssVo.class, response);
    }

    /**
     * 获取详细信息
     *
     * @param sysOssId sysOssID
     */
//@SaCheckPermission("system:sysOss:query")
    @GetMapping(value = "/{sysOssId}")
    public R<SysOssVo> getInfo(@PathVariable Long sysOssId) {
        return R.ok(sysOssService.selectSysOssById(sysOssId));
    }

    /**
     * 新增sysOss
     *
     * @return
     */
    //@SaCheckPermission("system:sysOss:add")
    //@Log(title = "sysOss管理", businessType = BusinessType.INSERT)
    @PostMapping("upload")
    public R<HashMap<String, Object>> add(MultipartFile file) {
        HashMap<String, Object> insertSysOss = sysOssService.insertSysOss(file);
        return R.ok(insertSysOss);
    }


    /**
     * 删除sysOss
     *
     * @param sysOssId
     */
    @DeleteMapping("/{sysOssId}")
    public R<Void> deleteSysOssById(@PathVariable Long sysOssId) {
        return R.result(sysOssService.deleteSysOssById(sysOssId));
    }

    /**
     * 删除sysOss
     *
     * @param sysOssIds sysOssID串
     */
    @DeleteMapping("mul/{sysOssIds}")
    public R<Void> deleteSysOssByIds(@PathVariable Collection<Long> sysOssIds) {
        return R.result(sysOssService.deleteSysOssByIds(sysOssIds));
    }


    /**
     * 获取sysOss列表(所有)
     */
    @GetMapping("/selectSysOssList")
    public R<List<SysOssVo>> selectSysOssList(@Validated SysOssQuery sysOss) {
        List<SysOssVo> sysOssList = sysOssService.selectSysOssList(sysOss);
        return R.ok(sysOssList);
    }
}
