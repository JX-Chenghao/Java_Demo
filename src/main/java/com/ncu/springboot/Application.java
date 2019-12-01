package com.ncu.springboot;

import com.ncu.springboot.cxf.CxfInvokeTest;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;


@Controller
@SpringBootApplication
public class Application {

	private  static final Logger LOG = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		LOG.info("ApplicationContextID :{}",context.getId());
		RedissonClient redissonClient= (RedissonClient)context.getBean("redissonClient");
        LOG.info("RedissonClient Bean :{}",redissonClient.toString());

		//UserController userController= (UserController)context.getBean("userController");
        //new DistributionLockTest(userController).test1();
		//new DistributionLockTest(userController).test2();

		new CxfInvokeTest().testCxfInvoke();
        //new CxfInvokeTest().testAopException();
    }

    // 问题：config server fail GenericConversionService类中 handleConverterNotFound
    // 解决：config server 使用了与client 不同的版本H...,改回F...
    @Value(value = "${config.server.test.second}")
    private String configServerTestSecond;

    @GetMapping(value = "/configServer")
    @ResponseBody
    public String configServer(){
        return configServerTestSecond;
    }
}
