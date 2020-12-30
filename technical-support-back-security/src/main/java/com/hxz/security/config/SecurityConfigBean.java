package com.hxz.security.config;

import com.hxz.security.authentication.mobile.SmsCodeSender;
import com.hxz.security.authentication.mobile.SmsSend;
import com.hxz.security.authentication.session.CustomInvalidSessionStrategy;
import com.hxz.security.authentication.session.CustomSessionInformationExpiredStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/*
* 主要为容器中添加Bean实例
* */
@Configuration
public class SecurityConfigBean {


    /*
    * 当某一个时刻，一个账号超数登录（好比2个账号在异地登录）
    * 若果容器中有其他SessionInformationExpiredStrategy这个类型的实例，
    * 那么就会采用新的实例，不采用默认的实例
    * */
    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy(){
        return new CustomSessionInformationExpiredStrategy();
    }




    /*
    * 当session失效后的处理类
    * 如果容器中有其他InvalidSessionStrategy这个类型的实例
    * 那么就会采用新的实例，不采用默认的实例
    * */
    @Autowired
    private SessionRegistry sessionRegistry;

    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy(){
        return new CustomInvalidSessionStrategy(sessionRegistry);
    }




    //这种写法相当于在mobile中的SmsCodeSender类，给这个类加上@Component注解让其加入容器
    /*
    * @ConditionalOnMissingBean(SmsSend.class)
    * 这个注解的意思是，默认情况下采用的是SmsCodeSender实例，
    * 但是如果容器当中有其他SmsSend类型的实例，
    * 那当前的这个SmsCodeSender就失效了，会采用新的那个实例
    * */
    @Bean
    @ConditionalOnMissingBean(SmsSend.class)
    public SmsSend smsSend(){
        return new SmsCodeSender();
    }





}
