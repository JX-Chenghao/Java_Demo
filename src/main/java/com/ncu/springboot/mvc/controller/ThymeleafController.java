package com.ncu.springboot.mvc.controller;

import com.ncu.springboot.pojo.User;
import com.ncu.springboot.service.AuthorizationService;
import com.ncu.springboot.service.UserService;
import com.ncu.springboot.websocket.PriceCreateThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;

@Controller
@RequestMapping("/thymeleaf")
@Slf4j
public class ThymeleafController {
    private static boolean START_FLAG = false;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserService userService;

    @RequestMapping("/websocket")
    public ModelAndView index() {
        log.info("价格实时展示");
        if (!START_FLAG) {
            new Thread(new PriceCreateThread()).start();
            START_FLAG = true;
            log.info("开启新线程实时推送价格");
        }
        User currentUser = userService.findUserByName(SecurityUtils.getSubject().getPrincipal().toString());

        ModelAndView modelAndView = new ModelAndView("/indexThymeleaf");
        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }
}
