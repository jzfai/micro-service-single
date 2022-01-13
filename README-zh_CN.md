# micro-service-plus

**中文** | [English](./README.md)

```
> micro-service-plus提供企业级的开发demo
```

### 前言

本架构为springCloud微服务架构，提供基本的jwt权限认证，token更新，以及各微服务之间调用，以及提供企业级的使用例子

```java
spring-boot:2.1.4.RELEASE
        spring-cloud:Greenwich.SR1
        spring-cloud-starter-gateway:2.1.1.RELEASE
        nacos:2.1.3.RELEASE
        mybatis-plus:3.3.2
        rabbitmq:3.7-management
        redis:3.2.9
        mysql:5.7
        seata:1.4.2
        canal:1.2.1  
```

> 以上部分版本后续会升级

### 推荐阅读：

使用save action 保存自动格式化代码

http://www.360doc.com/content/21/1130/10/77916903_1006517212.shtml

网关hystix和ribbon超时时间熔断设置
https://blog.csdn.net/u014203449/article/details/105248914?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_ecpm_v1~rank_v31_ecpm-1-105248914.pc_agg_new_rank&utm_term=gateway%E8%AE%BE%E7%BD%AE%E8%B6%85%E6%97%B6%E6%97%B6%E9%97%B4&spm=1000.2123.3001.4430

高并发架构系列：Kafka、RocketMQ、RabbitMQ的优劣势比较
https://www.jianshu.com/p/fec054f3e496

#### 更新日志：

to look doc ----

[带你用springcloud微服务撸后台(系列文章入口)](https://juejin.cn/post/7044843310204059655)

#### 线上文档：

[带你用springcloud微服务撸后台(系列文章入口)](https://juejin.cn/post/7044843310204059655)

#### 开发接口和nacos地址

[swager文档地址](http://8.135.1.141/micro-service-doc/swagger-ui.html)

[nacos地址](http://8.135.1.141:8848/nacos/)    naocs:    用户名：jzfai; 密码：123456

#### 线上体验地址：

[github address](https://github.com/jzfai/micro-service-plus.git)

[Access address](http://8.135.1.141/vue3-admin-plus)

国内体验地址：http://8.135.1.141/vue3-admin-plus

github 地址：  https://github.com/jzfai/micro-service-plus.git

#### 目前实现的微服务（后续还会新增新的微服务）

```javascript
ty - gateway - one
:
网关微服务。使用的是spring - cloud - starter - gateway，相对于zull网关来说性能更好。主要实现了，权限控制和拦截，jwt
token解析和校验, swagger文件整合，使用令牌技术请求限流等。可启动多个
ty - auth：权限服务。jwt
token生成，解析，校验等，默认配置3天有效期，少于一天自动续约（此处感兴趣的可以查看下源码）
ty - example
:
提供一些例子。包括，rabbitmq延时队列，goFastDFS文件上传，canal, seata分部式事务，短信发送，邮件发送等。
integration - front：数据整合。vue3 - admin - plus
数据来源
easycode - temp：easycode前后端模板
```

#### 如何运行

运行micro-service-plus，默认选择的是test分支

```
git clone  https://github.com/jzfai/micro-service-plus.git
//maven 下载依赖,即可运行
```

如何配合vue3-admin-plus ，前后端一起开发

```java
#vue3-admin-plus
        git clone https://github.com/jzfai/vue3-admin-plus.git  
        pnpm i
        #test分支请求本地的网关
        pnpm run test

        #micro-service-plus 至少需要运行
        ty-auth
        ty-gateway-one
        ty-integration-front
        以上三个服务 
```

> 注：在Maven Projects->Profies 选择环境。目前线上是prod环境下构建的包，请不要选择。

#### 架构亮点

1.网关中将jwt token和解析后的token信息已经设置在请求头中，在后续转发的微服务可以在请求头中，轻松的拿到token和token解析后的数据。不用再通过feign去调用ty-auth，获取token信息。

2.各微服务在调用期间，会将请求头的token进行转发。所以在用feign调用微服务时token也不会丢失。

3.少于1天时间自动更新token，通过isNeedUpdateToken字段告诉前端需要更新token，实现了token续约的功能

4.多环境随意切换。在根目录下的pom.xml配置了profiles可以根据你的需要进行切换环境

5.提供了vue3+element-plus和mybits-plus相关的easycode模板。你可以一次性生成前后端模板，极大提高开发效率