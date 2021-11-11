``#### 更新日志
```
// 2021-09-15
1.增加了文件上传下载，为vue3-element-plus提供例子数据
```

### 前言
```
本架构使用的技术为：
spring-boot:2.1.4.RELEASE
spring-cloud:Greenwich.SR1
spring-cloud-starter-gateway:2.1.1.RELEASE
nacos:2.1.3.RELEASE
mybatis-plus: 3.3.2
rabbitmq:3.7-management
redis: 3.2.9
mysql:5.7
```

#### swager文档地址：

http://8.135.1.141/micro-service-doc/swagger-ui.html

#### nacos地址（naocs配置请不要删除）：

http://8.135.1.141:8848/nacos/

用户名：jzfai
密码：123456

#### 微服务（后续还会新增新的微服务）

```
ty-common: 统一依赖，统一配置管理，feign接口整合等
本架构的微服务有：
ty-gateway:网关微服务。使用的是spring-cloud-starter-gateway，先对于zull网关来说性能更好。主要实现了，权限控制和拦截，jwt token解析和校验,swagger文件整合，使用令牌技术请求限流等
ty-auth：权限服务。jwt token生成，解析，校验等，默认配置3天有效期，少于一天自动续约（此处感兴趣的可以查看下源码）
ty-user：用户服务。用户的登录，注册，查看用户信息等一系列功能
ty-upload：文件上传服务。使用的技术为fastDFS技术，此服务主要用于文件的上传和下载
```

#### 亮点：

```
1.在网关中将jwt token和解析后的token信息已经设置在请求头中，在后续转发的微服务可以在请求头中轻松的拿到token和token解析后的数据，不用再通过feign去调用ty-auth获取token信息
```

![1631692199271](http://8.135.1.141/file/images/java1.png)

```
2.此时微服务已经可以在请求通中获取到token和信息，但是feign调用其他微服务的时候却出现了token丢失的情况，此时需要在feign调用服务时将请求头一起转发,那么微服务之前调用时也能获取都token和信息
```

![1631692308212](http://8.135.1.141/file/images/java2.png)

```
3.少于1天时间自动更新token，主要通过gateway的两个拦截器来完成
AuthorizeFilterBefore：请求前拦截。解析token的有效性和是否过期，和配置了白名单的请求方法进行放行
AuthorizeFilterAfter：请求后拦截。主要用于token更新校验，和生产新的token,通过isNeedUpdateToken字段告诉前端要更新token
如果想知道前端如何更新token可查看：
https://github.com/jzfai/vue3-admin-template
```

![1631693000767](http://8.135.1.141/file/images/java3.png)

```
4.多环境和nacos：
开发人员可以快速的切换环境以达到开发的目的
目前为止只添加了
dev:开发时使用的分支
prod:发布是切换的分支
注：配置环境时请在nacos上添加相应的配置文件，不然会报错
nacos:
注册中心：
通过discovery进行配置，具体可查看源码
配置中心：
加载顺序：naocs默认加载的config配置文件-> shared-configs->extension-configs
优先级：naocs默认加载的config配置文件->extension-configs-> shared-configs
```

![1631693866876](http://8.135.1.141/file/images/java4.png)

![1631694125676](http://8.135.1.141/file/images/java5.png)

```
5.easycode模板配置：
本架构提供了基本的版本生成，可以快速生成,实现前后端的增删改查，前端的easycode生成的模板适配vue-element-template架构，可以把模板直接复制到easycode中生成相应的代码
不会使用easycode可以查看这里
https://www.jianshu.com/p/e4192d7c6844
```

![1631695768271](http://8.135.1.141/file/images/java6.png)

### 如何运行

```javascript
克隆项目
git clone  https://github.com/jzfai/micro-service-plus.git
用idea打开项目,加载下载依赖即可运行
```

>注：本项目加入了热更新：运行项目后，使用ctrl+F9快捷键或rebuild下就行直接更新服务

#### 如果需要实时交流的可以加入wx群(有vue3+ts视频教程)

 ![http://8.135.1.141/file/images/wx-groud.png](http://8.135.1.141/file/images/wx-groud.png)

大家的支持是我前进的动力    欢迎加入一起开发
