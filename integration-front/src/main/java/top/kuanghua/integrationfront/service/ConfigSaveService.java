package top.kuanghua.integrationfront.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import top.kuanghua.integrationfront.entity.ConfigSave;
import top.kuanghua.integrationfront.mapper.ConfigSaveMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代码生成配置保存Service
 *
 * @author 熊猫哥
 * @since 2022-10-07 16:33:13
 */
@Service
public class ConfigSaveService {

    @Resource
    private ConfigSaveMapper configSaveMapper;

    public Page<ConfigSave> selectPage(Integer pageNum, Integer pageSize, QueryWrapper<ConfigSave> queryWrapper) {
        return this.configSaveMapper.selectPage(new Page<ConfigSave>(pageNum, pageSize), queryWrapper);
    }

    public ConfigSave selectById(Integer id) {
        return this.configSaveMapper.selectById(id);
    }

    public List<ConfigSave> selectBatchIds(List<Integer> idList) {
        return this.configSaveMapper.selectBatchIds(idList);
    }

    public int insert(ConfigSave configSave) {
        return this.configSaveMapper.insert(configSave);
    }

    public int updateById(ConfigSave configSave) {
        return this.configSaveMapper.updateById(configSave);
    }

    public int deleteById(Integer id) {
        return this.configSaveMapper.deleteById(id);
    }

    public int deleteBatchIds(List<Integer> idList) {
        return this.configSaveMapper.deleteBatchIds(idList);
    }
}
