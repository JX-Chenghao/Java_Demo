package com.ncu.springboot;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import javax.sql.DataSource;
import java.util.Random;

@Controller
@SpringBootApplication
public class Application implements  Runnable{
    private  static final Logger LOG= LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private Environment env;

	// destroy-method="close"
    // 当数据库连接不使用的时候,就把该连接重新放到数据池中
    // 调用的是DruidDataSource的close()方法，
    // 如果返回类型为DataSource会被检查出没有close方法。可以改为DruidDataSource为返回类型
    // 也可以选择xml配置
    @Bean(destroyMethod="close" )
	public DataSource dataSource() {
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
        LOG.info("MYSQL数据源建立");
        return dataSource;
	}

	@Override
	public void run() {
	    Thread.currentThread().setName("T-Websocket");
		int bitPrice = 100000;
		while(true){
			//每隔1-3秒就产生一个新价格
			int duration = 1000+new Random().nextInt(2000);
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//新价格围绕100000左右50%波动
			float random = 1+(float) (Math.random()-0.5);
			int newPrice = (int) (bitPrice*random);
			LOG.info("新价格--------------------{}------------------------------------------------------------------",newPrice);
			//查看的人越多，价格越高
			int total = ServerManager.getTotal();
			newPrice = newPrice*total;
			String messageFormat = "{\"price\":\"%d\",\"total\":%d}";
			String message = String.format(messageFormat, newPrice,total);
			//广播出去
			ServerManager.broadCast(message);
			if(newPrice>1000000) break;
		}
	}



}
