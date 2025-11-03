package com.jakie.usercenter.exception;

import com.jakie.usercenter.common.ErrorCode;

public class BusinessException extends RuntimeException {

    private final int code;
    private final String message;
    private final String description;

    public BusinessException(int code,String message,   String description) {
        super(message);
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
