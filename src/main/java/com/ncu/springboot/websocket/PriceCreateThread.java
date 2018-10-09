package com.ncu.springboot.websocket;

import com.ncu.springboot.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class PriceCreateThread implements  Runnable {
    private  static final Logger LOG= LoggerFactory.getLogger(Application.class);
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
