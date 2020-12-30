package com.hxz.security.properties;

/*
* 认证响应的类型（分为你需要返回json消息还是重定向到页面）
* */
public enum LoginResponseType {
    /*
    * 响应JSON字符串
    * */
    JSON,

    /*
    * 重定向地址
    * */
    REDIRECT
}
