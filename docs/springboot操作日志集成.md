切面拦截器

依赖

```xml
<!-- SpringBoot 拦截器 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```



定义注解

```java
package top.hugo.common.annotation;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    String title() default "";

    //    /**
    //     * 功能
    //     */
    //    BusinessType businessType() default BusinessType.OTHER;
    //
    //    /**
    //     * 操作人类别
    //     */
    //    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;
}
```



切面类

```java
package top.hugo.admin.aspectj;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.hugo.common.annotation.Log;
import top.hugo.common.event.OperLogEvent;
import top.hugo.common.utils.ServletUtils;
import top.hugo.satoken.helper.LoginHelper;


/**
 * 操作日志记录处理
 *
 * @author kuanghua
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        OperLogEvent operLog = new OperLogEvent();


        operLog.setStatus(1);
        // 请求的地址
        String ip = ServletUtils.getClientIP();
        operLog.setOperIp(ip);
        operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
        operLog.setOperName(LoginHelper.getUsername());
        // 设置方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        operLog.setMethod(className + "." + methodName + "()");
        // 设置请求方式
        operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
        SpringUtil.getApplicationContext().publishEvent(operLog);

        log.info(controllerLog.title());
    }

}

```



定义 OperLogEvent 事件

```java
@Async
@EventListener
public void recordOper(OperLogEvent operLogEvent) {
    SysOperLog operLog = BeanUtil.toBean(operLogEvent, SysOperLog.class);
    // 远程查询操作地点   operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
    insertSysOperLog(operLog);
}
```

