package top.hugo.admin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.admin.dto.SysOperLogDto;
import top.hugo.admin.entity.SysOperLog;
import top.hugo.admin.query.SysOperLogQuery;
import top.hugo.admin.service.SysOperLogService;
import top.hugo.admin.vo.SysOperLogVo;
import top.hugo.common.annotation.Log;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志记录信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-21 13:58:50
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/operatorLog")
public class SysOperLogController {
    private final SysOperLogService sysOperLogService;

    /**
     * 获取sysOperLog列表
     *
     * @return
     */
    @Log(title = "sysOperLog管理")
    @PostMapping("/list")
    public TableDataInfo<SysOperLogVo> list(@RequestBody @Validated SysOperLogQuery sysOperLog) {
        return sysOperLogService.selectPageSysOperLogList(sysOperLog);
    }

    /**
     * 导出sysOperLog列表
     */
    //@Log(title = "sysOperLog管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOperLogQuery sysOperLog, HttpServletResponse response) {
        List<SysOperLogVo> list = sysOperLogService.selectSysOperLogList(sysOperLog);
        EasyExcelUtils.exportExcel(list, "sysOperLog数据", SysOperLogVo.class, response);
    }

    /**
     * 根据sysOperLog编号获取详细信息
     *
     * @param sysOperLogId sysOperLogID
     */

    @GetMapping(value = "/{sysOperLogId}")
    public R<SysOperLog> getInfo(@PathVariable Long sysOperLogId) {
        return R.ok(sysOperLogService.selectSysOperLogById(sysOperLogId));
    }

    /**
     * 新增sysOperLog
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysOperLogDto sysOperLogDto) {
        SysOperLog sysOperLog = BeanCopyUtils.copy(sysOperLogDto, SysOperLog.class);
        return R.result(sysOperLogService.insertSysOperLog(sysOperLog));
    }

    /**
     * 修改sysOperLog
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysOperLogDto sysOperLogDto) {
        SysOperLog sysOperLog = BeanCopyUtils.copy(sysOperLogDto, SysOperLog.class);
        return R.result(sysOperLogService.updateSysOperLog(sysOperLog));
    }

    /**
     * 删除sysOperLog
     *
     * @param sysOperLogIds sysOperLogID串
     */

    @DeleteMapping("/{sysOperLogIds}")
    public R<Void> remove(@PathVariable Long[] sysOperLogIds) {
        return R.result(sysOperLogService.deleteSysOperLogByIds(sysOperLogIds));
    }

    /**
     * 获取sysOperLog选择框列表
     */
    @GetMapping("/selectSysOperLogAll")
    public R<List<SysOperLogVo>> selectSysOperLogAll() {
        List<SysOperLogVo> sysOperLogs = sysOperLogService.selectSysOperLogAll();
        return R.ok(sysOperLogs);
    }
}
