package top.hugo.system.controller;


import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.controller.BaseController;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.domain.R;
import top.hugo.common.excel.ExcelUtil;
import top.hugo.common.page.TableDataInfo;
import top.hugo.system.entity.SysDept;
import top.hugo.system.entity.SysUser;
import top.hugo.system.helper.LoginHelper;
import top.hugo.system.service.SysDeptService;
import top.hugo.system.service.SysUserService;
import top.hugo.system.vo.SysUserExportVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户信息
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    private final SysUserService sysUserService;
    private final SysDeptService deptService;
//    private final SysRoleService roleService;
//    private final SysPostService postService;

    /**
     * 获取用户列表
     */
    //@SaCheckPermission("system:user:list")
    @GetMapping("/list")
    public TableDataInfo<SysUser> list(SysUser user, PageQuery pageQuery) {
        return sysUserService.selectPageUserList(user, pageQuery);
    }


    /**
     * 导出用户列表
     */
    //@Log(title = "用户管理", businessType = BusinessType.EXPORT)
//    @SaCheckPermission("system:user:export")
    @PostMapping("/export")
    public void export(SysUser user, HttpServletResponse response) {
        List<SysUser> list = sysUserService.selectUserList(user);
        List<SysUserExportVo> listVo = BeanUtil.copyToList(list, SysUserExportVo.class);
        for (int i = 0; i < list.size(); i++) {
            SysDept dept = list.get(i).getDept();
            SysUserExportVo vo = listVo.get(i);
            if (ObjectUtil.isNotEmpty(dept)) {
                vo.setDeptName(dept.getDeptName());
                vo.setLeader(dept.getLeader());
            }
        }
        ExcelUtil.exportExcel(listVo, "用户数据", SysUserExportVo.class, response);
    }


    /**
     * 重置密码
     */
    //@SaCheckPermission("system:user:resetPwd")
    //@Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public R<Void> resetPwd(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        //sysUserService.checkUserDataScope(user.getUserId());
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        return toAjax(sysUserService.resetPwd(user));
    }

    /**
     * 删除用户
     *
     * @param userIds 角色ID串
     */
    //@SaCheckPermission("system:user:remove")
    //@Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<Void> remove(@PathVariable Long[] userIds) {
        if (ArrayUtil.contains(userIds, LoginHelper.getUserId())) {
            return R.fail("当前用户不能删除");
        }
        return toAjax(sysUserService.deleteUserByIds(userIds));
    }


    /**
     * 状态修改
     */
    //@SaCheckPermission("system:user:edit")
    //@Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        //sysUserService.checkUserDataScope(user.getUserId());
        return toAjax(sysUserService.updateUserStatus(user));
    }

    /**
     * 获取部门树列表
     */
    //@SaCheckPermission("system:user:list")
    @GetMapping("/deptTree")
    public R<List<Tree<Long>>> deptTree(SysDept dept) {
        return R.ok(deptService.selectDeptTreeList(dept));
    }

}