package com.hxz.security.controller;

import com.hxz.base.response.Result;
import com.hxz.security.authentication.mobile.SmsSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class MobileLoginController {

    public static final String SESSION_KEY ="SESSION_KEY_MOBILE_CODE";


    /*
     * 前往手机验证码登录页
     * */
    @RequestMapping("/mobile/page")
    public String toMobilePage(){
        return "login-mobile";
    }

    @Autowired
    SmsSend smsSend;

    /*
     *发送手机验证码
     * */
    @ResponseBody//响应JSON字符串
    @RequestMapping("/code/mobile")
    public Result smsCode(HttpServletRequest request){
        //1.生成手机验证码
        String code = RandomStringUtils.randomNumeric(4);
        log.info("MobileLoginController生成的验证码为"+code);
        //2.将手机验证码保存到session中
        request.getSession().setAttribute(SESSION_KEY,code);
        //3.获取用户输入的手机信息
        String mobile = request.getParameter("mobile");
        //发送验证码到用户手机上
        smsSend.sendSms(mobile,code);

        return Result.ok();
    }
}
