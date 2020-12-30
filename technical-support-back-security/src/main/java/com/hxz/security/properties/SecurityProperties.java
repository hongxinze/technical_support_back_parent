package com.hxz.security.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component  //不能少，需要注入到容器内的配置
@ConfigurationProperties(prefix="hxz.security")
public class SecurityProperties {

    //会将hxz.security.authentication 下面的值绑定到AuthenticationProperties
    private  AuthenticationProperties authentication;

    public AuthenticationProperties getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthenticationProperties authentication) {
        this.authentication = authentication;
    }
}
