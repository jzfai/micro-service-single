## docker-compose 安装 nacos

新增  /docker目录

```shell
mkdir -p /docker
```

复制 [nacos](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/nacos)  到 docker 目录下

授予最高权限

```shell
chmod -R  777 /docker/nacos
```

#### nacos配置文件

```yml
cat>/docker/compose/nacos.yml<<EOF
version: "3"
services:
  nacos:
    image: nacos/nacos-server:v2.2.0
    container_name: nacos
    environment:
      # 支持主机名可以使用hostname,否则使用ip，默认ip
      - PREFER_HOST_MODE=ip
      # 单机模式
      - MODE=standalone
      # 数据源平台 支持mysql或不保存empty
      - SPRING_DATASOURCE_PLATFORM=mysql
      # mysql配置，attention必须是mysql所在主机IP
      - MYSQL_SERVICE_HOST=127.0.0.1
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_USER=root
      - MYSQL_SERVICE_PASSWORD=@Root123
      - MYSQL_SERVICE_DB_NAME=nacos_config
      - NACOS_AUTH_SYSTEM=true
      - JVM_XMS=128m
      - JVM_XMX=128m
      - JVM_XMN=64m
    volumes:
      - /docker/nacos/logs:/home/nacos/logs
      - /docker/nacos/conf:/home/nacos/conf
    ports:
      - "8848:8848"
      - "9848:9848"
      - "9849:9849"
    restart: always 
    network_mode: "host"
EOF
```

>注：xxx ： 改为你服务ip ，根据部署的mysql信息进行修改



#### 配置nacos使用的sql 

运行 [nacos_config.sql](https://gitee.com/jzfai/micro-serivce-learn/tree/master/docs-file/nacos_config.sql)  文件，插入sql

#### 部署

```shell
docker-compose -f  /docker/compose/nacos.yml  up -d  nacos
```



查看是否启动成功

```shell
docker container logs -f nacos
```

查看端口启动

```shell
netstat -antp | grep 8848
```

容器目录复制到宿主机(非必须)

```
docker cp  nacos:/home/nacos/logs /docker/nacos
docker cp nacos:/home/nacos/conf /docker/nacos
```

开放端口

```
8848,9848,9849
```

控制台地址：http://xxx:8848/nacos/

用户名：密码   nacos:nacos



创建命名空间 test

>注：名称，namespaceId 都是 test



## springboot集成nacos

maven

```xml
<!-- SpringCloud Alibaba Nacos discovery -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>2021.0.4.0</version>
</dependency>

<!-- SpringCloud Alibaba Nacos Config -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <version>2021.0.4.0</version>
</dependency>
```



yml配置文件

```yml
--- # nacos配置
spring:
  cloud:
    nacos:
      password: nacos
      username: nacos
      discovery:
        server-addr: xxx:8848
        group: dev
        namespace: dev
      config:
        server-addr: xxx:8848
        group: dev
        namespace: dev
  config:
    import:
      - optional:nacos:application-common.yml
```

>注： namespace 写  "命名空间ID"  不能写命名空间名称
>
>xxx:需要替换部署服务器ip



nacos中新建文件测试 文件是否能被读取

application.common.yml

```yml
server:
  port: 10200
```

>注：命名空间和组  要和你配置的一致



启动服务 端口是否变成了 10200