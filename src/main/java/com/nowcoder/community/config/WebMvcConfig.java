package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tonngw
 * @date 2022-01-15 11:49
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private AlphaInterceptor alphaInterceptor;
    
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    // 改用 Spring Security 做认证和授权
    // @Autowired
    // private LoginRequiredInterceptor loginRequiredInterceptor;
    
    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/.css", "/**/.js", "/**/.png", "/**/.jpg", "/**/.jpeg")
                .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/.css", "/**/.js", "/**/.png", "/**/.jpg", "/**/.jpeg");

        // registry.addInterceptor(loginRequiredInterceptor)
        //         .excludePathPatterns("/**/.css", "/**/.js", "/**/.png", "/**/.jpg", "/**/.jpeg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/.css", "/**/.js", "/**/.png", "/**/.jpg", "/**/.jpeg");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/.css", "/**/.js", "/**/.png", "/**/.jpg", "/**/.jpeg");
    }
}
