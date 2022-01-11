package top.kuanghua.integrationfront.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.integrationfront.entity.ErrorCollection;
import top.kuanghua.integrationfront.mapper.ErrorCollectionMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ErrorCollection)
 *
 * @author kuanghua
 * @since 2021-10-08 11:37:24
 */
@Service
public class ErrorCollectionService {

    @Resource
    private ErrorCollectionMapper errorCollectionMapper;

    public Page<ErrorCollection> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<ErrorCollection> queryWrapper) {
        return this.errorCollectionMapper.selectPage(new Page<ErrorCollection>(pageNum, pageSize), queryWrapper);
    }

    public ErrorCollection selectById(Integer id) {
        return this.errorCollectionMapper.selectById(id);
    }

    public List<ErrorCollection> selectBatchIds(List<Integer> idList) {
        return this.errorCollectionMapper.selectBatchIds(idList);
    }

    public int insert(ErrorCollection errorCollection) {
        return this.errorCollectionMapper.insert(errorCollection);
    }

    public int updateById(ErrorCollection errorCollection) {
        return this.errorCollectionMapper.updateById(errorCollection);
    }

    public int deleteById(Integer id) {
        return this.errorCollectionMapper.deleteById(id);
    }

    public int deleteBatchIds(List<Long> idList) {
        return this.errorCollectionMapper.deleteBatchIds(idList);
    }
}
