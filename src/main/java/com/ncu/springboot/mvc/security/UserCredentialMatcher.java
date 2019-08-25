package com.ncu.springboot.mvc.security;

import com.ncu.springboot.mvc.config.ShiroConfig;
import com.ncu.springboot.mvc.helper.PasswordHelper;
import com.ncu.springboot.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  密码验证 ：实现 CredentialsMatcher接口 匹配用户输入的token的凭证（未加密）与系统提供的凭证（已加密） -如果密码加密的的话
 *  密码加密 ：实现 PasswordService   接口
 */
public class UserCredentialMatcher extends HashedCredentialsMatcher {
    @Autowired
    private PasswordHelper passwordHelper;
    private Map<String,AtomicInteger> passwordRetryCache =new HashMap<>();
    private  static final Logger LOG= LoggerFactory.getLogger(UserCredentialMatcher.class);
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info){
        //密码重试
        String username = (String)token.getPrincipal();
        //retry count + 1
        passwordRetryCache.putIfAbsent(username,new AtomicInteger(5));//如果没登录过 5次机会重试
        AtomicInteger retryCount = passwordRetryCache.get(username);
        LOG.info("用户:{} - 重试次数为{}",username,retryCount);
        if(retryCount.decrementAndGet() == -1) {
            //重试次数 已为0
            throw new ExcessiveAttemptsException();
        }
        //登录令牌里的用户信息（账号/密码）(如果realm未使用PasswordSerive加密，则可能是未加密的明文)
        //若是未加密，在此可以加密成 密文
        //String name = (String) token.getPrincipal(); 已取
        String password = token.getCredentials() == null ? null : String.valueOf((char[]) token.getCredentials());

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new IncorrectCredentialsException();
        }
        boolean matcheRes = super.doCredentialsMatch(token, info);
        if (matcheRes) {
            // clear retry count
            passwordRetryCache.remove(username);
            //认证虽然成功但 此时 subject 中shiro 用户仍为null
            LOG.info("认证成功,当前用户:{}", SecurityUtils.getSubject().getPrincipal());
        }
        LOG.info("CredentialMatcher 认证 {}",matcheRes ? "successful" : "failed");
        return matcheRes;
    }
}
