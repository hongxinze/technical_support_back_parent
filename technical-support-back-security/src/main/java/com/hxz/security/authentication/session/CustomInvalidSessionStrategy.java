package com.hxz.security.authentication.session;

import com.hxz.base.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 当session失效后的处理逻辑
* 即反馈保存用户信息超时，用户需要重新登录的类
* */
@Slf4j
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    private SessionRegistry sessionRegistry;

    public CustomInvalidSessionStrategy(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        log.info("request.getSession().getId()"+request.getSession().getId()+request.getSession(false).getId());//错误的sessionid方法
        log.info("request.getRequestedSessionId()"+request.getRequestedSessionId());
//        sessionRegistry.removeSessionInformation(request.getSession().getId());//错误的sessionid方法
        sessionRegistry.removeSessionInformation(request.getRequestedSessionId());

        //调用方法，将浏览器中cookie的jsessionid删除
        cancelCookie(request,response);

        Result result
                =new Result().build(HttpStatus.UNAUTHORIZED.value(),"登录已超时，请重新登录");

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(result.toJsonString());

    }
        //存在问题，超时后重新访问登录页也会一直显示已超时，原因是原cookie没有删除，需要删除
        //当你重启浏览器的时候，会生成一个新的cooke，所以就又可以重新访问登录页
        //所以如果要在显示超时以后重新访问登录页，那么就需要将浏览器中cookie的jsessionid删除
        protected void cancelCookie(HttpServletRequest request,HttpServletResponse response){
            Cookie cookie=new Cookie("JSESSIONID",null);
            cookie.setMaxAge(0);
            cookie.setPath(getCookiePath(request));
            response.addCookie(cookie);
        }

        private String getCookiePath(HttpServletRequest request){
            String contextPath=request.getContextPath();
            return contextPath.length() > 0 ? contextPath : "/";
        }

}
