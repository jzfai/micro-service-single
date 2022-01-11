### 前言

本架构为springCloud微服务架构，提供基本的jwt权限认证，token更新，以及各微服务之间调用等
使用的技术如下：

```java
spring-boot:2.1.4.RELEASE
spring-cloud:Greenwich.SR1
spring-cloud-starter-gateway:2.1.1.RELEASE
nacos:2.1.3.RELEASE
mybatis-plus: 3.3.2
rabbitmq:3.7-management
redis: 3.2.9
mysql:5.7
```

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

```
ty-common: 统一依赖，统一配置管理，feign接口整合等
本架构的微服务有：
ty-gateway:网关微服务。使用的是spring-cloud-starter-gateway，先对于zull网关来说性能更好。主要实现了，权限控制和拦截，jwt token解析和校验,swagger文件整合，使用令牌技术请求限流等
ty-auth：权限服务。jwt token生成，解析，校验等，默认配置3天有效期，少于一天自动续约（此处感兴趣的可以查看下源码）
integration-front：用户服务。用户的登录，注册，查看用户信息等一系列功能
ty-excel：excel文件的读取和导出
ty-upload：文件上传服务。使用的技术为fastDFS技术，此服务主要用于文件的上传和下载
```


#### 如何运行

```java
//git clone项目
git clone  https://github.com/jzfai/micro-service-plus.git

//maven 下载依赖,即可运行
```

>注：在Maven Projects->Profies 选择环境时，请不要选择prod环境。目前线上是prod环境下构建的包

#### 架构亮点

1.网关中将jwt token和解析后的token信息已经设置在请求头中，在后续转发的微服务可以在请求头中，轻松的拿到token和token解析后的数据。不用再通过feign去调用ty-auth，获取token信息。

2.各微服务在调用期间，会将请求头的token进行转发。所以在用feign调用微服务时token也不会丢失。

3.少于1天时间自动更新token，通过isNeedUpdateToken字段告诉前端需要更新token，实现了token续约的功能

4.多环境随意切换。在根目录下的pom.xml配置了profiles可以根据你的需要进行切换环境

5.提供了vue3+element-plus和mybits-plus相关的easycode模板。你可以一次性生成前后端模板，极大提高开发效率