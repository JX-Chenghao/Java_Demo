package com.ncu.springboot;

import com.ncu.springboot.mvc.controller.UserController;
import com.ncu.springboot.test.distribution.DistributionLockTest;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class Application {

	private  static final Logger LOG = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		LOG.info("ApplicationContextID :{}",context.getId());
		RedissonClient redissonClient= (RedissonClient)context.getBean("redissonClient");
        LOG.info("RedissonClient Bean :{}",redissonClient.toString());

		UserController userController= (UserController)context.getBean("userController");
        //new DistributionLockTest(userController).test1();
		//new DistributionLockTest(userController).test2();
    }
}
