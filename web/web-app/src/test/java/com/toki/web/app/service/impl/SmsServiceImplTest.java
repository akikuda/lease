package com.toki.web.app.service.impl;

import com.toki.model.entity.UserInfo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmsServiceImplTest {

    @Resource
    private SmsServiceImpl smsService;

    @Test
    void sendCode() {
        smsService.sendCode("17727295905", "1234");
    }

    @Test
    void lombokTest() {
        final UserInfo userInfo = new UserInfo();
        userInfo.setPassword("password");
        System.out.println(userInfo.getPassword());
    }
}