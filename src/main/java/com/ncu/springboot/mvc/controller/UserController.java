package com.ncu.springboot.mvc.controller;

import com.ncu.springboot.Service.UserService;
import com.ncu.springboot.mvc.exception.OwnException;
import com.ncu.springboot.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private  final Logger LOG= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
/*    @Autowired
    UserRepository userRepository;*/

    @PostMapping("/login")
    public Map<String,String> login(HttpServletRequest request, String userName, String password){

        /*Map<String,String> map=new HashMap<>();
        if("".equals(userName) && "".equals(password)){
            LOG.info("用户名和密码错误");
            throw new OwnException("用户名和密码错误");
        }
        request.getSession().setAttribute("user","admin");
        map.put("result","true");*/
        LOG.info("账户登录");
        Map<String,String> map=new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        try {
            subject.login(token);
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            throw new OwnException("账户名或密码出错");
        }catch (ExcessiveAttemptsException e){
            e.printStackTrace();
            throw new OwnException("登录失败5次,账户:"+subject.getPrincipal()+"已被锁定");
        }
        map.put("result","true");

        LOG.info("账户登录成功,当前Shiro.Subject用户 {}: ",subject.getPrincipal());
        return map;
    }



    @PostMapping("/user/find")
    public User find(String name) {
        LOG.info("查询账户");
        return userService.findUserByName(name);
    }

    @PostMapping("/user/save")
    public Map<String,String> find(User user) {
        Map<String,String> map=new HashMap<>();
        LOG.info("注册账户");
        userService.saveUser(user);
        map.put("result","true");
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
