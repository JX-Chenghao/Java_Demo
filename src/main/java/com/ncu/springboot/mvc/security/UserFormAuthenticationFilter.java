package com.ncu.springboot.mvc.security;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 拦截器在url 进来处理前拦截，onPreHandler调用
 */
public class UserFormAuthenticationFilter extends FormAuthenticationFilter {
    private  static final Logger LOG= LoggerFactory.getLogger(UserFormAuthenticationFilter.class);
    //走到这个函数 代表 用户名/密码 验证失败
    // -- 假如一条过滤器链（captchaValidate,authc）的话
    //    -- captchaValidate  可能验证失败 可能验证成功 ，验证失败则会拦截掉，
    //       重写 isAccessAllow() 验证码正确返回true,返回true,就继续 走 authc
    //       验证码错误 返回 false, 所以我们需要重写onAccessDenied()来返回true，才能继续 走 authc
    //       往下走过滤链
    //
    //    -- authc 继承FormAuthenticationFilter，父类已重写两方法，isAccessAllow()里调用isAuthentic()
    //       对新用户/login来说，认证一定返回false，走到访问认证已失败函数onAccessDenied
    //       重写 onAccessDenied
    //            验证码已错的，保证他返回true，从而不在去验证用户名密码，而是继续走完调用链
    //       否则
    //           --如果用户名密码出错，将会抛出身份验证异常，前台不会告知用户 是验证码错误
    //           --如果用户名密码正确，就算验证码错误，也会使得登录成功
    //       对已登录用户/login来说，认证一定返回true，不会走到 onAccessDenied，
    //          后台拿到sessionid,找到连接，所以不会再次登录不会去管你输入的用户名密码，乱输都登录当前用户
    //          但是 如果用户名输入另一个用户 ，我们最好在controller 判断Subject中用户，直接返回有另一个用户存在，需要注销
    //          或者 要求进入controller登录时 我们首先先去注销Subject，等用户重新登录
    //为什么要这样 ，为什么不直接让captchaValidate过滤器返回false

    //因为 我们需要在表单提交中展示 验证码错误 这条信息，而不是直接拦截掉请求，我们需要使请求放行到 表单身份验证此处


    //onAccessDenied：表示访问拒绝时是否自己处理，
    //   如果返回true表示自己不处理且继续拦截器链执行，
    //   返回false表示自己已经处理了（比如重定向到另一个页面）
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (request.getAttribute(getFailureKeyAttribute()) != null) {
            return true;//onAccessDenied 中的 用户名密码验证 不再做
        }
        LOG.info("FilterChain - 表单验证");
        return super.onAccessDenied(request, response, mappedValue);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        /*
        protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        this.issueSuccessRedirect(request, response);
        return false;
        }
        */
        /*父类 */
        // 登录成功，onLoginSuccess 返回false,使过滤器过滤掉我们访问的login请求
          //不走controller login方法，直接自己进行跳转到 之前拦截的请求url 或 设置的 loginSuccessUrl
        // 登录失败  onLoginFailure 返回true，
          // 错误信息在session中的failureKeyAttribute 中 ,过滤器不过滤，走我们访问的login，
           // 我们只需要检查session 就行


        /*子类重写*/
        //onLoginSuccess 在 onAccessDenied 中访问
              //重写返回true ,代表登录访问并没有被过滤掉，继续走我们的login方法


        //返回true -> 那么登录的成功时候，onAccessDenied 返回true
        //-> 而 onPreHandle 中isAccessAllowed()||onAccessDenied()
        //   isAccessAllowed() 一定为 false 才会调用 onAccessDenied()
        //   onAccessDenied()  我们重写 登录成功时 一定true ，登录失败onLoginFailure 父类中 也是返回 true
        //   那么 controller login 方法一定会 调用
        // ->返回true-> 不过滤
        return true;
    }
}
