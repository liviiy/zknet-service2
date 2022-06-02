package com.zknet.gateway.limit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * the limit key
     */
    String key() default "rate_limit:";

    /**
     * limit time,time unit is seconds
     */
    int time() default 60;

    /**
     * the limit count
     */
    int count() default 100;

    /**
     * limit type
     */
    LimitType limitType() default LimitType.API_KEY;
}
