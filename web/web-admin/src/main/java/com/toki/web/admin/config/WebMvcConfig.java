package com.toki.web.admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * webMVC注册自定义配置
 * </p>
 *
 * @author toki
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final String2EnumConverterFactory String2EnumConverterFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 注册自定义转换器
        registry.addConverterFactory(this.String2EnumConverterFactory);
    }
}
