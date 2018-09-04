package com.ncu.springboot.mvc.controller;

import com.ncu.springboot.Application;
import com.ncu.springboot.pojo.LearnResouce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {
    private  static final Logger LOG= LoggerFactory.getLogger(ThymeleafController.class);
    private static  boolean START_FLAG=false;

    @RequestMapping("/websocket")
    public ModelAndView index(){
        if(!START_FLAG){
            new Thread(new Application()).start();
            START_FLAG=true;
            LOG.info("开启新线程实时推送价格");
        }
        List<LearnResouce> learnList =new ArrayList<LearnResouce>();
        LearnResouce bean =new LearnResouce("参考文档","Spring Boot Reference Guide","http://docs.spring.io/spring-boot/docs/1.5.1.RELEASE/reference/htmlsingle/#getting-started-first-application");
        learnList.add(bean);
        bean =new LearnResouce("CCCCC","百度","https://www.baidu.com/");
        learnList.add(bean);
        bean =new LearnResouce("程序猿","谷歌","https://www.google.com/");
        learnList.add(bean);
        bean =new LearnResouce("HHHHH","IE","http://www.baidu.com");
        learnList.add(bean);
        ModelAndView modelAndView = new ModelAndView("/indexThymeleaf");
        modelAndView.addObject("learnList", learnList);
        return modelAndView;
    }
}
