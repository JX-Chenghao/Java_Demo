package com.ncu.springboot.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiInvokeTimeShow {
    String methodName() default "default";
}
