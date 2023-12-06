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

        //
        log.info(controllerLog.title());
    }

}
