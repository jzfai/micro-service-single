package top.hugo.satoken.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限管理实现类
 *
 * @author kuanghua
 */
@Component
public class SaPermissionImpl implements StpInterface {
    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("permission:list");
        return arrayList;
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("role:list");
        return arrayList;
    }
}