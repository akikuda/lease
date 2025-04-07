package com.toki;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author toki
 */
@SpringBootApplication
@Slf4j
public class AppWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppWebApplication.class);
        log.info("移动端APP项目启动成功~~QAQ~~");
    }
}