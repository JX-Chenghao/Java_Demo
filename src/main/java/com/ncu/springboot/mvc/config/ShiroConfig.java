package com.ncu.springboot.mvc.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.ncu.springboot.mvc.security.CaptchaValidateFilter;
import com.ncu.springboot.mvc.security.UserCredentialMatcher;
import com.ncu.springboot.mvc.security.UserDbRealm;
import com.ncu.springboot.mvc.security.UserFormAuthenticationFilter;
import com.ncu.springboot.mvc.security.redis.RedisCacheManager;
import com.ncu.springboot.mvc.security.redis.RedisManager;
import com.ncu.springboot.mvc.security.redis.RedisSessionDAO;
import com.ncu.springboot.mvc.security.redis.ShiroDefaultWebSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;

import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfig {
    private  static final Logger LOG= LoggerFactory.getLogger(ShiroConfig.class);
    private static final String SHIRO_LOGIN_FAILURE = "shiroLoginFailure";

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassWord;

    @Value("${server.servlet.session.cookie.name}")
    private String sessionIdCookieName;

    private String rememberMeCookieName = "springBoot-rememberMe";
    /*身份验证*/
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

    //一般的Cookie都是从document对象中获得的，现在浏览器在设置 Cookie的时候一般都接受
    // 一个叫做HttpOnly的参数，跟domain等其他参数一样，一旦这个HttpOnly被设置，你在浏
    // 览器的 document对象中就看不到Cookie了，而浏览器在浏览的时候不受任何影响，因为
    // Cookie会被放在浏览器头中发送出去(包括ajax的时 候)，应用程序也一般不会在js里操作
    // 这些敏感Cookie的，对于一些敏感的Cookie我们采用HttpOnly，对于一些需要在应用程序中
    // 用js操作的cookie我们就不予设置，这样就保障了Cookie信息的安全也保证了应用。
    @Bean
    public Cookie cookie(){
        SimpleCookie simpleCookie = new SimpleCookie(sessionIdCookieName);
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        return  simpleCookie;
    }

    @Bean
    public Cookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie(rememberMeCookieName);
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(3600*24*15);//15天
        return  simpleCookie;
    }


    /*
        访问一般网页，如个人在主页之类的，我们使用user拦截器即可，user
    拦截器只要用户登录(isRemembered()==true or isAuthenticated()==true)
    过即可访问成功；
        访问特殊网页，如我的订单，提交订单页面，我们使用authc拦截器即可，
    authc拦截器会判断用户是否是通过Subject.login（isAuthenticated()==true）
    登录的，如果是才放行，否则会跳转到登录页面叫你重新登录。
    因此RememberMe使用过程中，需要配合相应的拦截器来实现相应的功能*/
    @Bean
    public RememberMeManager rememberMeManager(){
        CookieRememberMeManager rememberMeManager=new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie());
        rememberMeManager.setCipherKey(Base64.decode("1AvVhmFLUs0KTA3Kprscat=="));
        return rememberMeManager;
    }

    /**
     * 配置 shiro redisManager
     * 其实使用的是 shiro-redis 开源插件
     */
    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost);
        if (StringUtils.isNotEmpty(redisPassWord)) {
            redisManager.setPassword(redisPassWord);
        }
        redisManager.setPort(redisPort);
        redisManager.setExpire(1800);//键值对 过期
        redisManager.setTimeout(10000);
        return  redisManager;
    }

    @Bean
    public SessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
    @Bean
    public CacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return  redisCacheManager;
    }

    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new ShiroDefaultWebSessionManager();
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(cookie());
        sessionManager.setGlobalSessionTimeout(180000);
        sessionManager.setSessionDAO(redisSessionDAO());
        return  sessionManager;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultSecurityManager securityManager = new DefaultWebSecurityManager();

        LOG.info("Shiro - SecurityManager 建立");
        LOG.info("Shiro - SecurityManager.Realm 设置完成");
        //将SecurityManager设置到SecurityUtils 方便全局使用
        SecurityUtils.setSecurityManager(securityManager);
        // 自定义session管理 使用redis,导致shiro 在请求处理中
        // 需要用到session 的时候都要从redis 中取数据并且反序列化

        // shiro 使用redis 频繁read session 以及 save session
        // 比如lastTime.. 都会去save
        securityManager.setSessionManager(sessionManager());

        //如果之后一直不去 保存KEY到redis  那么注释掉此行！！！！！！！
        securityManager.setCacheManager(cacheManager());

        //rememberMe
        securityManager.setRememberMeManager(rememberMeManager());

        securityManager.setRealm(realm());
        return  securityManager;

    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     * AuthorizationAttributeSourceAdvisor  通知器
     *
     *  （requiresRoles/requiresPermission/..）注解了的類和方法
     *   將會被此類找尋到
     *
     *   匹配所有类
     *   匹配所有加认证注解的方法
     *
     *   其他 框架的項目：<aop:config/>会扫描配置文件中的所有advisor，并为其创建代理。
     *   spring-boot項目： 需要 spring-boot-starter-aop 依賴支持
     */

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
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
        //表单登录认证过滤器
        UserFormAuthenticationFilter userFormAuthenticationFilter = new UserFormAuthenticationFilter();
        userFormAuthenticationFilter.setFailureKeyAttribute(SHIRO_LOGIN_FAILURE);
        userFormAuthenticationFilter.setUsernameParam("username");
        userFormAuthenticationFilter.setPasswordParam("password");
        userFormAuthenticationFilter.setRememberMeParam("rememberMe");
        UserFilter userFilter = new UserFilter();
        filters.put("logout",logoutFilter);
        filters.put("captchaValidate",captchaValidateFilter);
        filters.put("authc",userFormAuthenticationFilter);
        filters.put("user",userFilter);
        shiroFilter.setFilters(filters);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/loginPage", "anon");
        filterChainDefinitionMap.put("/login", "captchaValidate,authc");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/*.ico", "anon");
        filterChainDefinitionMap.put("/captcha.jpg", "anon");
        filterChainDefinitionMap.put("/cxf-services/**", "anon");
        //此url 依旧需要用户认证，只持有remberMe Cookie 无权访问
        filterChainDefinitionMap.put("/importantPage", "authc");//一旦这样(可以在重要页面，rememberMe不起作用，继续要求认证) 那么rememberMe 就算为true ,之后你再进来此页面依旧需要登录
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/**", "user");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilter.setLoginUrl("/login");
        return  shiroFilter;
    }

    /*验证码*/
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
