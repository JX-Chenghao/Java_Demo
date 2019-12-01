package com.ncu.springboot.mvc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ncu.springboot.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
/*配置类上来开启声明式事务的支持  经过测试，发现不开启此注解也能成功开启
   经查询资料发现 Application启动类上 存在 @SpringBootApplication 注解
   @SpringBootApplication 配置了 @EnableAutoConfiguration 注解
   @EnableAutoConfiguration 注解开启，则 @EnableTransactionManagement 无需配置，声明式事务已支持
   Aop 顺序 Order若需配置在这 默认2147483647
   */
public class DbConfig implements TransactionManagementConfigurer{
    private  static final Logger LOG= LoggerFactory.getLogger(DbConfig.class);
    private final Environment env;

    @Autowired
    public  DbConfig(Environment env) {
        this.env = env;
    }


    @Bean(name="dataSource")
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
        dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setInitialSize(2);//初始化时建立物理连接的个数
        dataSource.setMaxActive(20);//最大连接池数量
        dataSource.setMinIdle(0);//最小连接池数量
        dataSource.setMaxWait(60000);//获取连接时最大等待时间，单位毫秒。
        dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效的sql
        dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效
        dataSource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。
        dataSource.setPoolPreparedStatements(false);//是否缓存preparedStatement，也就是PSCache
        LOG.info("MYSQL数据源建立: {} ",env.getProperty("spring.datasource.username"));
        return dataSource;
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
