package com.hxz.security.authorize;

import com.hxz.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;


/*
* 身份认证相关的授权配置
*
* */
@Order(Integer.MAX_VALUE) //值越小，加载越优先，值越大，加载越靠后
@Component
public class CustomAuthorizeConfigurerProvider implements AuthorizeConfigurerProvider {
    @Autowired
    SecurityProperties securityProperties;

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
          config.antMatchers(securityProperties.getAuthentication().getLoginPage(),
                securityProperties.getAuthentication().getImageCodeUrl(),
                securityProperties.getAuthentication().getMobileCodeUrl(),
                securityProperties.getAuthentication().getMobilePage()
        ).permitAll();//放行/login/page不需要认证


        //所有其他请求都要通过身份认证
        config.anyRequest().authenticated();
    }
}
