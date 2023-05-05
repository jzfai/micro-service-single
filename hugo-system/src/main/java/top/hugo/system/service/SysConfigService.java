package top.hugo.system.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.hugo.common.constant.CacheNames;
import top.hugo.common.constant.UserConstants;
import top.hugo.common.domain.PageQuery;
import top.hugo.common.exception.ServiceException;
import top.hugo.common.page.TableDataInfo;
import top.hugo.common.utils.StringUtils;
import top.hugo.common.utils.redis.CacheUtils;
import top.hugo.system.mapper.SysConfigMapper;
import top.hugo.system.vo.SysConfig;

import java.util.Arrays;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysConfigService {

    private final SysConfigMapper baseMapper;

    public TableDataInfo<SysConfig> selectPageConfigList(SysConfig config, PageQuery pageQuery) {
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .like(ObjectUtil.isNotEmpty(config.getConfigName()), SysConfig::getConfigName, config.getConfigName())
                .eq(ObjectUtil.isNotEmpty(config.getConfigType()), SysConfig::getConfigType, config.getConfigType())
                .like(ObjectUtil.isNotEmpty(config.getConfigKey()), SysConfig::getConfigKey, config.getConfigKey())
                .between(ObjectUtil.isNotEmpty(config.getBeginTime()) && ObjectUtil.isNotEmpty(config.getEndTime()),
                        SysConfig::getCreateTime, config.getBeginTime(), config.getEndTime());
        Page<SysConfig> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @DS("master")
    public SysConfig selectConfigById(Long configId) {
        return baseMapper.selectById(configId);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Cacheable(cacheNames = CacheNames.SYS_CONFIG, key = "#configKey")
    public String selectConfigByKey(String configKey) {
        SysConfig retConfig = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, configKey));
        if (ObjectUtil.isNotNull(retConfig)) {
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (ObjectUtil.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config) {
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .like(ObjectUtil.isNotEmpty(config.getConfigName()), SysConfig::getConfigName, config.getConfigName())
                .eq(ObjectUtil.isNotEmpty(config.getConfigType()), SysConfig::getConfigType, config.getConfigType())
                .like(ObjectUtil.isNotEmpty(config.getConfigKey()), SysConfig::getConfigKey, config.getConfigKey())
                .between(ObjectUtil.isNotEmpty(config.getBeginTime()) && ObjectUtil.isNotEmpty(config.getEndTime()),
                        SysConfig::getCreateTime, config.getBeginTime(), config.getEndTime());
        return baseMapper.selectList(lqw);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#config.configKey")
    public String insertConfig(SysConfig config) {
        int row = baseMapper.insert(config);
        if (row > 0) {
            return config.getConfigValue();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#config.configKey")
    public String updateConfig(SysConfig config) {
        int row = 0;
        if (config.getConfigId() != null) {
            SysConfig temp = baseMapper.selectById(config.getConfigId());
            if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey())) {
                CacheUtils.evict(CacheNames.SYS_CONFIG, temp.getConfigKey());
            }
            row = baseMapper.updateById(config);
        } else {
            row = baseMapper.update(config, new LambdaQueryWrapper<SysConfig>()
                    .eq(SysConfig::getConfigKey, config.getConfigKey()));
        }
        if (row > 0) {
            return config.getConfigValue();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            CacheUtils.evict(CacheNames.SYS_CONFIG, config.getConfigKey());
        }
        baseMapper.deleteBatchIds(Arrays.asList(configIds));
    }

    /**
     * 加载参数缓存数据
     */
    public void loadingConfigCache() {
        List<SysConfig> configsList = selectConfigList(new SysConfig());
        configsList.forEach(config ->
                CacheUtils.put(CacheNames.SYS_CONFIG, config.getConfigKey(), config.getConfigValue()));
    }

    /**
     * 清空参数缓存数据
     */
    public void clearConfigCache() {
        CacheUtils.clear(CacheNames.SYS_CONFIG);
    }

    /**
     * 重置参数缓存数据
     */
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public String checkConfigKeyUnique(SysConfig config) {
        Long configId = ObjectUtil.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey()));
        if (ObjectUtil.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据参数 key 获取参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    public String getConfigValue(String configKey) {
        return selectConfigByKey(configKey);
    }

}
