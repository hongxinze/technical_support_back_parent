package com.hxz.security.authentication;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* 这个接口是用来监听认证成功之后的处理,也就是说认证成功后
* 让成功处理器调用此接口方法successListener
* */
public interface AuthenticationSuccessListener {

    void successListener(HttpServletRequest request,
                         HttpServletResponse response, Authentication authentication);
}
