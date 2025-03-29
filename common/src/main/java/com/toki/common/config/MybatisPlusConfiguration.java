package com.toki.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.toki.web.*.mapper")
public class MybatisPlusConfiguration {

}