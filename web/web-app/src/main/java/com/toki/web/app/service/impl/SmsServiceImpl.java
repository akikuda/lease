package com.toki.web.app.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.toki.common.config.AliyunSMSConfig;
import com.toki.common.config.AliyunSMSProperties;
import com.toki.web.app.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final Client client;

    @Override
    public void sendCode(String phone, String verifyCode) {
        SendSmsRequest smsRequest = new SendSmsRequest();
        smsRequest.setPhoneNumbers(phone);
        smsRequest.setSignName("阿里云短信测试");
        smsRequest.setTemplateCode("SMS_154950909");
        smsRequest.setTemplateParam("{\"code\":\"" + verifyCode + "\"}");
        try {
            client.sendSms(smsRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
