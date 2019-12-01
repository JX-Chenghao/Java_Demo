package com.ncu.springboot.cxf;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

@Slf4j
public class CxfInvokeTest {


    //JaxWsProxyFactoryBean解析wsdl,根据ICommonService class对象构造具体的实现类
    public void testCxfInvoke() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ICommonService.class);
        factory.setAddress("http://localhost:9090/cxf-services/CommonService?wsdl");
        ICommonService commonService = (ICommonService) factory.create();
        log.info("【cxf客户端】 接收 returns{}",commonService.sayHello("cxf client"));
    }


    public void testAopException() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ICommonService.class);
        factory.setAddress("http://localhost:9090/cxf-services/CommonService?wsdl");
        ICommonService commonService = (ICommonService) factory.create();
        log.info("【cxf客户端】 接收 returns{}",commonService.getAopException("cxf client"));
    }
}