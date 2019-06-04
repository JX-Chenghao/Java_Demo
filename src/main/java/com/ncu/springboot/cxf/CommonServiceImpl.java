package com.ncu.springboot.cxf;

import com.ncu.springboot.pojo.Role;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@WebService(serviceName = "HelloService", // 与接口中指定的name一致
        targetNamespace = "http://cxf.springboot.ncu.com/", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.ncu.springboot.cxf.ICommonService"// 接口地址
)
@Component
public class CommonServiceImpl implements ICommonService {

    @Override
    public String sayHello(String name) {
        return "Hello ," + name;
    }

    @Override
    public Role getRole(String name) {
        Role role = new Role();
        role.setRoleType("ADMIN");
        return role;
    }
}