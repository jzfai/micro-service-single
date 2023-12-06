## springboot集成feign

本篇讲解启动两个服务 hugo-admin   hugo-demo ， 需要先集成nacos并把对应服务注册上去，如果对nacos不熟悉可以看下之前 nacos 章节



### hugo-admin

controller

```java
package top.hugo.admin.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("demo")
public class DemoController {

    @GetMapping("getReq")
    public String getReq() {
        System.out.println("hugo-admin的getReq");
        return "getReq";
    }


    @PostMapping("postReq")
    public String postReq(@RequestParam("name") String name) {
        System.out.println("hugo-admin的postReq");
        return name;
    }
}

```

用于给 hugo-demo 的feign 调用使用



## hugo-demo

添加依赖

```xml
<!--feign-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>3.1.5</version>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    <version>3.1.5</version>
</dependency>
```





hugo-demo

## 解决feign转发后 头信息丢失问题

FeignConfiguration

```java
package top.kuanghua.commonpom.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
/*feign转发时将请求的参数设转发到请求头上*/
@Configuration
@Log4j2
public class FeignConfiguration implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (name.equals("content-length")){
                        continue;
                    }
                    template.header(name, values);
                }
            }
        }
    }
}
```



feign转发

```java
package top.hugo.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.hugo.demo.config.FeignConfiguration;
@FeignClient(name="hugo-admin",configuration = FeignConfiguration.class,path = "demo")
public interface DemoFeign {
    @GetMapping("getReq")
    public String getReq();


    @PostMapping("postReq")
    public String postReq(@RequestParam("name") String name);
}
```



开启feign

```java
@SpringBootApplication
@EnableFeignClients(basePackages = {"top.hugo.demo.feign"})
public class HugoDemoApplication {
}
```



controller

```java
package top.hugo.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hugo.demo.feign.DemoFeign;

import javax.annotation.Resource;

@RestController("demo")
public class TestController {
    @Resource
    private DemoFeign demoFeign;
    
    @GetMapping("getFeign")
    public String test(){
      return demoFeign.getReq();
    }
    
    @GetMapping("postFeign")
    public String postFeign(String name){
        return demoFeign.postReq(name);
    }
}

```



hugo-admin和 hugo-demo两个服务启动