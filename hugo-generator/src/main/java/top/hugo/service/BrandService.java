package top.hugo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.hugo.entity.Brand;
import top.hugo.service.mapper.BrandMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * 品牌表Service
 *
 * @author 熊猫哥
 * @since 2022-12-16 14:42:22
 */
@Service
public class BrandService {

    @Resource
    private BrandMapper brandMapper;

    /*
     * 查询列表分页
     * */
    public Page<Brand> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<Brand> queryWrapper) {
        return this.brandMapper.selectPage(new Page<Brand>(pageNum, pageSize), queryWrapper);
    }

    /*
     * 根据id查询明细
     * */
    public Brand selectById(Integer id) {
        return this.brandMapper.selectById(id);
    }
    
    /*
     * 根据批量id查询列表
     * */
    public List<Brand> selectBatchIds(List<Integer> idList) {
        return this.brandMapper.selectBatchIds(idList);
    }

    /*
     * 新增
     * */
    public int insert(Brand brand) {
        this.brandMapper.insert(brand);
        return brand.getId();
    }

    /*
     * 根据id主键更新
     * */
    public int updateById(Brand brand) {
        return this.brandMapper.updateById(brand);
    }

    /*
     * 单个删除
     * */
    public int deleteById(Integer id) {
        return this.brandMapper.deleteById(id);
    }

    /*
     * 批量删除
     * */
    public int deleteBatchIds(List<Integer> idList) {
        return this.brandMapper.deleteBatchIds(idList);
    }
}
