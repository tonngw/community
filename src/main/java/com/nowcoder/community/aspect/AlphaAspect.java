package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * AOP 示例
 *
 * @author tonngw
 * @date 2022-01-16 15:36
 */
//@Component
//@Aspect
public class AlphaAspect {

    /**
     * 定义切入点表达式
     */
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

    }

    /**
     * 前置通知
     */
    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    /**
     * 后置通知
     */
    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    /**
     * 返回通知
     */
    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    /**
     * 异常通知
     */
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    /**
     * 环绕通知，是最强大的，可以完成其他四种通知的功能
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }

}
