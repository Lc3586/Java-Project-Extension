package project.extension.redis.extension.requestRateLimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限制请求速率注解集合
 *
 * @author LCTR
 * @date 2023-03-29
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestRateLimiters {
    /**
     * 限制请求速率注解
     */
    RequestRateLimiter[] value();
}
