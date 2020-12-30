package com.hxz.security.authentication.session;

import com.hxz.security.authentication.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/*
* 当同一个用户在相同时间的session达到指定的数量时，会执行这个类
* （即在某一时刻，一个用户的账号的登录数量达到指定数量，即超数时，会执行这个类）
* */
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;


    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        //1.获取用户名
        UserDetails userDetails = (UserDetails) event.getSessionInformation().getPrincipal();

        //%s是String.format的语法，调用的是后面,后面的参数
        AuthenticationServiceException exception =
                new AuthenticationServiceException(String.format("[%s] 该账户在另一处已登录，您已被下线",userDetails.getUsername()));

        try {
            //当用户在另外一台电脑登录后，交给失败处理器，回到认证页面
            event.getRequest().setAttribute("toAuthentication",true);
            customAuthenticationFailureHandler.onAuthenticationFailure(event.getRequest(),event.getResponse(),exception);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
