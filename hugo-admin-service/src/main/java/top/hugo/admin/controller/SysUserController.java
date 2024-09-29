package top.hugo.admin.controller;


import cn.dev33.satoken.secure.BCrypt;
import top.hugo.admin.dto.SysUserDto;
import top.hugo.admin.entity.SysUser;
import top.hugo.admin.query.SysUserQuery;
import top.hugo.admin.query.UserPostQuery;
import top.hugo.admin.service.SysUserService;
import top.hugo.admin.service.UserPostService;
import top.hugo.admin.vo.SysUserDetailVo;
import top.hugo.admin.vo.SysUserVo;
import top.hugo.admin.vo.UserPostVo;
import top.hugo.common.domain.R;
import top.hugo.domain.TableDataInfo;
import top.hugo.easyexecl.utils.EasyExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户信息表信息操作处理
 *
 * @author kuanghua|
 * @since 2023-11-20 14:48:14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController {
    private final SysUserService sysUserService;
    private final UserPostService userPostService;


    /**
     * 获取sysUser列表
     */
    @PostMapping("/list")
    public TableDataInfo<UserPostVo> selectPageUserPostList(@RequestBody @Validated UserPostQuery userPostQuery) {
        TableDataInfo<UserPostVo> userPostList = userPostService.selectPageUserPostList(userPostQuery);
        return userPostList;
    }

    @PostMapping("/selectPageSysUserPostList")
    public TableDataInfo<SysUserVo> selectPageSysUserPostList(@RequestBody @Validated SysUserQuery sysUser) {
        return sysUserService.selectPageSysUserPostList(sysUser);
    }

    /**
     * 导出sysUser列表
     */
    //@Log(title = "sysUser管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysUserQuery sysUser, HttpServletResponse response) {
        List<SysUserVo> list = sysUserService.selectSysUserList(sysUser);
        EasyExcelUtils.exportExcel(list, "sysUser数据", SysUserVo.class, response);
    }

    /**
     * 根据sysUser编号获取详细信息
     *
     * @param sysUserId sysUserID
     * @return
     */

    @GetMapping(value = "/{sysUserId}")
    public R<SysUserDetailVo> getInfo(@PathVariable Long sysUserId) {
        return R.ok(sysUserService.selectSysUserById(sysUserId));
    }
    /**
     * 新增sysUser
     */
    @PostMapping("syncUser")
    public R<Void> syncUser(@Validated @RequestBody SysUserDto sysUserDto) {
        sysUserService.syncUser(sysUserDto);
        return  R.ok();
    }

    /**
     * 新增sysUser
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysUserDto sysUserDto) {
        return R.result(sysUserService.insertSysUser(sysUserDto));
    }

    /**
     * 修改sysUser
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysUserDto sysUserDto) {
        return R.result(sysUserService.updateSysUser(sysUserDto));
    }

    /**
     * 删除sysUser
     *
     * @param sysUserIds sysUserID串
     */
//@Log(title = "sysUser管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysUserIds}")
    public R<Void> remove(@PathVariable Long[] sysUserIds) {
        return R.result(sysUserService.deleteSysUserByIds(sysUserIds));
    }

    /**
     * 状态修改
     */
    //@SaCheckPermission("system:user:edit")
    //@Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody SysUser user) {
        //sysUserService.checkUserDataScope(user.getUserId());
        return R.result(sysUserService.updateUserStatus(user));
    }

    /**
     * 获取sysUser选择框列表
     */
    @GetMapping("/selectSysUserAll")
    public R<List<SysUserVo>> selectSysUserAll() {
        List<SysUserVo> sysUsers = sysUserService.selectSysUserAll();
        return R.ok(sysUsers);
    }


    @PutMapping("/resetPwd")
    public R<Void> resetPwd(@RequestBody SysUser sysUser) {
        sysUser.setPassword(BCrypt.hashpw(sysUser.getPassword()));
        return R.result(sysUserService.updateUserPassWord(sysUser));
    }


}
