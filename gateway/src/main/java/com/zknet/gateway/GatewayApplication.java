package com.zknet.gateway;

import com.zknet.gateway.config.filter.CustomFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@EnableCaching
@MapperScan("com.zknet.engine.mapper")
@SpringBootApplication(scanBasePackages = "com.zknet")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }

    @Bean
    public FilterRegistrationBean<CustomFilter> Filters() {
        FilterRegistrationBean<CustomFilter> registrationBean = new FilterRegistrationBean<CustomFilter>();
        registrationBean.setFilter(new CustomFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("CustomFilter");
        return registrationBean;
    }
}
