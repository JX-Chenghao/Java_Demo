package com.ncu.springboot.mvc.security;

import com.google.code.kaptcha.Constants;
import com.ncu.springboot.websocket.BitCoinWebSocketServer;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by bl02780 on 2017/4/23.
 */
public class CaptchaValidateFilter extends AccessControlFilter {
    private boolean captchaEnabled = true;   //是否开启验证码验证
    private String captchaParam = "kaptcha"; //前台提交的验证码参数名
    private String failureKeyAttribute = "shiroLoginFailure"; //验证失败后存储到的属性名
    private  static final Logger LOG= LoggerFactory.getLogger(CaptchaValidateFilter.class);
    public void setCaptchaEnabled(boolean captchaEnabled) {
        this.captchaEnabled = captchaEnabled;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }
    public void setFailureKeyAttribute(String failureKeyAttribute) {
        this.failureKeyAttribute = failureKeyAttribute;
    }
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        LOG.info("FilterChain - 验证码验证");
        //1、设置验证码是否开启属性，页面可以根据该属性来决定是否显示验证码
        request.setAttribute("captchaEnabled", captchaEnabled);

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //2、判断验证码是否禁用 或不是表单提交（允许访问）
        if (!captchaEnabled || !"post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            return true;
        }

        String kaptchaExpected = (String)httpServletRequest.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String kaptchaReceived = httpServletRequest.getParameter(captchaParam);

        //3、此时是表单提交，验证验证码是否正确
        if (kaptchaReceived == null
                || kaptchaExpected == null
                || !kaptchaReceived.toUpperCase().equals(kaptchaExpected.toUpperCase())) {
            LOG.info("FilterChain - 账户输入的验证码有误");
            return false;//返回验证码错误
        }
        return true;
    }
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //如果验证码失败了，存储失败key属性
        request.setAttribute(failureKeyAttribute, "captchaError");
        return true;
    }
}
