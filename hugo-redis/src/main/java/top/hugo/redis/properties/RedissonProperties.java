package top.hugo.redis.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redisson 配置属性
 *
 * @author kuanghua
 */
@Data
@Component
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {

    /**
     * redis缓存key前缀
     */
    private String keyPrefix;

    /**
     * 线程池数量,默认值 = 当前处理核数量 * 2
     */
    private int threads;

    /**
     * Netty线程池数量,默认值 = 当前处理核数量 * 2
     */
    private int nettyThreads;

    /**
     * 单机服务配置
     */
    private SingleServerConfig singleServerConfig;

    @Data
    @NoArgsConstructor
    public static class SingleServerConfig {

        /**
         * 客户端名称
         */
        private String clientName;

        /**
         * 最小空闲连接数
         */
        private int connectionMinimumIdleSize;

        /**
         * 连接池大小
         */
        private int connectionPoolSize;

        /**
         * 连接空闲超时，单位：毫秒
         */
        private int idleConnectionTimeout;

        /**
         * 命令等待超时，单位：毫秒
         */
        private int timeout;

        /**
         * 发布和订阅连接池大小
         */
        private int subscriptionConnectionPoolSize;
    }
}