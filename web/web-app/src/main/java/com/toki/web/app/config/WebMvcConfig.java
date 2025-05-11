package com.toki.web.app.config;

import com.toki.web.app.interceptor.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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

    private final AuthenticationInterceptor authenticationInterceptor;
//    private final AiAuthenticationInterceptor aiAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authenticationInterceptor)
                .addPathPatterns("/app/**")
                .excludePathPatterns(
                        "/app/login/**",
                        "/app/ai/**"
                );
        // 添加AI专用拦截器
//        registry.addInterceptor(this.aiAuthenticationInterceptor)
//                .addPathPatterns("/app/ai/**");
    }
}





