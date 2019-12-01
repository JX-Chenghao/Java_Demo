package com.ncu.springboot.cxf;

import com.ncu.springboot.pojo.Role;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "HelloService",// 暴露服务名称
        targetNamespace = "http://cxf.springboot.ncu.com/"// 命名空间,一般是接口的包名倒序
)
public interface ICommonService {
    @WebMethod
    public String sayHello(@WebParam(name = "userName") String name);

    @WebMethod
    public String getAopException(@WebParam(name = "userName") String name);

    @WebMethod
    public Role getRole(@WebParam(name = "userName") String name);
}