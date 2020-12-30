package com.hxz.security.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;


/*
* 将所有的授权配置统一的管理起来
* */
@Component
public class CustomAuthorizeConfigurerManager implements AuthorizeConfigurerManager{

    /*
    * 这么写是因为便于扩展
    * 能够快速获取所有的AuthorizeConfigurerProvider类
    * */

    @Autowired
    List<AuthorizeConfigurerProvider> authorizeConfigurerProviders;

    /*
    * 将一个个AuthorizeConfigurerProvider的实现类，传入配置的参数
    * */
    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        for (AuthorizeConfigurerProvider provider: authorizeConfigurerProviders){//遍历每一个Provider
            provider.configure(config);//调用provider，接收config参数
        }
    }
}
