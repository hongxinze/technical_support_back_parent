package com.hxz.security.authentication.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* 自定义退出处理类
* 退出之后，将对应session从缓存中清除
*
* */
@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       Authentication authentication) {
        //退出之后，将对应session从缓存中清除
        sessionRegistry.removeSessionInformation(httpServletRequest.getSession().getId());

    }
}
