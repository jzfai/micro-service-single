package top.hugo.admin.aspectj;


import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import top.hugo.common.annotation.Log;
import top.hugo.common.event.OperLogEvent;
import top.hugo.common.utils.ServletUtils;
import top.hugo.satoken.helper.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


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
     * 计算操作消耗时间
     */
    private static final ThreadLocal<StopWatch> TIME_THREADLOCAL = new TransmittableThreadLocal<>();

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        StopWatch stopWatch = new StopWatch();
        TIME_THREADLOCAL.set(stopWatch);
        stopWatch.start();
    }


    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        OperLogEvent operLog = new OperLogEvent();
        operLog.setStatus("0");
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

        //设置注解上的值
        operLog.setBusinessType(controllerLog.businessType().ordinal());
        operLog.setTitle(controllerLog.title());
        operLog.setOperatorType(controllerLog.operatorType().ordinal());
        // 设置消耗时间
        StopWatch stopWatch = TIME_THREADLOCAL.get();
        stopWatch.stop();
        operLog.setCostTime(stopWatch.getTime());

        //发送事件保存
        SpringUtil.getApplicationContext().publishEvent(operLog);
    }
}
