package com.zknet.gateway.config;

import com.zknet.gateway.config.interceptor.RequestHeaderCheckInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@SpringBootConfiguration
public class CustomWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Resource
    private RequestHeaderCheckInterceptor requestHeaderCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestHeaderCheckInterceptor).addPathPatterns("/**");
    }
}
