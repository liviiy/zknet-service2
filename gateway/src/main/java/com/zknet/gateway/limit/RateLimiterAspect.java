package com.zknet.gateway.limit;

import com.zknet.engine.utils.IpUtil;
import com.zknet.gateway.config.RequestHeaderContext;
import com.zknet.gateway.exception.BizCodeEnum;
import com.zknet.gateway.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Aspect
@Component
@Slf4j
public class RateLimiterAspect {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private RedisScript<Long> rateLimitScript;

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        String key = rateLimiter.key();
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        String combineKey = getCombineKey(rateLimiter, point);
        List<Object> keys = Collections.singletonList(combineKey);
        try {
            Long number = redisTemplate.execute(rateLimitScript, keys, count, time);
            if (number==null || number.intValue() > count) {
                log.warn("RATE LIMIT HAPPEN,KEY:{},METHOD:{}",keys,point.getSignature().getName());
                throw new BizException(BizCodeEnum.RATE_LIMIT);
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("rate limit internal error:",e);
            throw new BizException(BizCodeEnum.INTERNAL_ERROR);
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            stringBuffer.append(IpUtil.getIPAddress(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())).append("-");
        } else if (rateLimiter.limitType() == LimitType.API_KEY) {
            stringBuffer.append(RequestHeaderContext.getInstance().getApiKey()).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
//        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(method.getName());
        return stringBuffer.toString();
    }
}
