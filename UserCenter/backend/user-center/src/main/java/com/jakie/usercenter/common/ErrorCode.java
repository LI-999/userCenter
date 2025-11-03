package com.jakie.usercenter.common;

public enum ErrorCode {

    SUCCESS(20000,"请求成功",""),
    PARAM_ERROR(40001,"参数异常",""),
    NOT_LOGIN(40002,"未登录",""),
    NO_AUTH(40003,"权限不足",""),
    NULL_PARAM(40004,"参数为空",""),
    SYSTEM_ERROR(50000,"系统错误",""),
    NOT_EXISTS(50010,"查询结果为空",""),
    ALREADY_EXISTS(50011,"已存在",""),
    ACCOUNT_ERROR(50012,"账户异常",""),
    INSERT_ERROR(50020,"添加失败","");

    private final int code;
    private final String message;
    private final String description;



    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
