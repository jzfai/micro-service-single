### docker-compose安装redis

新建目录  /docker/redis

```shell
mkdir -p /docker/redis
cd /docker/redis
```

>-p 递归创建目录

```yml
cat >/docker/compose/redis.yml<<EOF
version: '3'
services:
  redis:
    image: redis:6.2.7
    container_name: redis
    ports:
      - "6379:6379"
    environment:
      # 时区上海
      TZ: Asia/Shanghai
    volumes:
      # 配置文件
      - /docker/redis/conf:/redis/config
      # 数据文件
      - /docker/redis/data/:/redis/data/
    command: "redis-server /redis/config/redis.conf"
    privileged: true
    network_mode: "host"
EOF
```

```shell
docker-compose -f /docker/compose/redis.yml up -d redis
```

```shell
chmod -R  777 /docker/redis
```



redis配置

```conf
cat >/docker/redis/conf/redis.conf <<EOF
# redis 密码
requirepass admin
# key 监听器配置
# notify-keyspace-events Ex
#绑定的主机地址
bind 0.0.0.0
#监听端口
port 6379
# 配置持久化文件存储路径
dir /redis/data
# 配置rdb
# 15分钟内有至少1个key被更改则进行快照
save 900 1
# 5分钟内有至少10个key被更改则进行快照
save 300 10
# 1分钟内有至少10000个key被更改则进行快照
save 60 10000
# 开启压缩
rdbcompression yes
# rdb文件名 用默认的即可
dbfilename dump.rdb

# 开启aof
appendonly yes
# 文件名
appendfilename "appendonly.aof"
# 持久化策略,no:不同步,everysec:每秒一次,always:总是同步,速度比较慢
# appendfsync always
appendfsync everysec
# appendfsync no
EOF
```

>requirepass admin
>
>开启redis登录权限

重启redis

```shell
docker container restart redis
```

>注：需要开启端口 6379



## springboot集成redis

#### 集成redis

```xml
<!--redis和redisson集成-->
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.19.1</version>
</dependency>

<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-data-27</artifactId>
    <version>3.19.1</version>
</dependency>

<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>lock4j-redisson-spring-boot-starter</artifactId>
    <version>2.2.3</version>
</dependency>
```

application-dev.yml

```yml
--- # redis 单机配置(单机与集群只能开启一个另一个需要注释掉)
spring:
  redis:
    host: xxxx
    # 端口，默认为6379
    port: 6379
    # 数据库索引
    database: 2
    # 密码(如没有密码请注释掉)
    password: admin
    # 是否开启ssl
    ssl: false

redisson:
  # redis key前缀
  keyPrefix: hugo
  # 线程池数量
  threads: 2
  # Netty线程池数量
  nettyThreads: 4
  # 单节点配置
  singleServerConfig:
    # 客户端名称
    clientName: ${hugo.name}
    # 最小空闲连接数
    connectionMinimumIdleSize: 4
    # 连接池大小
    connectionPoolSize: 16
    # 连接空闲超时，单位：毫秒
    idleConnectionTimeout: 10000
    # 命令等待超时，单位：毫秒
    timeout: 3000
    # 发布和订阅连接池大小
    subscriptionConnectionPoolSize: 20
```

RedisConfig

```java
package top.hugo.framework.config;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.hugo.framework.config.properties.RedissonProperties;
import top.hugo.framework.handler.KeyPrefixHandler;
import top.hugo.framework.manager.PlusSpringCacheManager;

import javax.annotation.Resource;

/**
 * redis配置
 *
 * @author kuanghua
 */
@Slf4j
@Configuration
@EnableCaching
@EnableConfigurationProperties(RedissonConfigProperties.class)
public class RedisConfig {

    @Autowired
    private RedissonConfigProperties redissonProperties;

    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return config -> {
            config.setThreads(redissonProperties.getThreads())
                    .setNettyThreads(redissonProperties.getNettyThreads())
                    //设置格式化设置为jackson
                    .setCodec(new JsonJacksonCodec(objectMapper));
            RedissonConfigProperties.SingleServerConfig singleServerConfig = redissonProperties.getSingleServerConfig();
            // 使用单机模式
            if (ObjectUtil.isNotNull(singleServerConfig)) {
                config.useSingleServer()
                        //设置redis key前缀
                        .setNameMapper(new KeyPrefixHandler(redissonProperties.getKeyPrefix()))
                        .setTimeout(singleServerConfig.getTimeout())
                        .setClientName(singleServerConfig.getClientName())
                        .setIdleConnectionTimeout(singleServerConfig.getIdleConnectionTimeout())
                        .setSubscriptionConnectionPoolSize(singleServerConfig.getSubscriptionConnectionPoolSize())
                        .setConnectionMinimumIdleSize(singleServerConfig.getConnectionMinimumIdleSize())
                        .setConnectionPoolSize(singleServerConfig.getConnectionPoolSize());
            }
            log.info("初始化 redis 配置");
        };
    }

    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    public CacheManager cacheManager() {
        return new PlusSpringCacheManager();
    }
}
```



import top.hugo.common.utils.RedisUtils;

```java
package top.hugo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.config.Config;
import top.hugo.common.spring.SpringUtils;
import org.redisson.api.*;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * redis 工具类
 *
 * @author kuanghua
 * @version 3.1.0 新增
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisUtils {

    private static final RedissonClient CLIENT = SpringUtil.getBean(RedissonClient.class);

    public static NameMapper getNameMapper() {
        Config config = CLIENT.getConfig();
        if (config.isClusterConfig()) {
            return config.useClusterServers().getNameMapper();
        }
        return config.useSingleServer().getNameMapper();
    }

    /**
     * 限流
     *
     * @param key          限流key
     * @param rateType     限流类型
     * @param rate         速率
     * @param rateInterval 速率间隔
     * @return -1 表示失败
     */
    public static long rateLimiter(String key, RateType rateType, int rate, int rateInterval) {
        RRateLimiter rateLimiter = CLIENT.getRateLimiter(key);
        rateLimiter.trySetRate(rateType, rate, rateInterval, RateIntervalUnit.SECONDS);
        if (rateLimiter.tryAcquire()) {
            return rateLimiter.availablePermits();
        } else {
            return -1L;
        }
    }

    /**
     * 获取客户端实例
     */
    public static RedissonClient getClient() {
        return CLIENT;
    }

    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param consumer   自定义处理
     */
    public static <T> void publish(String channelKey, T msg, Consumer<T> consumer) {
        RTopic topic = CLIENT.getTopic(channelKey);
        topic.publish(msg);
        consumer.accept(msg);
    }

    public static <T> void publish(String channelKey, T msg) {
        RTopic topic = CLIENT.getTopic(channelKey);
        topic.publish(msg);
    }

    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道key
     * @param clazz      消息类型
     * @param consumer   自定义处理
     */
    public static <T> void subscribe(String channelKey, Class<T> clazz, Consumer<T> consumer) {
        RTopic topic = CLIENT.getTopic(channelKey);
        topic.addListener(clazz, (channel, msg) -> consumer.accept(msg));
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setCacheObject(final String key, final T value) {
        setCacheObject(key, value, false);
    }

    /**
     * 缓存基本的对象，保留当前对象 TTL 有效期
     *
     * @param key       缓存的键值
     * @param value     缓存的值
     * @param isSaveTtl 是否保留TTL有效期(例如: set之前ttl剩余90 set之后还是为90)
     * @since Redis 6.X 以上使用 setAndKeepTTL 兼容 5.X 方案
     */
    public static <T> void setCacheObject(final String key, final T value, final boolean isSaveTtl) {
        RBucket<T> bucket = CLIENT.getBucket(key);
        if (isSaveTtl) {
            try {
                bucket.setAndKeepTTL(value);
            } catch (Exception e) {
                long timeToLive = bucket.remainTimeToLive();
                setCacheObject(key, value, Duration.ofMillis(timeToLive));
            }
        } else {
            bucket.set(value);
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public static <T> void setCacheObject(final String key, final T value, final Duration duration) {
        RBatch batch = CLIENT.createBatch();
        RBucketAsync<T> bucket = batch.getBucket(key);
        bucket.setAsync(value);
        bucket.expireAsync(duration);
        batch.execute();
    }

    /**
     * 注册对象监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addObjectListener(final String key, final ObjectListener listener) {
        RBucket<T> result = CLIENT.getBucket(key);
        result.addListener(listener);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout) {
        return expire(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置有效时间
     *
     * @param key      Redis键
     * @param duration 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final Duration duration) {
        RBucket rBucket = CLIENT.getBucket(key);
        return rBucket.expire(duration);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheObject(final String key) {
        RBucket<T> rBucket = CLIENT.getBucket(key);
        return rBucket.get();
    }

    /**
     * 获得key剩余存活时间
     *
     * @param key 缓存键值
     * @return 剩余存活时间
     */
    public static <T> long getTimeToLive(final String key) {
        RBucket<T> rBucket = CLIENT.getBucket(key);
        return rBucket.remainTimeToLive();
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    public static boolean deleteObject(final String key) {
        return CLIENT.getBucket(key).delete();
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    public static void deleteObject(final Collection collection) {
        RBatch batch = CLIENT.createBatch();
        collection.forEach(t -> {
            batch.getBucket(t.toString()).deleteAsync();
        });
        batch.execute();
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public static <T> boolean setCacheList(final String key, final List<T> dataList) {
        RList<T> rList = CLIENT.getList(key);
        return rList.addAll(dataList);
    }

    /**
     * 注册List监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addListListener(final String key, final ObjectListener listener) {
        RList<T> rList = CLIENT.getList(key);
        rList.addListener(listener);
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public static <T> List<T> getCacheList(final String key) {
        RList<T> rList = CLIENT.getList(key);
        return rList.readAll();
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public static <T> boolean setCacheSet(final String key, final Set<T> dataSet) {
        RSet<T> rSet = CLIENT.getSet(key);
        return rSet.addAll(dataSet);
    }

    /**
     * 注册Set监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addSetListener(final String key, final ObjectListener listener) {
        RSet<T> rSet = CLIENT.getSet(key);
        rSet.addListener(listener);
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存的key
     * @return set对象
     */
    public static <T> Set<T> getCacheSet(final String key) {
        RSet<T> rSet = CLIENT.getSet(key);
        return rSet.readAll();
    }

    /**
     * 缓存Map
     *
     * @param key     缓存的键值
     * @param dataMap 缓存的数据
     */
    public static <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            RMap<String, T> rMap = CLIENT.getMap(key);
            rMap.putAll(dataMap);
        }
    }

    /**
     * 注册Map监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addMapListener(final String key, final ObjectListener listener) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        rMap.addListener(listener);
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    public static <T> Map<String, T> getCacheMap(final String key) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.getAll(rMap.keySet());
    }

    /**
     * 获得缓存Map的key列表
     *
     * @param key 缓存的键值
     * @return key列表
     */
    public static <T> Set<String> getCacheMapKeySet(final String key) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.keySet();
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public static <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        rMap.put(hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static <T> T getCacheMapValue(final String key, final String hKey) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.get(hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static <T> T delCacheMapValue(final String key, final String hKey) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.remove(hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static <K, V> Map<K, V> getMultiCacheMapValue(final String key, final Set<K> hKeys) {
        RMap<K, V> rMap = CLIENT.getMap(key);
        return rMap.getAll(hKeys);
    }

    /**
     * 设置原子值
     *
     * @param key   Redis键
     * @param value 值
     */
    public static void setAtomicValue(String key, long value) {
        RAtomicLong atomic = CLIENT.getAtomicLong(key);
        atomic.set(value);
    }

    /**
     * 获取原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long getAtomicValue(String key) {
        RAtomicLong atomic = CLIENT.getAtomicLong(key);
        return atomic.get();
    }

    /**
     * 递增原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long incrAtomicValue(String key) {
        RAtomicLong atomic = CLIENT.getAtomicLong(key);
        return atomic.incrementAndGet();
    }

    /**
     * 递减原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long decrAtomicValue(String key) {
        RAtomicLong atomic = CLIENT.getAtomicLong(key);
        return atomic.decrementAndGet();
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public static Collection<String> keys(final String pattern) {
        Stream<String> stream = CLIENT.getKeys().getKeysStreamByPattern(getNameMapper().map(pattern));
        return stream.map(key -> getNameMapper().unmap(key)).collect(Collectors.toList());
    }

    /**
     * 删除缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     */
    public static void deleteKeys(final String pattern) {
        CLIENT.getKeys().deleteByPattern(getNameMapper().map(pattern));
    }

    /**
     * 检查redis中是否存在key
     *
     * @param key 键
     */
    public static Boolean hasKey(String key) {
        RKeys rKeys = CLIENT.getKeys();
        return rKeys.countExists(getNameMapper().map(key)) > 0;
    }
}
```

properties

```java
package top.hugo.framework.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
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
public class RedissonConfigProperties {

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

    /**
     * 集群服务配置
     */
    private ClusterServersConfig clusterServersConfig;

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

    @Data
    @NoArgsConstructor
    public static class ClusterServersConfig {

        /**
         * 客户端名称
         */
        private String clientName;

        /**
         * master最小空闲连接数
         */
        private int masterConnectionMinimumIdleSize;

        /**
         * master连接池大小
         */
        private int masterConnectionPoolSize;

        /**
         * slave最小空闲连接数
         */
        private int slaveConnectionMinimumIdleSize;

        /**
         * slave连接池大小
         */
        private int slaveConnectionPoolSize;

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

        /**
         * 读取模式
         */
        private ReadMode readMode;

        /**
         * 订阅模式
         */
        private SubscriptionMode subscriptionMode;

    }

}
```

redis缓存器
top.hugo.framework.manager.PlusSpringCacheManager

```java
/**
 * Copyright (c) 2013-2021 Nikita Koksharov
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.hugo.framework.manager;

import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.util.StringUtils;
import top.hugo.common.utils.RedisUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A {@link org.springframework.cache.CacheManager} implementation
 * backed by Redisson instance.
 * <p>
 * 修改 RedissonSpringCacheManager 源码
 * 重写 cacheName 处理方法 支持多参数
 *
 * @author Nikita Koksharov
 *
 */
@SuppressWarnings("unchecked")
public class PlusSpringCacheManager implements CacheManager {

    Map<String, CacheConfig> configMap = new ConcurrentHashMap<>();
    ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<>();
    private boolean dynamic = true;
    private boolean allowNullValues = true;
    private boolean transactionAware = true;

    /**
     * Creates CacheManager supplied by Redisson instance
     */
    public PlusSpringCacheManager() {
    }


    /**
     * Defines possibility of storing {@code null} values.
     * <p>
     * Default is <code>true</code>
     *
     * @param allowNullValues stores if <code>true</code>
     */
    public void setAllowNullValues(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }

    /**
     * Defines if cache aware of Spring-managed transactions.
     * If {@code true} put/evict operations are executed only for successful transaction in after-commit phase.
     * <p>
     * Default is <code>false</code>
     *
     * @param transactionAware cache is transaction aware if <code>true</code>
     */
    public void setTransactionAware(boolean transactionAware) {
        this.transactionAware = transactionAware;
    }

    /**
     * Set cache config mapped by cache name
     *
     * @param config object
     */
    public void setConfig(Map<String, ? extends CacheConfig> config) {
        this.configMap = (Map<String, CacheConfig>) config;
    }

    protected CacheConfig createDefaultConfig() {
        return new CacheConfig();
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = instanceMap.get(name);
        if (cache != null) {
            return cache;
        }
        if (!dynamic) {
            return cache;
        }

        CacheConfig config = configMap.get(name);
        if (config == null) {
            config = createDefaultConfig();
            configMap.put(name, config);
        }

        // 重写 cacheName 支持多参数
        String[] array = StringUtils.delimitedListToStringArray(name, "#");
        name = array[0];
        if (array.length > 1) {
            config.setTTL(DurationStyle.detectAndParse(array[1]).toMillis());
        }
        if (array.length > 2) {
            config.setMaxIdleTime(DurationStyle.detectAndParse(array[2]).toMillis());
        }
        if (array.length > 3) {
            config.setMaxSize(Integer.parseInt(array[3]));
        }

        if (config.getMaxIdleTime() == 0 && config.getTTL() == 0 && config.getMaxSize() == 0) {
            return createMap(name, config);
        }

        return createMapCache(name, config);
    }

    private Cache createMap(String name, CacheConfig config) {
        RMap<Object, Object> map = RedisUtils.getClient().getMap(name);

        Cache cache = new RedissonCache(map, allowNullValues);
        if (transactionAware) {
            cache = new TransactionAwareCacheDecorator(cache);
        }
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        }
        return cache;
    }

    private Cache createMapCache(String name, CacheConfig config) {
        RMapCache<Object, Object> map = RedisUtils.getClient().getMapCache(name);

        Cache cache = new RedissonCache(map, config, allowNullValues);
        if (transactionAware) {
            cache = new TransactionAwareCacheDecorator(cache);
        }
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        } else {
            map.setMaxSize(config.getMaxSize());
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(configMap.keySet());
    }

    /**
     * Defines 'fixed' cache names.
     * A new cache instance will not be created in dynamic for non-defined names.
     * <p>
     * `null` parameter setups dynamic mode
     *
     * @param names of caches
     */
    public void setCacheNames(Collection<String> names) {
        if (names != null) {
            for (String name : names) {
                getCache(name);
            }
            dynamic = false;
        } else {
            dynamic = true;
        }
    }
}
```

import top.hugo.framework.handler.KeyPrefixHandler;

```java
package top.hugo.framework.handler;

import cn.hutool.core.util.ObjectUtil;
import org.redisson.api.NameMapper;

/**
 * redis缓存key前缀处理
 *
 * @author ye
 * @date 2022/7/14 17:44
 * @since 4.3.0
 */
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
        if (ObjectUtil.isNotEmpty(keyPrefix) && !name.startsWith(keyPrefix)) {
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
```

top.hugo.common.spring.SpringUtils



