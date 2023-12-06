## docker 安装 seata

 *Seata* 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案 

上传文件夹  [seata-server](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/seata-server)   到   /docker目录下

开放目录权限

```shell
chmod -R  777 /docker/seata-server
```

生成rocketmq配置文件

```yml
cat>/docker/compose/seata.yml<<EOF
version: '3'
services:
  seata-server:
    image: seataio/seata-server:1.6.1
    container_name: seata-server
    ports:
      - "8091:8091"
      - "7091:7091"
    volumes:
      - /docker/seata-server/resources:/seata-server/resources
    environment:
      SEATA_IP: 8.134.158.197
    network_mode: "host"
EOF
```

复制容器里的文件信息(非必要)

```shell
docker cp seata-server:/seata-server  /docker/seata-server
```

### seata配置文件到nacos(服务)

注：nacos中新建namespace名seata

seata-server.properties

```properties
service.vgroupMapping.hugo-demo-group=default
service.vgroupMapping.hugo-admin-group=default
service.enableDegrade=false
service.disableGlobalTransaction=false

store.mode=db
store.lock.mode=db
store.session.mode=db
#Used for password encryption
store.publicKey=

#These configurations are required if the `store mode` is `db`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `db`, you can remove the configuration block.
store.db.datasource=hikari
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://8.134.158.197:3306/seata?useUnicode=true&rewriteBatchedStatements=true
store.db.user=root
store.db.password=@Root123
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000


#Transaction rule configuration, only for the server
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.distributedLockExpireTime=10000
server.xaerNotaRetryTimeout=60000
server.session.branchAsyncQueueSize=5000
server.session.enableBranchAsyncRemove=false

#Transaction rule configuration, only for the client
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=true
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.rm.sagaJsonParser=fastjson
client.rm.tccActionInterceptorOrder=-2147482648
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000
client.tm.interceptorOrder=-2147482648
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k

#For TCC transaction mode
tcc.fence.logTableName=tcc_fence_log
tcc.fence.cleanPeriod=1h

#Log rule configuration, for client and server
log.exceptionRate=100

#Metrics configuration, only for the server
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898

#For details about configuration items, see https://seata.io/zh-cn/docs/user/configurations.html
#Transport configuration, for client and server
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableTmClientBatchSendRequest=false
transport.enableRmClientBatchSendRequest=true
transport.enableTcServerBatchSendResponse=false
transport.rpcRmRequestTimeout=30000
transport.rpcTmRequestTimeout=30000
transport.rpcTcRequestTimeout=30000
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
transport.serialization=seata
transport.compressor=none
```

>store.mode=db  和mysql配置
>
>注：hugo-client-group 为您将要启动的服务名



将 [seata.sql](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/seata.sql)  导入到 数据库中



seata配置文件修改  /docker/seata-server/resource/application.yml 

```yml
server:
  port: 7091

spring:
  application:
    name: seata-server

logging:
  config: classpath:logback-spring.xml
  file:
    path: ${user.home}/logs/seata

console:
  user:
    username: seata
    password: seata
seata:
  config:
    type: nacos
    nacos:
      server-addr: 8.134.158.197:8848
      namespace: seata
      group: seata
      data-id: seata-server.properties
      userName: "nacos"
      password: "nacos"
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: 8.134.158.197:8848
      namespace: seata
      group: seata
      userName: "nacos"
      password: "nacos"
  security:
    secretKey: SeataSecretKey0c382ef121d778043159209298fd40bf3850a017
    tokenValidityInMilliseconds: 1800000
    ignore:
      urls: /,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/api/v1/auth/login
```

>主要修改配置 nacos配置和mysql配置 , store 从 seata-server.properties中获取
>
>xxx: 服务部署ip



#### 启动 seata-server

重启 

```shell
docker container restart seata-server
```

>xxx：服务ip

docker-compose部署

```shell
docker-compose -f  /docker/compose/seata.yml  up -d seata-server
```

>注： 开放 8091,7091 端口

seata 控制台 

服务ip:7091

用户名和密码：seata:seata





## springboot集成seata

hugo-admin 和 hugo-demo中先要集成 【spingboot集成mybits-plus和多数据源及mysql安装 】

### maven

dubbo(暂不讲)

```xml
<dependency>
    <groupId>org.apache.dubbo.extensions</groupId>
    <artifactId>dubbo-filter-seata</artifactId>
    <version>1.0.1</version>
    <exclusions>
        <exclusion>
            <groupId>io.seata</groupId>
            <artifactId>seata-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- SpringBoot Seata -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>*</artifactId>
        </exclusion>
        <exclusion>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.apache.dubbo.extensions</groupId>
            <artifactId>dubbo-filter-seata</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
feign

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    <exclusions>
        <exclusion>
            <groupId>io.seata</groupId>
            <artifactId>seata-spring-boot-starter</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
    <version>1.6.1</version>
</dependency>
```

>seata版本和部署的版本一致



#### seata配置（服务）

```yml
# seata配置
seata:
  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启
  enabled: true
  # Seata 应用编号，默认为 ${spring.application.name}
  application-id: ${spring.application.name}
  # Seata 事务组编号，用于 TC 集群名
  tx-service-group: ${spring.application.name}-group
  config:
    type: nacos
    nacos:
      server-addr: xxx:8848
      namespace: seata
      group: seata
      data-id: seata-server.properties
  registry:
    type: nacos
    nacos:
      server-addr: xxx:8848
      namespace: seata
      group: seata
      application: hugo-seata-server

```

>注：tx-service-group 配置要和  seata-server.properties的vgroupMapping  一致  



### application上开启seata事务(注所有涉及到事务的地方都要添加)


如果是多数据源

```yml
spring:
  datasource:
    dynamic:
      # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭
      seata: true
# seata配置
seata:
  # 关闭自动代理
  enable-auto-data-source-proxy: false
```



### hugo-admin和hugo-demo连接数据库

新建两个库 hugo-admin ，hugo-demo

业务数据库添加undo_log表

```sql
CREATE TABLE `undo_log` (`id` bigint(20) NOT NULL AUTO_INCREMENT,`branch_id` bigint(20) NOT NULL,`xid` varchar(100) NOT NULL,`context` varchar(128) NOT NULL,`rollback_info` longblob NOT NULL,`log_status` int(11) NOT NULL,`log_created` datetime NOT NULL,`log_modified` datetime NOT NULL,`ext` varchar(100) DEFAULT NULL,PRIMARY KEY (`id`),UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```



### service或方法上使用

@GlobalTransactional(rollbackFor = Exception.class)



运行服务 测试