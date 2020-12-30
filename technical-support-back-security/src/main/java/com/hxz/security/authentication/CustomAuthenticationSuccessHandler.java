package com.hxz.security.authentication;


import com.alibaba.fastjson.JSON;
import com.hxz.base.response.Result;
import com.hxz.security.properties.LoginResponseType;
import com.hxz.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 认证成功处理器
* 1:决定 响应json还是跳转页面，或者认证成功后进行其他处理
*
* */
@Slf4j
@Component("customAuthenticationSuccessHandler")
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//        //该方法在默认情况下会调用下面这个3个参数的方法
//        //所以我们可以默认不修改这个方法
//    }

    @Autowired
    SecurityProperties securityProperties;

    @Autowired(required = false)//容器可以不需要有接口的实现，如果有则自动注入
    AuthenticationSuccessListener authenticationSuccessListener;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if(authenticationSuccessListener !=null){
            //当认证之后，调用次监听，进行后续处理，比如加载用户权限菜单
            authenticationSuccessListener.successListener(request,response,authentication);
        }


        if (LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())){
            //认证成功后，响应JSON字符串
            Result result= Result.ok("认证成功");

            //将result转换为json字符串
//        String json=result.toJsonString();

            //将json字符串响应到前端
            response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(json);
            response.getWriter().write(result.toJsonString());
        }else{
            //重定向到上次请求的地址上，引发跳转到认证页面的地址
            log.info("authentication"+ JSON.toJSONString(authentication));
            super.onAuthenticationSuccess(request,response,authentication);
        }



    }
}
