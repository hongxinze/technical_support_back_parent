package com.hxz.security.authentication.mobile;

/*
* 短信发送统一接口
* */
public interface SmsSend {

    /*
    * 发送短信
    * mobile 手机号
    * content 发送的内容
    * true表示发送成功，false表示发送失败
    *
    * */
    boolean sendSms(String mobile,String content);
}
