package top.kuanghua.integrationfront.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.integrationfront.entity.Role;
import top.kuanghua.integrationfront.mapper.RoleMapper;

import javax.annotation.Resource;

/**
 * (Role)
 *
 * @author kuanghua
 * @since 2020-10-27 20:54:24
 */
@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    public Page selectPage(Integer pageNum, Integer pageSize,
                           Role role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>(role);
        return this.roleMapper.selectPage(new Page<Role>(pageNum, pageSize), queryWrapper);
    }

    public int insert(Role role) {
        return this.roleMapper.insert(role);
    }

    public int updateById(Role role) {
        return this.roleMapper.updateById(role);
    }

}