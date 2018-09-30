package com.ncu.springboot.mvc.security;

import com.ncu.springboot.mvc.config.MyWebMvcConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private  static final Logger LOG= LoggerFactory.getLogger(MyWebMvcConfigurerAdapter.class);
    UserDetailsService userDetailsService = new OwnUserDetailService();
    //身份验证管理生成器
 /*   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //1.启用内存用户存储
//        auth.inMemoryAuthentication()
//                .withUser("xfy").password(passwordEncoder().encode("1234")).roles("ADMIN").and()
//                .withUser("tom").password(passwordEncoder().encode("1234")).roles("USER");
        //2.基于数据库表进行验证
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery("select username,password,enabled from user where username = ?")
//                .authoritiesByUsernameQuery("select username,rolename from role where username=?")
//                .passwordEncoder(passwordEncoder());
        //3.配置自定义的用户服务
        auth.userDetailsService(userDetailsService);

    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    //HTTP请求安全处理
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/index","/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/loginPage")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
    //WEB安全
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers( "/webjars/**");
    }
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
