package com.ncu.springboot.mvc.security;

import com.ncu.springboot.service.AuthorizationService;
import com.ncu.springboot.mvc.helper.ExceptionStrPropertiesHelper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDbRealm extends AuthorizingRealm{
    @Autowired
    AuthorizationService authorizationService;
    private  static final Logger LOG = LoggerFactory.getLogger(UserDbRealm.class);
    @Override
    public String getName() {
        return "USER_REALM";
    }

    /**
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String)principalCollection.getPrimaryPrincipal();

        if (username == null) {
            throw new AuthorizationException(ExceptionStrPropertiesHelper.getInstance()
                    .getPropertiesValue("user_login_info_had_expired"));
        }
        //配置中已开启缓存
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(authorizationService.findRoles(username));
        authorizationInfo.setStringPermissions(authorizationService.findPermissions(username));

        return authorizationInfo;
    }

    /**
     * 认证（用户认证）
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)  {
        String username = (String)authenticationToken.getPrincipal();
        User user = authorizationService.findUserRolePermissionsByName(username);
        if(user == null) {
            LOG.info(ExceptionStrPropertiesHelper.getInstance().getPropertiesValue("user_does_not_exist_sys"));
            throw new AuthenticationException();
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
