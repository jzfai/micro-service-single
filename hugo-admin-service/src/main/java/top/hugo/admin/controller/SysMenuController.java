package top.hugo.admin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.admin.dto.SysMenuDto;
import top.hugo.admin.entity.SysMenu;
import top.hugo.admin.query.SysMenuQuery;
import top.hugo.admin.service.SysMenuService;
import top.hugo.admin.vo.SysMenuVo;
import top.hugo.common.domain.R;
import top.hugo.common.utils.BeanCopyUtils;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 菜单权限表信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-16 10:14:25
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {
    private final SysMenuService sysMenuService;

    /**
     * 获取sysMenu列表
     *
     * @return
     */
    //@SaCheckPermission("system:sysMenu:list")
    @PostMapping("/pageList")
    public TableDataInfo<SysMenuVo> pageList(@RequestBody @Validated SysMenuQuery sysMenu) {
        return sysMenuService.selectPageSysMenuList(sysMenu);
    }

    @PostMapping("/list")
    public R<List<SysMenuVo>> list(@RequestBody @Validated SysMenuQuery sysMenu) {
        List<SysMenuVo> sysMenuVos = sysMenuService.selectSysMenuList(sysMenu);
        return R.ok(sysMenuVos);
    }

    /**
     * 导出sysMenu列表
     */
//@Log(title = "sysMenu管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysMenuQuery sysMenu, HttpServletResponse response) {
        List<SysMenuVo> list = sysMenuService.selectSysMenuList(sysMenu);
        EasyExcelUtils.exportExcel(list, "sysMenu数据", SysMenuVo.class, response);
    }

    /**
     * 根据sysMenu编号获取详细信息
     *
     * @param sysMenuId sysMenuID
     */

    @GetMapping(value = "/{sysMenuId}")
    public R<SysMenu> getInfo(@PathVariable Long sysMenuId) {
        return R.ok(sysMenuService.selectSysMenuById(sysMenuId));
    }

    /**
     * 新增sysMenu
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysMenuDto sysMenuDto) {
        SysMenu sysMenu = BeanCopyUtils.copy(sysMenuDto, SysMenu.class);
        return R.result(sysMenuService.insertSysMenu(sysMenu));
    }

    /**
     * 修改sysMenu
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysMenuDto sysMenuDto) {
        SysMenu sysMenu = BeanCopyUtils.copy(sysMenuDto, SysMenu.class);
        return R.result(sysMenuService.updateSysMenu(sysMenu));
    }

    /**
     * 删除sysMenu
     *
     * @param sysMenuIds sysMenuID串
     */
//@Log(title = "sysMenu管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysMenuIds}")
    public R<Void> remove(@PathVariable Long[] sysMenuIds) {
        return R.result(sysMenuService.deleteSysMenuByIds(sysMenuIds));
    }

    /**
     * 获取sysMenu选择框列表
     */
    @GetMapping("/selectSysMenuAll")
    public R<List<SysMenuVo>> selectSysMenuAll() {
        List<SysMenuVo> sysMenus = sysMenuService.selectSysMenuAll();
        return R.ok(sysMenus);
    }
}
