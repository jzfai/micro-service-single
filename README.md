# micro-service-plus

**English** | [中文](./README-zh_CN.md)

> micro-service-plus provides enterprise-level development demo

## foreword

this architecture is the springCloud microservice architecture, which provides basic jwt authorization authentication,
token update, and calls between microservices, as well as enterprise-level usage examples.

```text
spring-boot:2.1.4.RELEASE
spring-cloud:Greenwich.SR1
spring-cloud-starter-gateway:2.1.1.RELEASE
nacos:2.1.3.RELEASE
mybatis-plus: 3.3.2
rabbitmq:3.7-management
redis: 3.2.9
mysql:5.7
seata:1.4.2
canal:1.2.1
```

> some of the above versions will be upgraded in the future

## Doc and Example

[swager文档地址](http://8.135.1.141/micro-service-doc/swagger-ui.html)

![1642222390648](http://8.135.1.141/file/micro-service-assets/1642222390648.png)

## Recommended reading

使用save action 保存自动格式化代码

http://www.360doc.com/content/21/1130/10/77916903_1006517212.shtml

网关hystix和ribbon超时时间熔断设置
https://blog.csdn.net/u014203449/article/details/105248914?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_ecpm_v1~rank_v31_ecpm-1-105248914.pc_agg_new_rank&utm_term=gateway%E8%AE%BE%E7%BD%AE%E8%B6%85%E6%97%B6%E6%97%B6%E9%97%B4&spm=1000.2123.3001.4430

高并发架构系列：Kafka、RocketMQ、RabbitMQ的优劣势比较
https://www.jianshu.com/p/fec054f3e496

## update log

to look doc ----

[带你用springcloud微服务撸后台(系列文章入口)](https://juejin.cn/post/7044843310204059655)

## online documentation

[带你用springcloud微服务撸后台(系列文章入口)](https://juejin.cn/post/7044843310204059655)

## Interface documentation and nacos address

[swager文档地址](http://8.135.1.141/micro-service-doc/swagger-ui.html)

[nacos地址](http://8.135.1.141:8848/nacos/)    naocs:    用户名：jzfai; 密码：123456

## Online experience address

[github address](https://github.com/jzfai/micro-service-plus.git)

[Access address](http://8.135.1.141/vue3-admin-plus)

国内体验地址：http://8.135.1.141/vue3-admin-plus

github 地址：  https://github.com/jzfai/micro-service-plus.git

## Introduction to Microservices

##### ty-gateway-one：

gateway Microservices. The spring-cloud-starter-gateway is used, which has better performance than the zull gateway. It
mainly implements permission control and interception, jwt token parsing and verification, swagger file integration, and
request current limiting using token technology. Can start multiple

##### ty-auth：

permission service. jwt token generation, parsing, verification, etc., the default configuration is valid for 3 days,
and the contract is automatically renewed if it is less than one day (if you are interested here, you can view the
source code)

##### ty-example:

provide some examples. Including, rabbitmq delay queue, goFastDFS file upload, canal, seata partial transaction, SMS
sending, mail sending, etc.

##### integration-front：

data Integration. vue3-admin-plus data source easycode-temp：easycode front-end and back-end templates

## How to run

Run micro-service-plus, the default selection is the test branch

```
git clone  https://github.com/jzfai/micro-service-plus.git
//maven download the depdences
```

How to cooperate with vue3-admin-plus to develop front-end and back-end together

```shell
#vue3-admin-plus
git clone  https://github.com/jzfai/vue3-admin-plus.git
pnpm i
#test Branch requests local gateway
pnpm run test

#micro-service-plus At least need to run
ty-auth
ty-gateway-one
ty-integration-front

The above three service
```

> Note: Select the environment in Maven Projects->Profies. Currently online is a package built in the prod environment, please do not select.

## Architecture Highlights

1. In the gateway, the jwt token and the parsed token information have been set in the request header, and the
   subsequent forwarding microservices can easily get the token and token parsed data in the request header. There is no
   need to call ty-auth through feign to obtain token information.

2. During the invocation of each microservice, the token in the request header will be forwarded. So the token will not
   be lost when calling the microservice with feign.

3. The token is automatically updated in less than 3 day, and the isNeedUpdateToken field is used to tell the front end
   that the token needs to be updated, realizing the function of token renewal

4. Switch between multiple environments at will. In the pom.xml in the root directory, profiles are configured to switch
   the environment according to your needs.

5. Provides easycode templates related to vue3+element-plus and mybits-plus. You can generate front-end and back-end
   templates at one time, greatly improving development efficiency

