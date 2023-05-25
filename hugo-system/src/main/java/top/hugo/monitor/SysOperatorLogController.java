package top.hugo.monitor;


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
import top.hugo.system.entity.SysOperLog;
import top.hugo.system.service.SysOperatorLogService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志记录
 *
 * @author kuanghua
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/operatorLog")
public class SysOperatorLogController extends BaseController {

    private final SysOperatorLogService operLogService;

    /**
     * 获取操作日志记录列表
     */
    //@SaCheckPermission("monitor:operlog:list")
    @GetMapping("/list")
    public TableDataInfo<SysOperLog> list(SysOperLog operLog, PageQuery pageQuery) {
        return operLogService.selectPageOperLogList(operLog, pageQuery);
    }

    /**
     * 导出操作日志记录列表
     */
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    //@SaCheckPermission("monitor:operlog:export")
    @PostMapping("/export")
    public void export(SysOperLog operLog, HttpServletResponse response) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil.exportExcel(list, "操作日志", SysOperLog.class, response);
    }

    /**
     * 批量删除操作日志记录
     *
     * @param operIds 日志ids
     */
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    //@SaCheckPermission("monitor:operlog:remove")
    @DeleteMapping("/{operIds}")
    public R<Void> remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    /**
     * 清理操作日志记录
     */
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    //@SaCheckPermission("monitor:operlog:remove")
    @DeleteMapping("/clean")
    public R<Void> clean() {
        operLogService.cleanOperLog();
        return R.ok();
    }
}
