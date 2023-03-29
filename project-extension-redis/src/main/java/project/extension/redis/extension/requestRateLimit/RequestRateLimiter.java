package project.extension.redis.extension.requestRateLimit;

import java.lang.annotation.*;

/**
 * 限制请求速率注解
 *
 * @author LCTR
 * @date 2023-03-29
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestRateLimiter {
    /**
     * Spell表达式
     * <p>用于Redis锁的键</p>
     */
    String[] keys() default {};

    /**
     * 令牌桶的容量
     *
     * @默认值 300
     */
    int capacity() default 300;

    /**
     * 生成令牌的速度
     *
     * @默认值 100
     */
    int rate() default 100;

    /**
     * 拒绝请求时的提示信息
     *
     * @默认值 操作太过频繁，请稍候再试
     */
    String showPromptMsg() default "操作太过频繁，请稍候再试";
}
