package com.ncu.springboot.mvc.security;

import com.ncu.springboot.Service.AuthorizationService;
import com.ncu.springboot.mvc.exception.OwnException;
import com.ncu.springboot.pojo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDbRealm extends AuthorizingRealm{
    @Autowired
    AuthorizationService authorizationService;
    @Override
    public String getName() {
        return "USER_REALM";
    }

    /**
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /*String username = (String)principalCollection.getPrimaryPrincipal();

        if (username == null) {
            throw new AuthorizationException("执行的用户不存在");
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(authorizationService.findRoles(username));
        authorizationInfo.setStringPermissions(authorizationService.findPermissions(username));

        return authorizationInfo;*/
        return null;
    }

    /**
     * 认证（用户认证）
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)  {
        String username = (String)authenticationToken.getPrincipal();
        User user = authorizationService.findUserRolePermissionsByName(username);
        if(user == null) {
            throw new OwnException("不存在此帐号");
        }
        /*
          if(Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }*/
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        return new SimpleAuthenticationInfo(
                user.getName(), //用户名
                user.getPwd(), //密码
                ByteSource.Util.bytes(user.getCredentialSalt()),//salt=username+salt
                getName()  //realm name
        );
    }
}
