package com.ncu.springboot.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqProducer {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    private  final Logger Log= LoggerFactory.getLogger(this.getClass());
    public void buildMsgForTopic(String msg){

        Log.info("创建MQ topic_Exchange消息_等待处理");
        //rabbitTemplate.convertAndSend(/*交换机*/"spt.topic"
        //        ,/*路由key*/"topic.sbt.msg.user.create"/*匹配绑定的队列1*/
        //        ,/*消息体*/"XX对象创建订单: "+msg);

        rabbitTemplate.convertAndSend(/*交换机*/"spt.topic"
                ,/*路由key*/"topic.sbt.msg.cheng.create"/*匹配绑定的队列1和队列2*/
                ,/*消息体*/"XX对象创建订单: "+msg);

    }
    public void buildMsgForFanout(String msg){
        Log.info("创建MQ fanout_Exchange消息_等待处理");
        rabbitTemplate.convertAndSend(/*交换机*/"spt.fanout"
                ,/*路由key*/""/*广播,无视key*/
                ,/*消息体*/"XX对象创建订单: "+msg);
    }

}
