package com.ncu.springboot.mvc.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ncu.springboot.aop.ApiInvokeTimeShow;
import com.ncu.springboot.pojo.ResponseVo;
import com.ncu.springboot.service.UserService;
import com.ncu.springboot.mvc.exception.OwnException;
import com.ncu.springboot.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private  final Logger LOG= LoggerFactory.getLogger(this.getClass());
    public static final String SHIRO_LOGIN_FAILURE = "shiroLoginFailure";
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptcha;


    @PostMapping("/login")
    @ResponseBody
    public ResponseVo login(HttpServletRequest request, String username, String password, Boolean rememberMe){
        LOG.info("账户登录");
        LOG.info("token.rememberMe-{}",rememberMe);
        ResponseVo response=new ResponseVo();

        Subject subject = SecurityUtils.getSubject();

        if(subject.isAuthenticated() && !subject.getPrincipal().equals(username)){
            throw new OwnException("當前瀏覽器已有另一個用戶登錄");
        }

        String strError = (String) request.getAttribute(SHIRO_LOGIN_FAILURE);
        if("captchaError".equals(strError)){
            throw new OwnException("验证码有误");
        }else if(IncorrectCredentialsException.class.getName().equals(strError)){
            throw new OwnException("[IncorrectCredentialsException]账户名或密码出错");
        }else if(ExcessiveAttemptsException.class.getName().equals(strError)){
            throw new OwnException("[ExcessiveAttemptsException]登录失败5次,账户:"+subject.getPrincipal()+"已被锁定");
        }else if(AuthenticationException.class.getName().equals(strError)){
            String[] split = strError.split("\\.");
            throw new OwnException("["+split[split.length-1]+"]账户 "+username+" 不存在");
        }else{
            response.setResult("true");
            LOG.info("账户登录成功,当前Shiro.Subject用户: {}",subject.getPrincipal());
            return response;
        }
           /*
            既然走了表单过滤器验证 那么subject.login(token)此代码
            已在FormAuthenticationFilter.onAccessDenied中调用
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
           */
    }

    @RequestMapping("/captcha.jpg")
    @ApiInvokeTimeShow
    public String captcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //客户端不存缓存，立马过期
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        // create the text for the image
        String capText = kaptcha.createText();
        // store the text in the session
        // 之后在过滤器中比对
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        // create the image with the text
        BufferedImage bi = kaptcha.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    @RequiresPermissions("user:find")
    @PostMapping("/user/find")
    @ApiInvokeTimeShow(methodName = "查询账户")
    public User find(String name) {
        return userService.findUserByName(name);
    }

    @RequiresPermissions("user:save")
    @PostMapping("/user/save")
    @ApiInvokeTimeShow(methodName = "注册账户")
    public Map<String,String> find(User user) {
        Map<String,String> map=new HashMap<>();
        userService.saveUser(user);
        map.put("result","true");
        return map;
    }

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
