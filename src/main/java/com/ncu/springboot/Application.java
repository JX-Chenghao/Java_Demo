package com.ncu.springboot;

import com.ncu.springboot.mvc.exception.OwnException;
import org.apache.tomcat.websocket.server.WsSci;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.websocket.WebSocketContainer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@SpringBootApplication
public class Application implements  Runnable{
    private  static final Logger LOG= LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
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
