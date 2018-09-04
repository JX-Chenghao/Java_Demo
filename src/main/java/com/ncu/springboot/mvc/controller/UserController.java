package com.ncu.springboot.mvc.controller;

import com.ncu.springboot.Application;
import com.ncu.springboot.mvc.exception.OwnException;
import com.ncu.springboot.pojo.LearnResouce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private  final Logger LOG= LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/login")
    public Map<String,String> login(HttpServletRequest request, String userName, String password){
        Map<String,String> map=new HashMap<>();
        if("".equals(userName) && "".equals(password)){
            LOG.info("用户名和密码错误");
            throw new OwnException("用户名和密码错误");
        }
        request.getSession().setAttribute("user","admin");
        map.put("result","true");
        return map;
    }
}
