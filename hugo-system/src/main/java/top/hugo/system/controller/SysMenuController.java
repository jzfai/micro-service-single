package top.hugo.system.controller;

import cn.hutool.core.lang.tree.Tree;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hugo.common.constant.UserConstants;
import top.hugo.common.controller.BaseController;
import top.hugo.common.domain.R;
import top.hugo.common.utils.StringUtils;
import top.hugo.system.entity.SysMenu;
import top.hugo.system.helper.LoginHelper;
import top.hugo.system.service.SysMenuService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    private final SysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public R<List<SysMenu>> list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, LoginHelper.getUserId());
        return R.ok(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     *
     * @param menuId 菜单ID
     */
    @GetMapping(value = "/{menuId}")
    public R<SysMenu> getInfo(@PathVariable Long menuId) {
        return R.ok(menuService.selectMenuById(menuId));
    }


    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public R<List<Tree<Long>>> treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, LoginHelper.getUserId());
        return R.ok(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     *
     * @param roleId 角色ID
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public R<Map<String, Object>> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysMenu> menus = menuService.selectMenuList(LoginHelper.getUserId());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return R.ok(ajax);
    }

    /**
     * 新增菜单
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toAjax(menuService.insertMenu(menu));
    }


    /**
     * 修改菜单
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    @DeleteMapping("/{menuId}")
    public R<Void> remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return R.warn("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return R.warn("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}
