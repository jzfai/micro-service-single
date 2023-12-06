package top.hugo.demo.mapper;

import top.hugo.db.mapper.BaseMapperPlus;
import top.hugo.demo.entity.SysPlatform;
import top.hugo.demo.vo.SysPlatformVo;

import java.util.List;

/**
 * 参数配置 数据层
 *
 * @author kuanghua
 */
public interface SysPlatformMapper extends BaseMapperPlus<SysPlatformMapper, SysPlatform, SysPlatformVo> {
    /**
     * 用户xml调用测试
     */
    List<SysPlatform> selectListXml();
}