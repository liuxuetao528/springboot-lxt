package com.oracle.springboot.lxt.vo;

/**
 * @Description:
 * @Author: Tu Xu
 * @CreateDate: 2021/10/25 11:12
 * @Version: 1.0
 **/
public class ResponseVo<T> {
    //状态码，标识此次请求的处理情况 200 400
    private String code;
    //响应的数据
    private T t;
    //提示消息
    private String message;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
