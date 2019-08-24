package com.ncu.springboot.rabbitmq;

import com.ncu.springboot.mvc.exception.OwnException;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class RabbitMqConsumer {
    private  final Logger Log= LoggerFactory.getLogger(this.getClass());
    @RabbitListener(queues = "spt_queue_test1")
    @RabbitHandler
    public void process1(String msg, Message message, Channel channel) {
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        Log.info("Queue spt_queue_test1 : {} 中物品仓库成功出货成功！", msg);
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            // TODO 容错处理,比如转移当前消息进入其它队列 或 数据库存储处理失败消息 重试
        }
        //可以发送短信提示该用户 订单出货成功
    }

    //spt_queue_test2 绑定了死信队列 异常将会丢到死信队列
    @RabbitListener(queues = "spt_queue_test2")
    @RabbitHandler
    public void process2(String msg, Message message, Channel channel) {
        try{
            BigDecimal res = new BigDecimal(10).divide(BigDecimal.ZERO);
            Log.info("Queue spt_queue_test2 : {} 中物品仓库成功出货成功！", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //可以发送短信提示该用户 订单出货成功
        }catch(Exception e){
            Log.warn("Queue spt_queue_test2 Consumed exception rabbitmq message: [{}]",msg, e);
           try {
                //进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e1) {
                Log.warn("IOException:basicReject {}",e1);
                // TODO 容错处理,比如转移当前消息进入其它队列 或 数据库存储处理失败消息 重试
            }
/*
            AcknowledgeMode.AUTO 根据方法的执行情况来决定是否确认还是拒绝（是否重新入queue）
            如果消息成功被消费（成功的意思就是在消费的过程中没有抛出异常），则自动确认。
            1）当抛出AmqpRejectAndDontRequeueException异常的时候，则消息会被拒绝，且requeue=false（不重新入队列）
            2）当抛出ImmediateAcknowledgeAmqpException异常，则消费者会被确认
            3）其他的异常，则消息会被拒绝，且requeue=true（如果此时只有一个消费者监听该队列，则有发生死循环的风险，多消费端也会造成资源的极大浪费，这个在开发过程中一定要避免的）。可以通过setDefaultRequeueRejected（默认是true）去设置，

            throw new OwnException(e);
 */
        }
    }

    @RabbitListener(queues = "spt_queue_test3")
    @RabbitHandler
    public void process3(String msg) {
        Log.info("Queue spt_queue_test3 : {} 中物品仓库成功出货成功！", msg);
        //可以发送短信提示该用户 订单出货成功
    }

/*
    @RabbitListener(queues = "direct_dead_queue")
    @RabbitHandler
    public void processDeadLetterQueue(String msg) {
        Log.info("死信队列 direct_dead_queue 处理message: {} ！", msg);
    }*/
}
