package com.ncu.springboot.aop;

import com.ncu.springboot.mvc.exception.OwnException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(3)
public class TestAopOrderAspectC {
    private static final Logger LOG= LoggerFactory.getLogger(TestAopOrderAspectC.class);

    @Pointcut(value = "within(com.ncu.springboot.mvc.controller..*) "
            +"|| @annotation(com.ncu.springboot.aop.ApiInvokeTimeShow)) ")
    public void pointCut(){LOG.debug("TestAopOrderAspect pointCut");}

    @Around(value = "pointCut() && @annotation(apiInvokeTimeShow)",argNames="joinPoint,apiInvokeTimeShow")
    public Object  aroundAdvice(ProceedingJoinPoint joinPoint, ApiInvokeTimeShow apiInvokeTimeShow) throws Throwable {
        LOG.info("{}：(order=3)","aroundAdvice1");
        Object result = null;
        try {
            result = joinPoint.proceed();
        }catch (Exception e) {
            LOG.info("error");
            throw e;
        }
        LOG.info("{}：(order=3)","aroundAdvice2");
        return result;

    }

    @Before(value = "pointCut() && @annotation(apiInvokeTimeShow)",argNames = "joinPoint,apiInvokeTimeShow")
    public void beforeAdvice(JoinPoint joinPoint, ApiInvokeTimeShow apiInvokeTimeShow){
        LOG.info("{}：(order=3)","beforeAdvice");
    }


    @After(value = "pointCut() && @annotation(apiInvokeTimeShow)",argNames="joinPoint,apiInvokeTimeShow")
    public void AfterAdvice(JoinPoint joinPoint, ApiInvokeTimeShow apiInvokeTimeShow){
        LOG.info("{}：(order=3)","AfterAdvice");
    }

    @AfterReturning(value = "pointCut() && @annotation(apiInvokeTimeShow)",argNames="joinPoint,apiInvokeTimeShow")
    public void AfterReturnAdvice(JoinPoint joinPoint, ApiInvokeTimeShow apiInvokeTimeShow){
        LOG.info("{}：(order=3)","AfterReturnAdvice");
    }

    @AfterThrowing(value = "pointCut() && @annotation(apiInvokeTimeShow)",argNames="joinPoint,apiInvokeTimeShow")
    public void AfterThrowAdvice(JoinPoint joinPoint, ApiInvokeTimeShow apiInvokeTimeShow){
        LOG.info("{}：(order=3)","AfterThrowAdvice");
    }

}
