package com.ncu.springboot.mvc.controller;

import com.ncu.springboot.mvc.exception.OwnException;
import com.ncu.springboot.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private  final Logger LOG= LoggerFactory.getLogger(this.getClass());
/*    @Autowired
    UserRepository userRepository;*/

    @PostMapping("/login")
    public Map<String,String> login(HttpServletRequest request, String userName, String password){
        Map<String,String> map=new HashMap<>();
        if("".equals(userName) && "".equals(password)){
            LOG.info("用户名和密码错误");
            throw new OwnException("用户名和密码错误");
        }
        request.getSession().setAttribute("user","admin");
        map.put("result","true");
        LOG.info("用户登录");
        return map;
    }
/*

    */
/**
     * 根据 id 查询用户
     * @param id
     * @param model
     * @return
     *//*

    @GetMapping("/user/{id}")
    public ModelAndView view(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user.get());
        model.addAttribute("title", "查看用户");
        return new ModelAndView("users/view","userModel",model);
    }
*/


}
