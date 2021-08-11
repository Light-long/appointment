package com.terninus.sms.service;

public interface SmsService {

    // 发送手机验证码
    boolean send(String phone, String code);
}
