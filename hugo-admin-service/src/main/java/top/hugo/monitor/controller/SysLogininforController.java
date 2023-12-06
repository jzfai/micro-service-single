package top.hugo.monitor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.constant.CacheConstants;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;
import top.hugo.monitor.dto.SysLogininforDto;
import top.hugo.monitor.entity.SysLogininfor;
import top.hugo.monitor.query.SysLogininforQuery;
import top.hugo.monitor.service.SysLogininforService;
import top.hugo.monitor.vo.SysLogininforVo;
import top.hugo.redis.utils.RedisUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统访问记录信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-17 10:02:07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("monitor/loginInfo")
public class SysLogininforController {
    private final SysLogininforService sysLogininforService;

    /**
     * 获取sysLogininfor列表
     *
     * @return
     */
    //@SaCheckPermission("system:sysLogininfor:list")
    @PostMapping("/list")
    public TableDataInfo<SysLogininforVo> list(@RequestBody @Validated SysLogininforQuery sysLogininfor) {
        return sysLogininforService.selectPageSysLogininforList(sysLogininfor);
    }

    /**
     * 导出sysLogininfor列表
     */
    //@Log(title = "sysLogininfor管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysLogininforQuery sysLogininfor, HttpServletResponse response) {
        List<SysLogininforVo> list = sysLogininforService.selectSysLogininforList(sysLogininfor);
        EasyExcelUtils.exportExcel(list, "sysLogininfor数据", SysLogininforVo.class, response);
    }

    /**
     * 根据sysLogininfor编号获取详细信息
     *
     * @param sysLogininforId sysLogininforID
     */

    @GetMapping(value = "/{sysLogininforId}")
    public R<SysLogininfor> getInfo(@PathVariable Long sysLogininforId) {
        return R.ok(sysLogininforService.selectSysLogininforById(sysLogininforId));
    }

    /**
     * 新增sysLogininfor
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysLogininforDto sysLogininforDto) {
        SysLogininfor sysLogininfor = BeanCopyUtils.copy(sysLogininforDto, SysLogininfor.class);
        return R.result(sysLogininforService.insertSysLogininfor(sysLogininfor));
    }

    /**
     * 修改sysLogininfor
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysLogininforDto sysLogininforDto) {
        SysLogininfor sysLogininfor = BeanCopyUtils.copy(sysLogininforDto, SysLogininfor.class);
        return R.result(sysLogininforService.updateSysLogininfor(sysLogininfor));
    }

    /**
     * 删除sysLogininfor
     *
     * @param sysLogininforIds sysLogininforID串
     */
//@Log(title = "sysLogininfor管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysLogininforIds}")
    public R<Void> remove(@PathVariable Long[] sysLogininforIds) {
        return R.result(sysLogininforService.deleteSysLogininforByIds(sysLogininforIds));
    }

    /**
     * 获取sysLogininfor选择框列表
     */
    @GetMapping("/selectSysLogininforAll")
    public R<List<SysLogininforVo>> selectSysLogininforAll() {
        List<SysLogininforVo> sysLogininfors = sysLogininforService.selectSysLogininforAll();
        return R.ok(sysLogininfors);
    }

    /**
     * 清空系统登录日志
     */

    @DeleteMapping("/clean")
    public void cleanLogininfor() {
        sysLogininforService.delete(new LambdaQueryWrapper<>());
    }


    /*
     * 解锁用户(重置用户)
     * */
    @GetMapping("/unlock/{userName}")
    public R<Void> unlock(@PathVariable("userName") String userName) {
        String loginName = CacheConstants.PWD_ERR_CNT_KEY + userName;
        if (RedisUtils.hasKey(loginName)) {
            RedisUtils.deleteObject(loginName);
        }
        return R.ok();
    }
}
