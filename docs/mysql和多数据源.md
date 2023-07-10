### mysql和多数据源

```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>
        <!-- Mysql驱动包 -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency>
        <!-- dynamic-datasource 多数据源-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        </dependency>
```

配置

application.yml

```yml
# MyBatisPlus配置
# https://baomidou.com/config/
mybatis-plus:
  # 不支持多包, 如有需要可在注解配置 或 提升扫包等级
  # 例如 com.**.**.mapper
  mapperPackage: top.hugo.**.mapper
  # 对应的 XML 文件位置
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  # 实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage: top.hugo.**.domain,top.hugo.**.entity,top.hugo.**.vo,top.hugo.**.bo
  # 启动时是否检查 MyBatis XML 文件的存在，默认不检查
  checkConfigLocation: false
  configuration:
    # 自动驼峰命名规则（camel case）映射
    mapUnderscoreToCamelCase: true
    # MyBatis 自动映射策略
    # NONE：不启用 PARTIAL：只对非嵌套 resultMap 自动映射 FULL：对所有 resultMap 自动映射
    autoMappingBehavior: PARTIAL
    # MyBatis 自动映射时未知列或未知属性处理策
    # NONE：不做处理 WARNING：打印相关警告 FAILING：抛出异常和详细信息
    autoMappingUnknownColumnBehavior: NONE
    # 更详细的日志输出 会有性能损耗 org.apache.ibatis.logging.stdout.StdOutImpl
    # 关闭日志记录 (可单纯使用 p6spy 分析) org.apache.ibatis.logging.nologging.NoLoggingImpl
    # 默认日志输出 org.apache.ibatis.logging.slf4j.Slf4jImpl
    logImpl: org.apache.ibatis.logging.nologging.NoLoggingImpl
  global-config:
    # 是否打印 Logo banner
    banner: true
    dbConfig:
      # 主键类型
      # AUTO 自增 NONE 空 INPUT 用户输入 ASSIGN_ID 雪花 ASSIGN_UUID 唯一 UUID
      idType: NONE
      # 逻辑已删除值
      logicDeleteValue: 2
      # 逻辑未删除值
      logicNotDeleteValue: 0
      # 字段验证策略之 insert,在 insert 的时候的字段验证策略
      # IGNORED 忽略 NOT_NULL 非NULL NOT_EMPTY 非空 DEFAULT 默认 NEVER 不加入 SQL
      insertStrategy: NOT_NULL
      # 字段验证策略之 update,在 update 的时候的字段验证策略
      updateStrategy: NOT_NULL
      # 字段验证策略之 select,在 select 的时候的字段验证策略既 wrapper 根据内部 entity 生成的 where 条件
      where-strategy: NOT_NULL
```





application-dev.yml

```yml
--- # 数据源配置
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    # 动态数据源文档 https://www.kancloud.cn/tracy5546/dynamic-datasource/content
    dynamic:
      # 性能分析插件(有性能损耗 不建议生产环境使用)
      p6spy: true
      # 设置默认的数据源或者数据源组,默认值即为 master
      primary: master
      # 严格模式 匹配不到数据源则报错
      strict: true
      datasource:
        # 主库数据源
        master:
          type: ${spring.datasource.type}
          driverClassName: com.mysql.cj.jdbc.Driver
          # jdbc 所有参数配置参考 https://lionli.blog.csdn.net/article/details/122018562
          # rewriteBatchedStatements=true 批处理优化 大幅提升批量插入更新删除性能(对数据库有性能损耗 使用批量操作应考虑性能问题)
          url: jdbc:mysql://111.230.198.245:3310/micro-service-single?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true
          username: root
          password: '@Root123'
        # 从库数据源
        #  slave:
        #    lazy: true
        #    type: ${spring.datasource.type}
        #    driverClassName: com.mysql.cj.jdbc.Driver
        #    url: jdbc:mysql://111.230.198.245:3310/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&autoReconnect=true&rewriteBatchedStatements=true
        #    username:
        #    password:
      hikari:
        # 最大连接池数量
        maxPoolSize: 20
        # 最小空闲线程数量
        minIdle: 10
        # 配置获取连接等待超时的时间
        connectionTimeout: 10000
        # 校验超时时间
        validationTimeout: 5000
        # 空闲连接存活最大时间，默认10分钟
        idleTimeout: 60000
        # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认30分钟
        maxLifetime: 900000
        # 连接测试query（配置检测连接是否有效）
        connectionTestQuery: SELECT 1
```

spy.properties

```properties
# p6spy 性能分析插件配置文件
modulelist=com.baomidou.mybatisplus.extension.p6spy.MybatisPlusLogFactory,com.p6spy.engine.outage.P6OutageFactory
# 自定义日志打印
logMessageFormat=com.baomidou.mybatisplus.extension.p6spy.P6SpyLogger
#日志输出到控制台
appender=com.baomidou.mybatisplus.extension.p6spy.StdoutLogger
# 使用日志系统记录 sql
#appender=com.p6spy.engine.spy.appender.Slf4JLogger
# 设置 p6spy driver 代理
#deregisterdrivers=true
# 取消JDBC URL前缀
useprefix=true
# 配置记录 Log 例外,可去掉的结果集有error,info,batch,debug,statement,commit,rollback,result,resultset.
excludecategories=info,debug,result,commit,resultset
# 日期格式
dateformat=yyyy-MM-dd HH:mm:ss
# SQL语句打印时间格式
databaseDialectTimestampFormat=yyyy-MM-dd HH:mm:ss
# 实际驱动可多个
#driverlist=org.h2.Driver
# 是否开启慢SQL记录
outagedetection=true
# 慢SQL记录标准 2 秒
outagedetectioninterval=2
# 是否过滤 Log
filter=true
# 过滤 Log 时所排除的 sql 关键字，以逗号分隔
exclude=SELECT 1
```



top.hugo.framework.config.MybatisPlusConfig

```java
package top.hugo.framework.config;

import cn.hutool.core.net.NetUtil;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.hugo.framework.handler.CreateAndUpdateMetaObjectHandler;

/**
 * mybatis-plus配置类(下方注释有插件介绍)
 *
 * @author kuanghua
 */
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
@MapperScan("${mybatis-plus.mapperPackage}")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 数据权限处理
        //interceptor.addInnerInterceptor(dataPermissionInterceptor());
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 分页插件，自动识别数据库类型
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(-1L);
        // 分页合理化
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

    /**
     * 乐观锁插件
     */
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * 元对象字段填充控制器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CreateAndUpdateMetaObjectHandler();
    }

    /**
     * 使用网卡信息绑定雪花生成器
     * 防止集群雪花ID重复
     */
    @Bean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator(NetUtil.getLocalhost());
    }
    //返回的map转驼峰
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(MybatisConfiguration configuration) {
                configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
            }
        };
    }
}
```



top.hugo.common.mapper.BaseMapperPlus

```java
package top.hugo.common.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import top.hugo.common.utils.BeanCopyUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 自定义 Mapper 接口, 实现 自定义扩展
 *
 * @param <M> mapper 泛型
 * @param <T> table 泛型
 * @param <V> vo 泛型
 * @since 2021-05-13
 */
@SuppressWarnings("unchecked")
public interface BaseMapperPlus<M, T, V> extends BaseMapper<T> {

    Log log = LogFactory.getLog(BaseMapperPlus.class);

    default Class<V> currentVoClass() {
        return (Class<V>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperPlus.class, 2);
    }

    default Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperPlus.class, 1);
    }

    default Class<M> currentMapperClass() {
        return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperPlus.class, 0);
    }

    default List<T> selectList() {
        return this.selectList(new QueryWrapper<>());
    }

    /**
     * 批量插入
     */
    default boolean insertBatch(Collection<T> entityList) {
        return Db.saveBatch(entityList);
    }

    /**
     * 批量更新
     */
    default boolean updateBatchById(Collection<T> entityList) {
        return Db.updateBatchById(entityList);
    }

    /**
     * 批量插入或更新
     */
    default boolean insertOrUpdateBatch(Collection<T> entityList) {
        return Db.saveOrUpdateBatch(entityList);
    }

    /**
     * 批量插入(包含限制条数)
     */
    default boolean insertBatch(Collection<T> entityList, int batchSize) {
        return Db.saveBatch(entityList, batchSize);
    }

    /**
     * 批量更新(包含限制条数)
     */
    default boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return Db.updateBatchById(entityList, batchSize);
    }

    /**
     * 批量插入或更新(包含限制条数)
     */
    default boolean insertOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return Db.saveOrUpdateBatch(entityList, batchSize);
    }

    /**
     * 插入或更新(包含限制条数)
     */
    default boolean insertOrUpdate(T entity) {
        return Db.saveOrUpdate(entity);
    }

    default V selectVoById(Serializable id) {
        return selectVoById(id, this.currentVoClass());
    }

    /**
     * 根据 ID 查询
     */
    default <C> C selectVoById(Serializable id, Class<C> voClass) {
        T obj = this.selectById(id);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return BeanCopyUtils.copy(obj, voClass);
    }

    default List<V> selectVoBatchIds(Collection<? extends Serializable> idList) {
        return selectVoBatchIds(idList, this.currentVoClass());
    }

    /**
     * 查询（根据ID 批量查询）
     */
    default <C> List<C> selectVoBatchIds(Collection<? extends Serializable> idList, Class<C> voClass) {
        List<T> list = this.selectBatchIds(idList);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanCopyUtils.copyList(list, voClass);
    }

    default List<V> selectVoByMap(Map<String, Object> map) {
        return selectVoByMap(map, this.currentVoClass());
    }

    /**
     * 查询（根据 columnMap 条件）
     */
    default <C> List<C> selectVoByMap(Map<String, Object> map, Class<C> voClass) {
        List<T> list = this.selectByMap(map);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanCopyUtils.copyList(list, voClass);
    }

    default V selectVoOne(Wrapper<T> wrapper) {
        return selectVoOne(wrapper, this.currentVoClass());
    }

    /**
     * 根据 entity 条件，查询一条记录
     */
    default <C> C selectVoOne(Wrapper<T> wrapper, Class<C> voClass) {
        T obj = this.selectOne(wrapper);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return BeanCopyUtils.copy(obj, voClass);
    }

    default List<V> selectVoList(Wrapper<T> wrapper) {
        return selectVoList(wrapper, this.currentVoClass());
    }

    default List<V> selectVoList() {
        return selectVoList(null, this.currentVoClass());
    }

    /**
     * 根据 entity 条件，查询全部记录
     */
    default <C> List<C> selectVoList(Wrapper<T> wrapper, Class<C> voClass) {
        List<T> list = this.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanCopyUtils.copyList(list, voClass);
    }


    default <P extends IPage<V>> P selectVoPage(IPage<T> page, Wrapper<T> wrapper) {
        return selectVoPage(page, wrapper, this.currentVoClass());
    }

    /**
     * 分页查询VO
     */
    default <C, P extends IPage<C>> P selectVoPage(IPage<T> page, Wrapper<T> wrapper, Class<C> voClass) {
        IPage<T> pageData = this.selectPage(page, wrapper);
        IPage<C> voPage = new Page<>(pageData.getCurrent(), pageData.getSize(), pageData.getTotal());
        if (CollUtil.isEmpty(pageData.getRecords())) {
            return (P) voPage;
        }
        voPage.setRecords(BeanCopyUtils.copyList(pageData.getRecords(), voClass));
        return (P) voPage;
    }

}
```





使用案例

top.hugo.admin.entity.SysPlatform

```java
package top.hugo.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 平台管理实体类
 *
 * @author 邝华
 * @since 2022-12-16 14:42:22
 */
@Data
@TableName(value = "sys_platform")
public class SysPlatform {
    //主键
    @TableId(type = IdType.AUTO)
    private Integer id;

    //平台的名字
    private String name;
    
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //更新人
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
}
```

top.hugo.admin.vo.SysPlatformVo

```java
package top.hugo.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.hugo.common.domain.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * 平台管理
 *
 * @author kuanghua
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysPlatformVo extends BaseEntity {
    /**
     * 平台id
     */
    private Integer id;
    /**
     * 平台名字
     */
    @NotBlank(message = "平台名字不能为空")
    private String name;
}
```

top.hugo.admin.mapper

```java
package top.hugo.admin.mapper;

import top.hugo.admin.entity.SysPlatform;
import top.hugo.admin.vo.SysPlatformVo;
import top.hugo.common.mapper.BaseMapperPlus;

import java.util.List;


/**
 * 参数配置 数据层
 *
 * @author kuanghua
 */
public interface SysPlatformMapper extends BaseMapperPlus<SysPlatformMapper, SysPlatform, SysPlatformVo> {


    /**
     * 用户xml调用测试
     *
     */
     List<SysPlatform> selectListXml();
}
```

SysPlatformMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="top.hugo.admin.mapper.SysPlatformMapper">

    <select id="selectListXml" resultType="SysPlatform">
         select * from sys_platform
    </select>
</mapper>
```

package-info.md

```
java包使用 . 分割 resource 目录使用 / 分割
此文件目的 防止文件夹粘连找不到 xml 文件
```

测试使用

top.hugo.admin.controller.MysqlTestController

```java
package top.hugo.admin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.admin.entity.SysPlatform;
import top.hugo.admin.mapper.SysPlatformMapper;
import top.hugo.common.domain.R;
import javax.annotation.Resource;
import java.util.List;
/**
 *  mysql使用测试
 */
@RestController
public class MysqlTestController {
    @Resource
    private SysPlatformMapper sysPlatformMapper;
    /**
     * mysql测试
     * @return
     */
    @GetMapping("getMysqlData")
    public R<List<SysPlatform>> test(){
        List<SysPlatform> sysPlatforms = sysPlatformMapper.selectList();
        return R.ok(sysPlatforms);
    }
    /**
     * mysql  xml测试
     * @return
     */
    @GetMapping("testXml")
    public R<List<SysPlatform>> testXml(){
        List<SysPlatform> sysPlatforms = sysPlatformMapper.selectListXml();
        return R.ok(sysPlatforms);
    }
}
```

