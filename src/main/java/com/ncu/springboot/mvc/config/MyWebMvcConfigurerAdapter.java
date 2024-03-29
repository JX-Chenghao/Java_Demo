package com.ncu.springboot.mvc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

@EnableWebMvc
@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer /*extends WebMvcConfigurationSupport(导致自动配置失效)*/ {
    private static final Logger LOG = LoggerFactory.getLogger(MyWebMvcConfigurerAdapter.class);

    /*补充Controller*/
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //与AutoConfiguration同时生效
        registry.addRedirectViewController("/", "/thymeleaf/websocket");
        registry.addViewController("/loginPage").setViewName("/loginPage");
        registry.addViewController("/findPage").setViewName("/users/view");
        registry.addViewController("/savePage").setViewName("/users/view");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/")
                .resourceChain(false)
                .addResolver(new WebJarsResourceResolver())
                .addResolver(new PathResourceResolver());
    }

/*    *//*拦截器的添加*//*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                if (request.getSession().getAttribute("user") == null) {
                    LOG.info("------------------------------拦截---------------------------------------------[{}]", request.getRequestURI());
                    request.getRequestDispatcher("/toLoginView").forward(request, response);
                    return false;
                } else {
                    LOG.info("------------------------------放行---------------------------------------------[{}]", request.getRequestURI());
                    return true;
                }
            }
        }).addPathPatterns("/**").excludePathPatterns("/loginPage", "/index", "/login", "/webjars/**");
    }*/

}
