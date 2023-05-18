package top.hugo.monitor;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.annotation.Log;
import top.hugo.common.constant.CacheConstants;
import top.hugo.common.controller.BaseController;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.domain.R;
import top.hugo.common.enums.BusinessType;
import top.hugo.common.excel.ExcelUtil;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.RedisUtils;
import top.hugo.system.entity.SysLogininfor;
import top.hugo.system.service.SysLoginInfoService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 测试批量方法
 *
 * @author Lion Li
 * @date 2021-05-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/loginInfo")
public class SysLoginInfoController extends BaseController {

    private final SysLoginInfoService logininforService;

    /**
     * 新增批量方法 可完美替代 saveBatch 秒级插入上万数据 (对mysql负荷较大)
     * <p>
     * 3.5.0 版本 增加 rewriteBatchedStatements=true 批处理参数 使 MP 原生批处理可以达到同样的速度
     */
    //@SaCheckPermission("monitor:logininfor:list")
    @GetMapping("/list")
    public TableDataInfo<SysLogininfor> list(SysLogininfor logininfor, PageQuery pageQuery) {
        return logininforService.selectPageLogininforList(logininfor, pageQuery);
    }

    /**
     * 导出系统访问记录列表
     */
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    //@SaCheckPermission("monitor:logininfor:export")
    @PostMapping("/export")
    public void export(SysLogininfor logininfor, HttpServletResponse response) {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil.exportExcel(list, "登录日志", SysLogininfor.class, response);
    }

    /**
     * 批量删除登录日志
     *
     * @param infoIds 日志ids
     */
    //@SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public R<Void> remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    /**
     * 清理系统访问记录
     */
    //@SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R<Void> clean() {
        logininforService.cleanLogininfor();
        return R.ok();
    }

    //@SaCheckPermission("monitor:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public R<Void> unlock(@PathVariable("userName") String userName) {
        String loginName = CacheConstants.PWD_ERR_CNT_KEY + userName;
        if (RedisUtils.hasKey(loginName)) {
            RedisUtils.deleteObject(loginName);
        }
        return R.ok();
    }
}
