package com.zknet.engine.cache;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Slf4j
@Configuration
public class DefaultRedisCacheManager {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        template.setValueSerializer(jacksonSeial);
        template.setKeySerializer(new StringRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        log.info("redis config completed:{}", JSONUtil.toJsonStr(template));
        return template;
    }
    /**
     * set a CacheManager to use @Cacheable
     *
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {

        // 基本配置
        RedisCacheConfiguration defaultCacheConfiguration =
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                        // not cache null
//                        .disableCachingNullValues()
                        // default cache 1 hour
                        .entryTtl(Duration.ofHours(1));

        RedisCacheManager redisCacheManager =
                RedisCacheManager.RedisCacheManagerBuilder
                        .fromConnectionFactory(redisTemplate.getConnectionFactory())
                        .cacheDefaults(defaultCacheConfiguration)
                        .withCacheConfiguration(CacheConstants.L2_TOKEN_CACHE_NAME, getCacheConfigurationWithTtl(redisTemplate, 60L))
                        // WITH put/evict
                        .transactionAware()
                        .build();

        return redisCacheManager;
    }

    private RedisCacheConfiguration getCacheConfigurationWithTtl(RedisTemplate<String, Object> template, long ttlSeconds) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getStringSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getValueSerializer()))
//                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(ttlSeconds));
    }

}
