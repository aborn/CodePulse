package com.github.aborn.codepulse.common.datatypes;

/**
 * 上报请求的基础Response
 * @author aborn (jiangguobao)
 * @date 2023/02/10 09:59
 */
public class BaseResponse<T> {
    T data;

    // 状态
    private boolean status;

    // 消息
    private String msg;

    // 状态码
    private int code;

    public BaseResponse(T data, boolean status, String msg, int code) {
        this.data = data;
        this.status = status;
        this.msg = msg;
        this.code = code;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data,true, "", 200);
    }

    public static <T> BaseResponse<T> successWithoutData() {
        return new BaseResponse<>(null,true, "无数据！", 201);
    }

    public static <T> BaseResponse<T> fail(String msg) {
        return new BaseResponse<>(null,false, msg, 400);
    }

    public static <T> BaseResponse<T> fail(String msg, int code) {
        return new BaseResponse<>(null,false, msg, code);
    }

    /**
     * 参数出错
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> failParams() {
        return new BaseResponse<>(null,true, "请求错误！", 505);
    }

    public T getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
