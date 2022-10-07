package top.kuanghua.integrationfront.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.integrationfront.entity.Role;
import top.kuanghua.integrationfront.mapper.RoleMapper;

import javax.annotation.Resource;
import java.util.List;

/**
*  角色Service
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/
@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    public Page< Role > selectPage(Integer pageNum, Integer pageSize, QueryWrapper< Role > queryWrapper) {
    return this.roleMapper.selectPage(new Page< Role >(pageNum, pageSize), queryWrapper);
    }

    public Role selectById(Long id) {
    return this.roleMapper.selectById(id);
    }

    public List< Role > selectBatchIds(List< Long > idList) {
    return this.roleMapper.selectBatchIds(idList);
    }

    public int insert(Role role) {
    return this.roleMapper.insert(role);
    }

    public int updateById(Role role) {
    return this.roleMapper.updateById(role);
    }

    public int deleteById(Long id) {
    return this.roleMapper.deleteById(id);
    }

    public int deleteBatchIds(List< Long > idList) {
    return this.roleMapper.deleteBatchIds(idList);
    }
}
