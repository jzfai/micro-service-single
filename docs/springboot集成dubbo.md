## springboot集成 dubbo

Apache Dubbo (incubating) |ˈdʌbəʊ| 是一款高性能、轻量级的开源Java RPC 框架，它提供了三大核心能力：面向接口的远程方法调用，智能容错和负载均衡，以及服务自动注册和发现。简单来说 Dubbo 是一个分布式服务框架，致力于提供高性能和透明化的RPC远程服务调用方案，以及SOA服务治理方案。

Dubbo 目前已经有接近 23k 的 Star ，Dubbo的Github 地址：https://github.com/apache/incubator-dubbo 。 另外，在开源中国举行的2018年度最受欢迎中国开源软件这个活动的评选中，Dubbo 更是凭借其超高人气仅次于 vue.js 和 ECharts 获得第三名的好成绩。



### hugo-admin 和 hugo-demo 都需要

dubbo依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
    <version>2.7.7</version>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-context</artifactId>
    <version>3.1.4</version>
</dependency>
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>3.1.4</version>
</dependency>

<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-actuator</artifactId>
    <version>3.1.4</version>
</dependency>

<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo</artifactId>
    <version>3.1.4</version>
    <exclusions>
        <exclusion>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```



yml

```yml
dubbo:
  application:
    logger: slf4j
    # 元数据中心 local 本地 remote 远程 这里使用远程便于其他服务获取
    metadataType: remote
    # 可选值 interface、instance、all，默认是 all，即接口级地址、应用级地址都注册
    register-mode: instance
    service-discovery:
      # FORCE_INTERFACE，只消费接口级地址，如无地址则报错，单订阅 2.x 地址
      # APPLICATION_FIRST，智能决策接口级/应用级地址，双订阅
      # FORCE_APPLICATION，只消费应用级地址，如无地址则报错，单订阅 3.x 地址
      migration: FORCE_APPLICATION
    qos-enable: false
  protocol:
    # 设置为 tri 即可使用 Triple 3.0 新协议
    # 性能对比 dubbo 协议并没有提升 但基于 http2 用于多语言异构等 http 交互场景
    # 使用 dubbo 协议通信
    name: dubbo
    # dubbo 协议端口(-1表示自增端口,从20880开始)
    port: -1
    # 指定dubbo协议注册ip
    # host: 192.168.0.100
  # 注册中心配置
  registry:
    address: nacos://xxx:8848
    group: dev
    parameters:
      namespace: dev
    username: nacos
    password: nacos
  # 消费者相关配置
  consumer:
    # 结果缓存(LRU算法)
    # 会有数据不一致问题 建议在注解局部开启
    cache: false
    # 支持校验注解
    validation: true
    # 超时时间
    timeout: 3000
    # 初始化检查
    check: false
  scan:
    # 接口实现类扫描
    base-packages: top.hugo.**.dubbo
  # 自定义配置
  custom:
    # 全局请求log
    request-log: true
    # info 基础信息 param 参数信息 full 全部
    log-level: info
```

> xxx: 服务ip
> base-packages : 根据自己服务相关情况配置





###  hugo-api

新建公共api服务 

dubbo服务需要注册的接口

```java
public interface RemoteDataApi {
    /**
     * 获取角色自定义权限语句
     */
    String getRoleCustom(Long roleId);

    /**
     * 获取部门和下级权限语句
     */
    String getDeptAndChild(Long deptId);
}
```





hugo-admin

注册 dubbo服务


```java
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteTestDubbo implements RemoteDataDubboApi {

    @Override
    public String getRoleCustom(Long roleId) {
        return "i am Dubbo getRoleCustom";
    }

    @Override
    public String getDeptAndChild(Long deptId) {
        return "i am Dubbo getDeptAndChild";
    }
}
```



调用dubbo服务
hugo-demo

```java
@RestController
public class ClientController {
    @DubboReference
    private RemoteDataDubboApi remoteDataApi;

    @GetMapping("dubboTest")
    public String dubboTest() throws InterruptedException {
        return remoteDataApi.getDeptAndChild(111L);
    }
}
```



hugo-admin和hugo-demo都要引入

```xml
        <dependency>
            <groupId>top.hugo</groupId>
            <artifactId>hugo-api</artifactId>
            <version>4.4.0</version>
        </dependency>
```





注： 必须有公共的api 服务  如 hugo-api  ,  注册的api和 使用的api要一致    ， 如：remoteDataApi