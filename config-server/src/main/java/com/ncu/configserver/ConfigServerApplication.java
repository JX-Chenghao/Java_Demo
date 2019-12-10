package com.ncu.configserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    @Autowired
    private DiscoveryClient discoveryClient;

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        List<ServiceInstance> instances = discoveryClient.getInstances("config-server");

        return "Hello World";
    }


    public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
