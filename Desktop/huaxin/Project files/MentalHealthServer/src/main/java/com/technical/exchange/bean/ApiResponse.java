package com.technical.exchange.bean;


//应答类
public class ApiResponse<T> {

    public int code = 0;
    public String message;
    public T data;

    public ApiResponse() {
    }

    public ApiResponse<T> code(int code) {
        this.code = code;
        return this;
    }

    //失败应答
    public ApiResponse<T> fail() {
        this.code = -1;
        return this;
    }

    public ApiResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResponse<T> data(T data) {
        this.data = data;
        return this;
    }

    public static ApiResponse<String> defaultError(String message) {
        return new ApiResponse<String>().code(-1).message(message);
    }

    public static ApiResponse<String> defaultSuccess(String message) {
        return new ApiResponse<String>().message(message);
    }


    public static ApiResponse<String> defaultSuccess() {
        return new ApiResponse<String>().message("ok");
    }
}
