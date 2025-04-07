package com.toki.web.app.service;

/**
 * @author toki
 */
public interface SmsService {

    void sendCode(String phone, String verifyCode);
}
