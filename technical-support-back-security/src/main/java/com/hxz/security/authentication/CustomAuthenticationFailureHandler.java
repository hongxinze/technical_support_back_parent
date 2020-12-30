package com.hxz.security.authentication;

import com.hxz.base.response.Result;
import com.hxz.base.response.ResultCode;
import com.hxz.security.properties.LoginResponseType;
import com.hxz.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("customAuthenticationFailureHandler")
@Slf4j
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {



    @Autowired
    SecurityProperties securityProperties;



    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if(LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())){

            //        认证失败响应JSON字符串
            Result result = Result.build(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());

            //将json字符串响应到前端
            response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(json);
            response.getWriter().write(result.toJsonString());

        }else{
            //配置登录校验失败返回页面，（该页面需要返回错误信息）（这种写法写死了登录失败的路径，导致只能调到账号密码登录的路径）
//            super.setDefaultFailureUrl(securityProperties.getAuthentication().getLoginPage()+"?error");

            //通过获取上一次的请求路径
            String referer = request.getHeader("Referer");
            log.info("referer:"+referer);

            //如果下面优质，则认为是多段登录，直接返回一个登录地址
            Object toAuthentication=request.getAttribute("toAuthentication");

            String lastUrl = toAuthentication !=null?securityProperties.getAuthentication().getLoginPage()
                    : StringUtils.substringBefore(referer,"?");
            log.info("上一次请求"+lastUrl);
            //设置默认的登录校验失败的返回页面
            super.setDefaultFailureUrl(lastUrl+"?error");

            super.onAuthenticationFailure(request,response,exception);
        }


    }
}
