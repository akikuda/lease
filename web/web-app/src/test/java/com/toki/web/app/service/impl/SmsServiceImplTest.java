//package com.toki.web.app.service.impl;
//
//import cn.hutool.core.date.DateUtil;
//import com.toki.model.entity.UserInfo;
//import jakarta.annotation.Resource;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//
//@SpringBootTest
//class SmsServiceImplTest {
//
//    @Resource
//    private SmsServiceImpl smsService;
//
//    @Test
//    void sendCode() {
//        smsService.sendCode("17727295905", "1234");
//    }
//
//    @Test
//    void timeTest() {
//        // 获取当前时间
//        String formattedTime = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm");
//        // 打印格式化后的时间
//        System.out.println(formattedTime);
//    }
//}
