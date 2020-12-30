package com.hxz.security.authentication.mobile;

import lombok.extern.slf4j.Slf4j;

/*
* 发送短信验证码，第三方的短信服务接口
* */
//@Component
@Slf4j
public class SmsCodeSender implements SmsSend{

    /*
    * 以下这个方法是模拟第三方接口发送短信到手机
    * 会在这个方法获取短信的内容
    * */

    @Override
    public boolean sendSms(String mobile, String content) {
        //编辑向手机号码发送的真正的文本
        String sendContent = String.format("后台手机号码登录,验证码%s,请勿泄露给他人，", content);

        //调动第三方发送功能的sdk
        log.info("向手机号码："+mobile+"发送的短信内容为:"+sendContent);



        return true;
    }
}
