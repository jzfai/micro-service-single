## springboot集成satoken

[sa-token官方文档](https://sa-token.cc/doc.html#/)

##### 个轻量级 Java 权限认证框架，让鉴权变得简单、优雅！

maven依赖

```xml
<!-- Sa-Token 权限认证, 在线文档：http://sa-token.dev33.cn/ -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot-starter</artifactId>
    <version>1.34.0</version>
</dependency>
<!-- Sa-Token 整合 jwt -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-jwt</artifactId>
    <version>1.34.0</version>
</dependency>
```

application-dev.yml

```yml
# Sa-Token配置
sa-token:
  enabled: true
  # token名称 (同时也是cookie名称)
  token-name: Authorization
  # token有效期 设为一周 (必定过期) 单位: 秒
  timeout: 604800
  # token临时有效期 (指定时间无操作就过期) 单位: 秒（三天）
  activity-timeout: 259200
  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
  is-share: true
  # 是否尝试从header里读取token
  is-read-header: true
  # 是否尝试从cookie里读取token
  is-read-cookie: false
  # token前缀
  token-prefix: "Bearer"
  # jwt秘钥
  jwt-secret-key: abcdefghijklmnopqrstuvwxyz

# security配置
security:
  # 排除路径
  excludes:
    # 静态资源
    - /**.html
    - /**/*.html
    - /**/*.css
    - /**/*.js
    - /login
    - /register
    # swagger 文档配置
    - /favicon.ico
    - /*/api-docs
    - /*/api-docs/**
    - /basis-func/**
    - /system/platform/** 
```

top.hugo.framework.config.properties.SecurityProperties

```java
package top.hugo.admin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Security 配置属性
 *
 * @author kuanghua
 */
@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SaTokenSecurityProperties {
    /**
     * 排除路径
     */
    private String[] excludes;
}
```

top.hugo.framework.config.SaTokenConfig

```java
package top.hugo.admin.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.hugo.admin.properties.SaTokenSecurityProperties;

@RequiredArgsConstructor
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "sa-token", name = "enabled", havingValue = "true")
public class SaTokenConfig  implements WebMvcConfigurer {
    private  final SaTokenSecurityProperties saTokenSecurityProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler->{
            SaRouter.match("/**").check(StpUtil::checkLogin);
        })).addPathPatterns("/**").excludePathPatterns(saTokenSecurityProperties.getExcludes());
    }
    @Bean
    public StpLogic getStpLogicJwt() {
        // Sa-Token 整合 jwt (简单模式)
        return new StpLogicJwtForSimple();
    }
}
```

菜单和角色权限集成

top.hugo.framework.config.SaPermissionImpl

```java
package top.hugo.framework.config;
import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
/**
 * sa-token 权限管理实现类
 *
 * @author kuanghua
 */
@Component
public class SaPermissionImpl implements StpInterface {
    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("permission:list");
        return arrayList;
    }
    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("role:list");
        return arrayList;
    }
}
```



测试用例


top.hugo.admin.controller.SaTokenController

```java
package top.hugo.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关
 */
@Validated
@RequiredArgsConstructor
@RestController
public class SaTokenController {
    /**
     * 登录方法
     */
    @SaIgnore
    @PostMapping("login")
    public Map<String, Object> login() {
        Map<String, Object> ajax = new HashMap<>();
        SaLoginModel saLoginModel = SaLoginConfig.setExtra("user", "fai");
        StpUtil.login(1,saLoginModel);
        ajax.put("token", StpUtil.getTokenValue());
        return ajax;
    }

    /**
     * 退出登录
     */
    @PostMapping("logout")
    public String logout() {
        StpUtil.logout();
        return"退出成功";
    }

    /**
     * 角色测试
     */
    @SaCheckRole("role:list")
    @GetMapping("RoleTest")
    public String RoleTest() {
        return "RoleTest";
    }

    /**
     * 权限测试
     */
    @SaCheckPermission("permission:list")
    @GetMapping("PermissionTest")
    public String PermissionTest() {
        return "RoleTest";
    }
}
```

>注： @SaCheckPermission和 @SaCheckRole 只二者选择其中一个



启动服务打开swagger文档进行测试

http://localhost:10100/doc.html#/test/sa-token-controller