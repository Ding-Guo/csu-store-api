package org.csu.api.config;

import org.csu.api.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: RQG
 * @Description: TODO
 * @Date 2023-03-20 21:50:52
 * @Version 1.0.0
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Bean
    public LoginInterceptor init() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(init());
        registration.addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register","/product/detail","/product/list");
    }
}
