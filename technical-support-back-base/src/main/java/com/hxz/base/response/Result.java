package com.hxz.base.response;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/*
* 公共返回结果
* */
@Data
public class Result {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回状态码")
    private Integer code;

    @ApiModelProperty(value = "返回信息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data =new HashMap<>();

    /*构造方法私有化，里面的方法都是静态方法，达到保护属性的作用*/
    public Result(){

    }

    public static Result ok(String message) {
        Result result=new Result();
        result.setMessage(message);
        return result;
    }

    /*这里是使用链式编程*/
    public static Result ok(){
        Result result=new Result();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        return result;
    }

    /*这里是使用链式编程*/
    public static Result error(){
        Result result=new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR.getCode());
        result.setMessage(ResultCode.ERROR.getMessage());
        return result;
    }

    public static Result build(Integer code, String message) {
        Result result=new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        return result;
    }




    /*自定义返回成功与否*/
    public Result success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    public Result data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public Result data(Map<String,Object> map){
        this.setData(map);
        return this;
    }




    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    /**
     * JSON字符串转成 ResultCode 对象
     * @param json
     * @return
     */
    public static Result format(String json) {
        try {
            return JSON.parseObject(json, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
