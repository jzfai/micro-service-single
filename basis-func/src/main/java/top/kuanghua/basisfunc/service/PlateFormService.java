package top.kuanghua.basisfunc.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.basisfunc.entity.PlateForm;
import top.kuanghua.basisfunc.mapper.PlateFormMapper;

import javax.annotation.Resource;
import java.util.List;

/**
*  平台Service
*
* @author 熊猫哥
* @since 2022-10-07 16:33:13
*/
@Service
public class PlateFormService {
    @Resource
    private PlateFormMapper plateFormMapper;

    public Page< PlateForm > selectPage(Integer pageNum, Integer pageSize, QueryWrapper< PlateForm > queryWrapper) {
    return this.plateFormMapper.selectPage(new Page< PlateForm >(pageNum, pageSize), queryWrapper);
    }

    public PlateForm selectById(Integer id) {
    return this.plateFormMapper.selectById(id);
    }

    public List< PlateForm > selectBatchIds(List< Integer > idList) {
    return this.plateFormMapper.selectBatchIds(idList);
    }

    public int insert(PlateForm plateForm) {
    return this.plateFormMapper.insert(plateForm);
    }

    public int updateById(PlateForm plateForm) {
    return this.plateFormMapper.updateById(plateForm);
    }

    public int deleteById(Integer id) {
    return this.plateFormMapper.deleteById(id);
    }

    public int deleteBatchIds(List< Integer > idList) {
    return this.plateFormMapper.deleteBatchIds(idList);
    }
}
