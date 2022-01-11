package top.kuanghua.integrationfront.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.integrationfront.entity.RoleAccess;
import top.kuanghua.integrationfront.mapper.RoleAccessMapper;

import javax.annotation.Resource;

/**
 * (RoleAccess)
 *
 * @author kuanghua
 * @since 2020-10-27 20:54:24
 */
@Service
public class RoleAccessService {

    @Resource
    private RoleAccessMapper roleAccessMapper;

    public Page selectPage(Integer pageNum, Integer pageSize,
                           RoleAccess roleAccess) {
        QueryWrapper<RoleAccess> queryWrapper = new QueryWrapper<>(roleAccess);
        return this.roleAccessMapper.selectPage(new Page<RoleAccess>(pageNum, pageSize), queryWrapper);
    }

    public int insert(RoleAccess roleAccess) {
        return this.roleAccessMapper.insert(roleAccess);
    }

    public int updateById(RoleAccess roleAccess) {
        return this.roleAccessMapper.updateById(roleAccess);
    }

}