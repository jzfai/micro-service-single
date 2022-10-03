package top.kuanghua.basisfunc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.basisfunc.entity.GeneratorConfigSave;
import top.kuanghua.basisfunc.mapper.GeneratorConfigSaveMapper;
import top.kuanghua.commonpom.utils.ObjSelfUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代码生成配置保存Service
 *
 * @author 熊猫哥
 * @since 2022-07-20 10:10:28
 */
@Service
public class GeneratorConfigSaveService {

    @Resource
    private GeneratorConfigSaveMapper generatorConfigSaveMapper;

    public Page<GeneratorConfigSave> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<GeneratorConfigSave> queryWrapper) {
        return this.generatorConfigSaveMapper.selectPage(new Page<GeneratorConfigSave>(pageNum, pageSize), queryWrapper);
    }

    public GeneratorConfigSave selectById(Integer id) {
        return this.generatorConfigSaveMapper.selectById(id);
    }

    public List<GeneratorConfigSave> selectBatchIds(List<Integer> idList) {
        return this.generatorConfigSaveMapper.selectBatchIds(idList);
    }

    public int insert(GeneratorConfigSave generatorConfigSave) {
        QueryWrapper <GeneratorConfigSave> queryWrapper = new QueryWrapper<>();
        //选中的字段配置
        if (ObjSelfUtils.isNotEmpty(generatorConfigSave.getName())) {
            queryWrapper.like("name", generatorConfigSave.getName());
        }
        if(ObjSelfUtils.isNotEmpty(generatorConfigSaveMapper.selectOne(queryWrapper))){
            throw new RuntimeException(generatorConfigSave.getName()+"已存在");
        }
        return this.generatorConfigSaveMapper.insert(generatorConfigSave);
    }

    public int updateById(GeneratorConfigSave generatorConfigSave) {
        return this.generatorConfigSaveMapper.updateById(generatorConfigSave);
    }

    public int deleteById(Integer id) {
        return this.generatorConfigSaveMapper.deleteById(id);
    }

    public int deleteBatchIds(List<Integer> idList) {
        return this.generatorConfigSaveMapper.deleteBatchIds(idList);
    }
}
