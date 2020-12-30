package com.hxz.base.handler;
/*
* 全局异常处理类
* */

import com.hxz.response.Result;
import com.hxz.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*
    * 全局异常处理，没有指定异常类型，不管什么类型都可以捕获
    * */

    /*这里如果不加@ResponseBody会无法抓捕controller中的异常，原因是controller中的异常被封装过的，是json格式*/

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
//        e.printStackTrace();
        log.error(e.getMessage());
        return Result.error();

    }



    /*
    * 接下来我们进行指定的异常捕获
    * 比如接下来这个就是算数异常捕获
    * */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
//        e.printStackTrace();
        log.error(e.getMessage());
        return Result.error().code(ResultCode.ARITHMETIC_EXCEPTION.getCode())
                .message(ResultCode.ARITHMETIC_EXCEPTION.getMessage());

    }



    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result error(BusinessException e){
//        e.printStackTrace();
        log.error(e.getErrMsg());
        return Result.error().code(e.getCode())
                .message(e.getErrMsg());
    }
}
