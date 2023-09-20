package top.hugo.system.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.hugo.common.helper.LoginHelper;
import top.hugo.system.entity.SysMenu;
import top.hugo.system.mapper.RbacMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class PermissionService {
    private final RbacMapper rbacMapper;

    public Set<String> getMenuPermission(int platformId) {
        Set<String> perms = new HashSet<String>();
        if (LoginHelper.isAdmin()) {
            perms.add("*:*:*");
        } else {
            List<SysMenu> sysMenus = rbacMapper.selectMenuByUserId(LoginHelper.getUserId(), platformId);
            sysMenus.forEach(m -> {
                perms.add(m.getPerms());
            });
        }
        return perms;
    }

    public Set<String> getMenuBtnPermission(Integer platformId) {
        Set<String> perms = new HashSet<String>();
        if (LoginHelper.isAdmin()) {
            perms.add("*:*:*");
        } else {
            List<SysMenu> sysMenus = rbacMapper.selectBtnPermByUserId(LoginHelper.getUserId(), platformId);
            sysMenus.forEach(m -> {
                perms.add(m.getPerms());
            });
        }
        return perms;
    }
}
