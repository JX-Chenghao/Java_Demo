package com.ncu.springboot.cxf;

import com.ncu.springboot.aop.ApiInvokeTimeShow;
import com.ncu.springboot.mvc.exception.OwnException;
import com.ncu.springboot.pojo.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@WebService(serviceName = "HelloService", // 与接口中指定的name一致
        targetNamespace = "http://cxf.springboot.ncu.com/", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.ncu.springboot.cxf.ICommonService"// 接口地址
)
@Component
@Slf4j
public class CommonServiceImpl implements ICommonService {

    @Override
    @ApiInvokeTimeShow
    public String sayHello(String name) {
        log.info("【cxf服务端】 WebService调用 sayHello ,params:{}",name);
        //throw new OwnException("Aop异常抛出");
        return "Hello ," + name;
    }

    @Override
    @ApiInvokeTimeShow
    public String getAopException(String name) {
        log.info("【cxf服务端】 WebService调用 sayHello ,params:{}",name);
        throw new OwnException("Aop异常抛出");
        //return "Hello ," + name;
    }

    @Override
    @ApiInvokeTimeShow
    public Role getRole(String name) {
        log.info("【cxf服务端】 WebService调用 getRole ,params:{}",name);
        Role role = new Role();
        role.setRoleType("ADMIN");
        return role;
    }
}