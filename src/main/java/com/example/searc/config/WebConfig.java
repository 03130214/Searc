package com.example.searc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 应用于所有API路径
            .allowedOrigins("http://localhost:3000") // 只允许来自前端应用域名和端口的请求
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 允许的HTTP方法
            .allowedHeaders("*") // 允许的HTTP头
            .allowCredentials(true) // 允许携带凭证，如Cookies
            .maxAge(3600); // 预检请求的有效期
    }
}
