package com.ncu.springboot.mvc.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig  {
    private  static final Logger LOG= LoggerFactory.getLogger(RedissonConfig.class);
    private int timeout = 3000;

    private String address;

    private String password;

    private int database = 0;

    private int connectionPoolSize = 64;

    private int connectionMinimumIdleSize=10;

    private int slaveConnectionPoolSize = 250;

    private int masterConnectionPoolSize = 250;

    private String[] sentinelAddresses;

    private String masterName;


    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
         config.useClusterServers()
                 .setScanInterval(2000)//设置集群状态扫描时间
                 .setMasterConnectionPoolSize(10000)//设置连接数
                 .setSlaveConnectionPoolSize(10000)
                 .setKeepAlive(true)
                 .setConnectTimeout(30000)
                 .setPingConnectionInterval(30000)//避免 org.redisson.client.RedisResponseTimeoutException: Redis server response timeout (3000 ms) occured after 3 retry attempts
                 .addNodeAddress("redis://47.99.42.22:6380")
                 .addNodeAddress("redis://47.99.42.22:6379")//可以用"rediss://"来启用SSL连接
                 .addNodeAddress("redis://47.99.42.22:6381")
                 .addNodeAddress("redis://47.99.42.22:6479")
                 .addNodeAddress("redis://47.99.42.22:6480")
                 .addNodeAddress("redis://47.99.42.22:6481");
        LOG.info("RedissonClient Redis ClusterServers 连接集群成功");
        //config.useMasterSlaveServers();
        //config.useSingleServer().setAddress("redis://47.99.42.22:6379");
        return Redisson.create(config);
    }

}
