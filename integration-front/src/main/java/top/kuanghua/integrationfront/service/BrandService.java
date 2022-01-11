package top.kuanghua.integrationfront.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.integrationfront.entity.Brand;
import top.kuanghua.integrationfront.mapper.BrandMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Brand)
 *
 * @author kuanghua
 * @since 2021-09-15 11:54:48
 */
@Service
public class BrandService {

    @Resource
    private BrandMapper brandMapper;

    public Page<Brand> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<Brand> queryWrapper) {
        return this.brandMapper.selectPage(new Page<Brand>(pageNum, pageSize), queryWrapper);
    }

    public Brand selectById(Integer id) {
        return this.brandMapper.selectById(id);
    }

    public List<Brand> selectBatchIds(List<Integer> idList) {
        return this.brandMapper.selectBatchIds(idList);
    }

    public int insert(Brand brand) {
        return this.brandMapper.insert(brand);
    }

    public int updateById(Brand brand) {
        return this.brandMapper.updateById(brand);
    }

    public int deleteById(Integer id) {
        return this.brandMapper.deleteById(id);
    }

    public int deleteBatchIds(List<Long> idList) {
        return this.brandMapper.deleteBatchIds(idList);
    }
}
