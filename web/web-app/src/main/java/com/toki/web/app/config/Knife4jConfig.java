package com.toki.web.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author toki
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("APP接口")
                        .version("1.0")
                        .description("用户端APP接口")
                        .termsOfService("http://doc.xiaominfo.com")
                        .license(new License().name("Apache 2.0")
                                .url("http://doc.xiaominfo.com"))
        );
    }


    @Bean
    public GroupedOpenApi loginAPI() {
        return GroupedOpenApi.builder().group("登录信息").
                pathsToMatch("/app/login/**", "/app/info").
                build();
    }

    @Bean
    public GroupedOpenApi personAPI() {
        return GroupedOpenApi.builder().group("个人信息").
                pathsToMatch(
                        "/app/history/**",
                        "/app/appointment/**",
                        "/app/agreement/**"
                ).
                build();
    }

    @Bean
    public GroupedOpenApi lookForRoomAPI() {
        return GroupedOpenApi.builder().group("找房信息").
                pathsToMatch(
                        "/app/apartment/**",
                        "/app/room/**",
                        "/app/payment/**",
                        "/app/region/**",
                        "/app/term/**"
                ).
                build();
    }

    @Bean
    public GroupedOpenApi groupBolgAPI() {
        return GroupedOpenApi.builder().group("圈子模块")
                .pathsToMatch(
                        "/app/group/**",
                        "/app/file/**"
                )
                .build();
    }
}