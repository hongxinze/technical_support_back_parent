package com.hxz.security.config;
/*
* 加载登录认证信息配置类（例如在登录验证失败是提示用户名和密码错误）
* */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ReloadMessageConfig {

    @Bean //这个注入spring容器是为了加载中文的国际认证提示信息
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
//国际化语jar包位置:Maven:org.springframework.security:spring-security-core:5.2.0.REKEASE
        //spring-security-core-5.2.0.RELEASE.jar
        //org.springframework.security.Resource Bundle "messages"
        //选择其中的messages_zh_CN.properties CN是中文版的意思 copy relatives
        // 复制相对路径粘贴，然后去除org之前的地址路径，因为jar包已经导入，所以不需要org之前的地址路径

        //注意：源码自带.properties后缀拼接，所以要删去.properties，否则会不生效
        /*messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");*/
        messageSource.setBasename("classpath:messages_zh_CN");
        return messageSource;
    }
}
