package com.toki.web.app.service.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmsServiceImplTest {

    @Resource
    private SmsServiceImpl smsService;

    @Test
    void sendCode() {
        smsService.sendCode("17727295905", "1234");
    }
}