package com.jakie.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用响应的信息
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String msg;
    private String description;

    public BaseResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public BaseResponse(int code, T data, String msg, String description) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = description;
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMessage());
    }

    public BaseResponse(ErrorCode errorCode,String description) {
        this(errorCode.getCode(),null,errorCode.getMessage(),description);
    }

}
