package top.hugo.admin.controller;


import top.hugo.admin.entity.SysRole;
import top.hugo.admin.query.SysRoleQuery;
import top.hugo.admin.service.SysRoleService;
import top.hugo.admin.vo.SysRoleVo;
import top.hugo.common.annotation.Log;
import top.hugo.common.domain.R;
import top.hugo.common.dto.SysRoleDto;
import top.hugo.common.enums.BusinessType;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色信息表信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-20 14:36:16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController {
    private final SysRoleService sysRoleService;

    /**
     * 获取sysRole列表
     *
     * @return
     */
//@SaCheckPermission("system:sysRole:list")
    @PostMapping("/list")
    public TableDataInfo<SysRoleVo> list(@RequestBody @Validated SysRoleQuery sysRole) {
        return sysRoleService.selectPageSysRoleList(sysRole);
    }

    /**
     * 导出sysRole列表
     */
    @Log(title = "sysRole管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysRoleQuery sysRole, HttpServletResponse response) {
        List<SysRoleVo> list = sysRoleService.selectSysRoleList(sysRole);
        EasyExcelUtils.exportExcel(list, "sysRole数据", SysRoleVo.class, response);
    }

    /**
     * 根据sysRole编号获取详细信息
     *
     * @param sysRoleId sysRoleID
     */

    @GetMapping(value = "/{sysRoleId}")
    public R<SysRole> getInfo(@PathVariable Long sysRoleId) {
        return R.ok(sysRoleService.selectSysRoleById(sysRoleId));
    }

    /**
     * 新增sysRole
     *
     * @return
     */
    @PostMapping
    public R<Object> add(@Validated @RequestBody SysRoleDto sysRoleDto) {
        sysRoleService.insertSysRole(sysRoleDto);
        return R.ok();
    }

    /**
     * 修改sysRole
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysRoleDto sysRoleDto) {
        return R.result(sysRoleService.updateSysRole(sysRoleDto));
    }

    /**
     * 删除sysRole
     *
     * @param sysRoleIds sysRoleID串
     */
    //@Log(title = "sysRole管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysRoleIds}")
    public R<Void> remove(@PathVariable Long[] sysRoleIds) {
        return R.result(sysRoleService.deleteSysRoleByIds(sysRoleIds));
    }

    /**
     * 获取sysRole选择框列表
     */
    @GetMapping("/selectSysRoleAll")
    public R<List<SysRoleVo>> selectSysRoleAll() {
        List<SysRoleVo> sysRoles = sysRoleService.selectSysRoleAll();
        return R.ok(sysRoles);
    }


    /**
     * 获取sysRole选择框列表
     */
    @GetMapping("/getUserIdsByRoleId")
    public R<List<Long>> getUserIdsByRoleId(Long roleId) {
        List<Long> userIdsByRoleId = sysRoleService.getUserIdsByRoleId(roleId);
        return R.ok(userIdsByRoleId);
    }
}
