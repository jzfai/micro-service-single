package top.hugo.system.controller;


import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hugo.common.constant.UserConstants;
import top.hugo.common.controller.BaseController;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.domain.R;
import top.hugo.common.excel.ExcelResult;
import top.hugo.common.excel.ExcelUtil;
import top.hugo.common.helper.LoginHelper;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.StreamUtils;
import top.hugo.system.entity.SysDept;
import top.hugo.system.entity.SysRole;
import top.hugo.system.entity.SysUser;
import top.hugo.system.listener.SysUserImportListener;
import top.hugo.system.service.SysDeptService;
import top.hugo.system.service.SysPostService;
import top.hugo.system.service.SysRoleService;
import top.hugo.system.service.SysUserService;
import top.hugo.system.vo.SysUserExportVo;
import top.hugo.system.vo.SysUserImportVo;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息
 *
 * @author hugo
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    private final SysUserService sysUserService;
    private final SysDeptService deptService;
    private final SysRoleService sysRoleService;
    private final SysPostService sysPostService;

    /**
     * 根据用户编号获取详细信息
     *
     * @param userId 用户ID
     */
    //@SaCheckPermission("system:user:query")
    @GetMapping(value = {"", "/{userId}"})
    public R<Map<String, Object>> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        sysUserService.checkUserDataScope(userId);
        Map<String, Object> ajax = new HashMap<>();
        List<SysRole> roles = sysRoleService.selectRoleAll();
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin()));
        ajax.put("posts", sysPostService.selectPostAll());
        if (ObjectUtil.isNotNull(userId)) {
            SysUser sysUser = sysUserService.selectUserById(userId);
            ajax.put("user", sysUser);
            ajax.put("postIds", sysPostService.selectPostListByUserId(userId));
            ajax.put("roleIds", StreamUtils.toList(sysUser.getRoles(), SysRole::getRoleId));
        }
        return R.ok(ajax);
    }

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
    //@SaCheckPermission("system:user:export")
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


    /**
     * 新增用户
     */
    //    @SaCheckPermission("system:user:add")
    //    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(sysUserService.checkUserNameUnique(user))) {
            return R.fail("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (ObjectUtil.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(user))) {
            return R.fail("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (ObjectUtil.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(sysUserService.checkEmailUnique(user))) {
            return R.fail("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        return toAjax(sysUserService.insertUser(user));
    }

    /**
     * 修改用户
     */
    //@SaCheckPermission("system:user:edit")
    //@Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        sysUserService.checkUserDataScope(user.getUserId());
        if (UserConstants.NOT_UNIQUE.equals(sysUserService.checkUserNameUnique(user))) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (ObjectUtil.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(sysUserService.checkPhoneUnique(user))) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (ObjectUtil.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(sysUserService.checkEmailUnique(user))) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        return toAjax(sysUserService.updateUser(user));
    }

    /**
     * 根据用户编号获取授权角色
     *
     * @param userId 用户ID
     */
    //    @SaCheckPermission("system:user:query")
    @GetMapping("/authRole/{userId}")
    public R<Map<String, Object>> authRole(@PathVariable Long userId) {
        SysUser user = sysUserService.selectUserById(userId);
        List<SysRole> roles = sysRoleService.selectRolesByUserId(userId);
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin()));
        return R.ok(ajax);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户Id
     * @param roleIds 角色ID串
     */
    //    @SaCheckPermission("system:user:edit")
    //    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public R<Void> insertAuthRole(Long userId, Long[] roleIds) {
        sysUserService.checkUserDataScope(userId);
        sysUserService.insertUserAuth(userId, roleIds);
        return R.ok();
    }

    /**
     * 导入数据
     *
     * @param file          导入文件
     * @param updateSupport 是否更新已存在数据
     */
    //    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    //    @SaCheckPermission("system:user:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<String> importData(@RequestPart("file") MultipartFile file, boolean updateSupport) throws Exception {
        ExcelResult<SysUserImportVo> result = ExcelUtil.importExcel(file.getInputStream(), SysUserImportVo.class, new SysUserImportListener(updateSupport));
        return R.ok(result.getAnalysis());
    }


    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "用户数据", SysUserImportVo.class, response);
    }
}
