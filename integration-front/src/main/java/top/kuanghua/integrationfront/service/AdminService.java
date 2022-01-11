package top.kuanghua.integrationfront.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.integrationfront.entity.Admin;
import top.kuanghua.integrationfront.mapper.AdminMapper;

import javax.annotation.Resource;

/**
 * (Admin)
 *
 * @author kuanghua
 * @since 2020-10-27 20:54:24
 */
@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    public Page selectPage(Integer pageNum, Integer pageSize,
                           Admin admin) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>(admin);
        return this.adminMapper.selectPage(new Page<Admin>(pageNum, pageSize), queryWrapper);
    }

    public int insert(Admin admin) {
        return this.adminMapper.insert(admin);
    }

    public int updateById(Admin admin) {
        return this.adminMapper.updateById(admin);
    }


    //


}
