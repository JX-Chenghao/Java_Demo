package com.ncu.springboot.mvc.config;

import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.impl.FishEyeGimpy;
import com.google.code.kaptcha.util.Config;
import com.ncu.springboot.mvc.security.CaptchaValidateFilter;
import com.ncu.springboot.mvc.security.UserCredentialMatcher;
import com.ncu.springboot.mvc.security.UserDbRealm;
import com.ncu.springboot.mvc.security.UserFormAuthenticationFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfig {
    private  static final Logger LOG= LoggerFactory.getLogger(ShiroConfig.class);
    private static final String SHIRO_LOGIN_FAILURE = "shiroLoginFailure";
    @Bean
    public UserDbRealm realm(){
        LOG.info("Shiro - Realm 建立");
        UserDbRealm userDbRealm = new UserDbRealm();
        UserCredentialMatcher matcher = new UserCredentialMatcher();
        //storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        matcher.setStoredCredentialsHexEncoded(true);
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(2);
        userDbRealm.setCredentialsMatcher(matcher);
        LOG.info("Shiro - Realm.CredentialMatcher 设置完成");
        return userDbRealm;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        LOG.info("Shiro - SecurityManager 建立");
        LOG.info("Shiro - SecurityManager.Realm 设置完成");
        //将SecurityManager设置到SecurityUtils 方便全局使用
        SecurityUtils.setSecurityManager(securityManager);
        // 自定义session管理 使用redis
        //securityManager.setSessionManager(sessionManager());
        return  securityManager;

    }
    @Bean
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());
        Map<String, Filter> filters = new HashMap<>();
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl("/loginPage");
        CaptchaValidateFilter captchaValidateFilter = new CaptchaValidateFilter();
        captchaValidateFilter.setCaptchaEnabled(true);
        captchaValidateFilter.setCaptchaParam("kaptcha");
        UserFormAuthenticationFilter userFormAuthenticationFilter = new UserFormAuthenticationFilter();
        userFormAuthenticationFilter.setFailureKeyAttribute(SHIRO_LOGIN_FAILURE);
        userFormAuthenticationFilter.setUsernameParam("username");
        userFormAuthenticationFilter.setPasswordParam("password");
        UserFilter userFilter = new UserFilter();
        filters.put("logout",logoutFilter);
        filters.put("captchaValidate",captchaValidateFilter);
        filters.put("authc",userFormAuthenticationFilter);
        filters.put("user",userFilter);
        shiroFilter.setFilters(filters);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/loginPage", "anon");
        filterChainDefinitionMap.put("/login", "captchaValidate,authc");
        filterChainDefinitionMap.put("/captcha.jpg", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/**", "user");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilter.setLoginUrl("/login");
        return  shiroFilter;
    }
    @Bean
    public Producer kaptcha(){
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties p = new Properties();
        p.setProperty("kaptcha.image.width","100");
        p.setProperty("kaptcha.image.height","50");
        p.setProperty("kaptcha.border.thickness","3");
        p.setProperty("kaptcha.border.color", "240,255,255");
        p.setProperty("kaptcha.background.clear.from","orange");
        p.setProperty("kaptcha.background.clear.to","white");
        //p.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        //p.setProperty("kaptcha.fishEyeGimpy.impl","com.google.code.kaptcha.impl.FishEyeGimpy");
        p.setProperty("kaptcha.waterRipple.impl","com.google.code.kaptcha.impl.WaterRipple");
        p.setProperty("kaptcha.textproducer.char.string","0123456789abcdefghijklmnopqrstuvwxyz");
        p.setProperty("kaptcha.textproducer.char.length","4");
        kaptcha.setConfig(new Config(p));
        return kaptcha;
    }
/*
    kaptcha.border  是否有边框  默认为true  我们可以自己设置yes，no
    kaptcha.border.color   边框颜色   默认为Color.BLACK
    kaptcha.border.thickness  边框粗细度  默认为1
    kaptcha.producer.impl   验证码生成器  默认为DefaultKaptcha
    kaptcha.textproducer.impl   验证码文本生成器  默认为DefaultTextCreator
    kaptcha.textproducer.char.string   验证码文本字符内容范围  默认为abcde2345678gfynmnpwx
    kaptcha.textproducer.char.length   验证码文本字符长度  默认为5
    kaptcha.textproducer.font.names    验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
    kaptcha.textproducer.font.size   验证码文本字符大小  默认为40
    kaptcha.textproducer.font.color  验证码文本字符颜色  默认为Color.BLACK
    kaptcha.textproducer.char.space  验证码文本字符间距  默认为2
    kaptcha.noise.impl    验证码噪点生成对象  默认为DefaultNoise
    kaptcha.noise.color   验证码噪点颜色   默认为Color.BLACK
    kaptcha.obscurificator.impl   验证码样式引擎  默认为WaterRipple
    kaptcha.word.impl   验证码文本字符渲染   默认为DefaultWordRenderer
    kaptcha.background.impl   验证码背景生成器   默认为DefaultBackground
    kaptcha.background.clear.from   验证码背景颜色渐进   默认为Color.LIGHT_GRAY
    kaptcha.background.clear.to   验证码背景颜色渐进   默认为Color.WHITE
    kaptcha.image.width   验证码图片宽度  默认为200
    kaptcha.image.height  验证码图片高度  默认为50
*/


}
