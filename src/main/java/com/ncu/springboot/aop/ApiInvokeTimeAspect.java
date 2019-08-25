package com.ncu.springboot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ApiInvokeTimeAspect {
    private static final Logger LOG= LoggerFactory.getLogger(ApiInvokeTimeAspect.class);
    private static final String DEFAULT = "default";

    @Pointcut(value = "within(com.ncu.springboot.mvc.controller..*) "
            +"|| @annotation(com.ncu.springboot.aop.ApiInvokeTimeShow)) ")
    public void pointCut(){LOG.debug("ApiInvokeTimeAspect pointCut");}


    @Around(value = "pointCut() && @annotation(apiInvokeTimeShow)",argNames="joinPoint,apiInvokeTimeShow")
    public Object  aroundAdvice(ProceedingJoinPoint joinPoint, ApiInvokeTimeShow apiInvokeTimeShow) throws Throwable {
        String methodName ;
        if (apiInvokeTimeShow.methodName().equals(DEFAULT)) {
            methodName = joinPoint.getSignature().getName();
        } else {
            methodName = apiInvokeTimeShow.methodName();
        }
        long begin = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        LOG.info("{}：耗时 {}ms" , methodName,(end - begin));

        return result;
    }


    @Before(value = "pointCut() && @annotation(apiInvokeTimeShow)",argNames = "joinPoint,apiInvokeTimeShow")
    public void beforeAdvice(JoinPoint joinPoint, ApiInvokeTimeShow apiInvokeTimeShow){
        String methodName ;
        if (apiInvokeTimeShow.methodName().equals(DEFAULT)) {
            methodName = joinPoint.getSignature().getName();
        } else {
            methodName = apiInvokeTimeShow.methodName();
        }
        LOG.info("{}：方法调用开始",methodName);
    }


    @After(value = "pointCut() && @annotation(apiInvokeTimeShow)",argNames="joinPoint,apiInvokeTimeShow")
    public void AfterAdvice(JoinPoint joinPoint, ApiInvokeTimeShow apiInvokeTimeShow){
        String methodName ;
        if (apiInvokeTimeShow.methodName().equals(DEFAULT)) {
            methodName = joinPoint.getSignature().getName();
        } else {
            methodName = apiInvokeTimeShow.methodName();
        }
        LOG.info("{}：方法调用结束",methodName);
    }


}
