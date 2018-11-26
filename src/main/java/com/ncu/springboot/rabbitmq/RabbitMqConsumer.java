package com.ncu.springboot.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqConsumer {
    private  final Logger Log= LoggerFactory.getLogger(this.getClass());
    @RabbitListener(queues = "spt_queue_test1")
    @RabbitHandler
    public void process1(String msg) {
        Log.info("Queue spt_queue_test1 : {} 中物品仓库成功出货成功！", msg);
        //可以发送短信提示该用户 订单出货成功
    }

    @RabbitListener(queues = "spt_queue_test2")
    @RabbitHandler
    public void process2(String msg) {
        Log.info("Queue spt_queue_test2 : {} 中物品仓库成功出货成功！", msg);
        //可以发送短信提示该用户 订单出货成功
    }

    @RabbitListener(queues = "spt_queue_test3")
    @RabbitHandler
    public void process3(String msg) {
        Log.info("Queue spt_queue_test3 : {} 中物品仓库成功出货成功！", msg);
        //可以发送短信提示该用户 订单出货成功
    }
}
