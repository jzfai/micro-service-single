package top.kuanghua.tyuser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.tyuser.entity.UserRole;
import top.kuanghua.tyuser.mapper.UserRoleMapper;

import javax.annotation.Resource;

/**
 * (UserRole)
 *
 * @author kuanghua
 * @since 2020-10-27 20:54:24
 */
@Service
public class UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    public Page selectPage(Integer pageNum, Integer pageSize,
                           UserRole userRole) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>(userRole);
        return this.userRoleMapper.selectPage(new Page<UserRole>(pageNum, pageSize), queryWrapper);
    }

    public int insert(UserRole userRole) {
        return this.userRoleMapper.insert(userRole);
    }

    public int updateById(UserRole userRole) {
        return this.userRoleMapper.updateById(userRole);
    }

}