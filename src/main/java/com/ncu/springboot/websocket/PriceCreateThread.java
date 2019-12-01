package com.ncu.springboot.websocket;

import com.ncu.springboot.Application;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;

public class PriceCreateThread implements  Runnable {
    private  static final Logger LOG= LoggerFactory.getLogger(Application.class);
    @Override
    public void run() {
        Thread.currentThread().setName("T-Websocket");
        while(true){
            //每隔4-6秒就产生一个新价格
            int duration = 4000+new Random().nextInt(2000);
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            //新价格围绕100000左右50%波动
            Long newTime = System.currentTimeMillis();
            String strDate = DateFormatUtils.format(newTime, "yyyy/MM/dd HH:mm:ss");
            LOG.info("时钟---------{}-------------", strDate);
            //查看的人数
            int total = ServerManager.getTotal();
            String messageFormat = "{\"time\":\"%s\",\"total\":%d}";
            String message = String.format(messageFormat, strDate, total);
            //广播出去
            ServerManager.broadCast(message);
            //准备一个跳出事件
            if(newTime < 0) break;
        }
    }

}
