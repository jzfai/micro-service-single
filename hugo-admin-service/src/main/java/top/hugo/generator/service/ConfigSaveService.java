package top.hugo.generator.service;

import cn.hutool.core.util.ObjectUtil;
import top.hugo.domain.TableDataInfo;
import top.hugo.generator.entity.ConfigSave;
import top.hugo.generator.mapper.ConfigSaveMapper;
import top.hugo.generator.query.ConfigSaveQuery;
import top.hugo.generator.vo.ConfigSaveVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 代码生成配置保存
 *
 * @author kuanghua
 */
@RequiredArgsConstructor
@Service
public class ConfigSaveService {
    private final ConfigSaveMapper configSaveMapper;

    public TableDataInfo<ConfigSaveVo> selectPageConfigSaveList(ConfigSaveQuery configSaveQuery) {
        LambdaQueryWrapper<ConfigSave> lqw = getQueryWrapper(configSaveQuery);
        lqw.select(ConfigSave::getId,ConfigSave::getUpdateBy,ConfigSave::getCreateBy,ConfigSave::getName);
        IPage<ConfigSaveVo> page = configSaveMapper.selectVoPage(configSaveQuery.build(), lqw);
        return TableDataInfo.build(page);
    }
    

    /**
     * 查询代码生成配置保存分页
     *
     * @return
     */
    public List<ConfigSaveVo> selectConfigSaveList(ConfigSaveQuery configSaveQuery) {
        LambdaQueryWrapper<ConfigSave> lqw = getQueryWrapper(configSaveQuery);
        return configSaveMapper.selectVoList(lqw);
    }


    /*查询条件抽取*/
    private static LambdaQueryWrapper<ConfigSave> getQueryWrapper(ConfigSaveQuery configSaveQuery) {
        LambdaQueryWrapper<ConfigSave> lqw = new LambdaQueryWrapper<>();
        lqw.like(ObjectUtil.isNotEmpty(configSaveQuery.getName()), ConfigSave::getName, configSaveQuery.getName());
//        lqw.eq(!LoginHelper.isAdmin(), ConfigSave::getCreateBy, LoginHelper.getUsername());
        lqw.orderByDesc(ConfigSave::getCreateTime).orderByDesc(ConfigSave::getUpdateTime);
        return lqw;
    }

    /**
     * 查询所有平台
     *
     * @return 平台列表
     */
    public List<ConfigSaveVo> selectConfigSaveAll() {
        return configSaveMapper.selectVoList();
    }

    /**
     * 通过代码生成配置保存ID查询
     *
     * @param configSaveId 代码生成配置保存ID
     * @return 角色对象信息
     */

    public ConfigSaveVo selectConfigSaveById(Long configSaveId) {
        return configSaveMapper.selectVoById(configSaveId);
    }


    /**
     * 删除代码生成配置保存
     *
     * @param configSaveId 平台ID
     * @return 结果
     */

    public int deleteConfigSaveById(Long configSaveId) {
        return configSaveMapper.deleteById(configSaveId);
    }

    /**
     * 批量删除代码生成配置保存
     *
     * @param configSaveIds 需要删除的平台ID
     * @return 结果
     */
    public int deleteConfigSaveByIds(Long[] configSaveIds) {
        return configSaveMapper.deleteBatchIds(Arrays.asList(configSaveIds));
    }

    /**
     * 新增保存代码生成配置保存
     *
     * @param configSave 平台信息
     * @return 结果
     */

    public int insertConfigSave(ConfigSave configSave) {
        return configSaveMapper.insert(configSave);
    }


    /**
     * 修改保存代码生成配置保存
     *
     * @param configSave
     * @return 结果
     */

    public int updateConfigSave(ConfigSave configSave) {
        return configSaveMapper.updateById(configSave);
    }

}