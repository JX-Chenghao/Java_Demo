package com.ncu.springboot.mvc.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    // 死信的交换机名
    private final String DEAD_LETTER_EXCHANGE = "spt.topic.dead_exchange";
    private final String DEAD_ROUTING_QUEUE = "direct_dead_queue";
    private final String DEAD_ROUTING_KEY = "direct_dead_routing_key";

    @Bean
    public Queue queueOne(){
        return new Queue("spt_queue_test1");
    }

    @Bean
    public Queue queueTwo(){
        Map<String,Object> args=new HashMap<>();
        // 设置此Queue的死信的信箱
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // 设置死信routingKey
        args.put("x-dead-letter-routing-key",DEAD_ROUTING_KEY);

        return new Queue("spt_queue_test2",true,false,false, args);
    }

    @Bean
    public Queue queueThree(){
        return new Queue("spt_queue_test3");
    }


    @Bean
    public TopicExchange topicExchange(){
        //绑定队列1(topic.sbt.msg.#) 队列2(topic.sbt.msg.cheng.*)
        return  new TopicExchange("spt.topic");
    }

    @Bean
    public DirectExchange directExchange(){
        //绑定队列1(direct_routingKey_red) 队列2 (direct_routingKey_dark)
        return  new DirectExchange("spt.direct");
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        //绑了队列1 队列2 队列3 (广播)
        return  new FanoutExchange("spt.fanout");
    }

    @Bean
    public Binding bindingTopic1(){
        //通配符匹配 * #
        return BindingBuilder.bind(queueOne()).to(topicExchange()).with("topic.sbt.msg.#");
    }

    @Bean
    public Binding bindingTopic2(){
        //通配符匹配 * #
        return BindingBuilder.bind(queueTwo()).to(topicExchange()).with("topic.sbt.msg.cheng.*");
    }

    @Bean
    public Binding bindingDirect1(){
        //全文匹配
        return BindingBuilder.bind(queueOne()).to(directExchange()).with("direct_routingKey_red");
    }

    @Bean
    public Binding bindingDirect2(){
        //全文匹配
        return BindingBuilder.bind(queueTwo()).to(directExchange()).with("direct_routingKey_dark");
    }

    @Bean
    public Binding bindingFanout1(){
        //广播
        return BindingBuilder.bind(queueOne()).to(fanoutExchange());
    }
    @Bean
    public Binding bindingFanout2(){
        //广播
        return BindingBuilder.bind(queueTwo()).to(fanoutExchange());
    }
    @Bean
    public Binding bindingFanout3(){
        //广播
        return BindingBuilder.bind(queueThree()).to(fanoutExchange());
    }

    @Bean
    public Queue deadLetterQueue(){
        return new Queue(DEAD_ROUTING_QUEUE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Binding deadLetterBinding(){
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_ROUTING_KEY);
    }


}
