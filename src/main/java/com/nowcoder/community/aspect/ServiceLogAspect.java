package com.nowcoder.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 统一处理日志
 * Service 日志切面，为所有的 Service 方法添加日志功能，在方法调用之前记录日志
 * 
 * @author tonngw
 * @date 2022-01-16 15:39
 */
@Component
@Aspect
public class ServiceLogAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
    
    
    /**
     * 定义切入点表达式
     */
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

    }
    
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        // 日志格式：用户 [ip]，在 [time]，访问了 [com.nowcoder.community.service.xxx()]
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
            String ip = request.getRemoteHost();
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            // getDeclaringTypeName() 获取包名，getName() 获取方法名
            String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            logger.info(String.format("用户 [%s], 在 [%s], 访问了 [%s]", ip, now, target));
        }
    }
}
