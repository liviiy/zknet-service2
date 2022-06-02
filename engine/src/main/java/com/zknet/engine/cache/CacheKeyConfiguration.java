package com.zknet.engine.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheKeyConfiguration {

    @Bean
    public CacheKeyGenerator defaultCacheKeyGenerator() {
        return new CacheKeyGenerator();
    }
}
