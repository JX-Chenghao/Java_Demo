package com.ncu.springboot.mvc.config;

import com.ncu.springboot.mvc.security.UserCredentialMatcher;
import com.ncu.springboot.mvc.security.UserDbRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    private  static final Logger LOG= LoggerFactory.getLogger(ShiroConfig.class);
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
        filters.put("logout",logoutFilter);
        shiroFilter.setFilters(filters);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/loginPage", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilter.setLoginUrl("/loginPage");
        shiroFilter.setUnauthorizedUrl("/403");
        return  shiroFilter;
    }

}
