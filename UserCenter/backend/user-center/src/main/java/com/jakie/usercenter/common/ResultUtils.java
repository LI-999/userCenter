package com.jakie.usercenter.common;

/**
 * 返回结果处理类
 */
public class ResultUtils {
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200,data,"请求成功");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     */
    public static BaseResponse error(ErrorCode errorCode,String description) {
        return new BaseResponse<>(errorCode, description);
    }

    /**
     * 失败
     */
    public static BaseResponse error(int errorCode,String message,String description) {
        return new BaseResponse(errorCode,null,message,description);
    }
}
