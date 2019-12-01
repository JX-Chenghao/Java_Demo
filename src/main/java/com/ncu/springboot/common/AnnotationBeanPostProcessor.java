package com.ncu.springboot.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 1、解析xml文件，解析出BeanDefinition
 2、Spring容器创建BeanFactoryPostProcessor实例
 3、调用BeanFactoryPostProcessor的postProcessBeanFactory方法
 4、Spring容器创建BeanPostProcessor实例
 5、在需要创建其他Bean实例的时候创建其他Bean
 6、调用Bean的构造方法
 7、调用Bean的setter方法为Bean属性赋值
 8、---------------------调用BeanPostProcessor的postProcessBeforeInitialization方法
 9、调用InitializingBean的afterPropertiesSet方法
 10、-------------------调用BeanPostProcessor的postProcessAfterInitialization方法
 11、容器销毁的时候调用DisposableBean的destroy方法

 */
public class AnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
