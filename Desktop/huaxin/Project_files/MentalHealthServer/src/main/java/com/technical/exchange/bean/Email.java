package com.technical.exchange.bean;

public class Email {
    public String code;//验证码
    public long time;//过期时间

    //验证码有效判断
    public boolean normal() {
        return time >= System.currentTimeMillis();
    }
}
