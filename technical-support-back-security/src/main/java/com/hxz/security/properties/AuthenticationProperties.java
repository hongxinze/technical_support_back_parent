package com.hxz.security.properties;


import lombok.Data;

@Data
public class AuthenticationProperties {


    /*
    * 这里每个属性的值在application.yml中已经配置了，
    * 如果在这里配置属性值，那么application.yml中的默认值会被覆盖
    * */
    private String loginPage;
//    private String loginPage ="/login/page";

    private String loginProcessingUrl;

    private String usernameParameter;

    private String passwordParameter;

    private String[] staticPaths;

    //认证响应的类型
//    private LoginResponseType  loginType = LoginResponseType.REDIRECT;
    private LoginResponseType  loginType;

    //获取图形验证码地址
    private String imageCodeUrl; // ="/code/image";

    //发送手机验证码地址
    private String mobileCodeUrl; // ="/code/mobile";

    //前往手机登录页面
    private String mobilePage;  // ="/mobile/page";

    //记住我功能保存用户登录信息有效时长 #60*60*24*7 这里可以写前面的那个乘法格式
    private Integer tokenValiditySeconds; //= 604800;


}
