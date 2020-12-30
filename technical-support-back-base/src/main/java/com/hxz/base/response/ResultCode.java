package com.hxz.base.response;

public enum ResultCode implements CustomizeResultCode{
    /*
    * 200:成功
    * */
    SUCCESS(200,"成功"),
    /*失败*/
    ERROR(300,"失败"),
    /*
    * 305:密码不正确
    * */
    PASS_NOT_CORRECT(301,"密码不正确，请重新尝试"),
    PAGE_NOT_FOUND(404,"你输入的路径未找到，它可能流远了"),
    INTERNAL_SERVER_ERROR(500,"服务器沉入大海了，请赶紧联系管理员去捞它"),
    ARITHMETIC_EXCEPTION(320,"出现算数异常"),
    USER_NOT_FOUND_EXCEPTION(321,"用户不存在")
    ;



    private Integer code;
    private String message;

    ResultCode(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
