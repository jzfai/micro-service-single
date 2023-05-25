package top.hugo.generator.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.hugo.generator.entity.ConfigSave;
import top.hugo.generator.mapper.ConfigSaveMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代码生成配置保存Service
 *
 * @author kuanghua
 * @since 2022-07-20 10:10:28
 */
@Service
public class ConfigSaveService {

    @Resource
    private ConfigSaveMapper generatorConfigSaveMapper;

    public Page<ConfigSave> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<ConfigSave> queryWrapper) {
        return this.generatorConfigSaveMapper.selectPage(new Page<ConfigSave>(pageNum, pageSize), queryWrapper);
    }

    public ConfigSave selectById(Integer id) {
        return this.generatorConfigSaveMapper.selectById(id);
    }

    public List<ConfigSave> selectBatchIds(List<Integer> idList) {
        return this.generatorConfigSaveMapper.selectBatchIds(idList);
    }

    public int insert(ConfigSave generatorConfigSave) {
        QueryWrapper<ConfigSave> queryWrapper = new QueryWrapper<>();
        //选中的字段配置
        if (ObjectUtil.isNotEmpty(generatorConfigSave.getName())) {
            queryWrapper.like("name", generatorConfigSave.getName());
        }
        if (ObjectUtil.isNotEmpty(generatorConfigSaveMapper.selectOne(queryWrapper))) {
            throw new RuntimeException(generatorConfigSave.getName() + "已存在");
        }
        return this.generatorConfigSaveMapper.insert(generatorConfigSave);
    }

    public int updateById(ConfigSave generatorConfigSave) {
        return this.generatorConfigSaveMapper.updateById(generatorConfigSave);
    }

    public int deleteById(Integer id) {
        return this.generatorConfigSaveMapper.deleteById(id);
    }

    public int deleteBatchIds(List<Integer> idList) {
        return this.generatorConfigSaveMapper.deleteBatchIds(idList);
    }
}
