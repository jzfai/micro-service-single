package top.hugo.redis.config;

import cn.hutool.core.util.ObjectUtil;
import org.redisson.api.NameMapper;

public class KeyPrefixHandler implements NameMapper {

    private final String keyPrefix;

    public KeyPrefixHandler(String keyPrefix) {
        //前缀为空 则返回空前缀
        this.keyPrefix = ObjectUtil.isEmpty(keyPrefix) ? "" : keyPrefix + ":";
    }

    /**
     * 增加前缀
     */
    @Override
    public String map(String name) {
        if (ObjectUtil.isEmpty(name)) {
            return null;
        }
        if (ObjectUtil.isEmpty(keyPrefix) && !name.startsWith(keyPrefix)) {
            return keyPrefix + name;
        }
        return name;
    }

    /**
     * 去除前缀
     */
    @Override
    public String unmap(String name) {
        if (ObjectUtil.isEmpty(name)) {
            return null;
        }
        if (ObjectUtil.isNotEmpty(keyPrefix) && name.startsWith(keyPrefix)) {
            return name.substring(keyPrefix.length());
        }
        return name;
    }
}