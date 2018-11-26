package com.ncu.springboot.mvc.controller;

import com.ncu.springboot.rabbitmq.RabbitMqProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RabbitController {

    @Autowired
    RabbitMqProducer rabbitMqProducer;

    @ResponseBody
    @PostMapping("/order/msg/create1")
    public Map<String,String> createOrderMsg1(String msg){
        Map<String,String> map=new HashMap<>();
        rabbitMqProducer.buildMsgForTopic(msg);
        map.put("result","true");
        return map;
    }

    @ResponseBody
    @PostMapping("/order/msg/create2")
    public Map<String,String> createOrderMsg2(String msg){
        Map<String,String> map=new HashMap<>();
        rabbitMqProducer.buildMsgForFanout(msg);
        map.put("result","true");
        return map;
    }
}
