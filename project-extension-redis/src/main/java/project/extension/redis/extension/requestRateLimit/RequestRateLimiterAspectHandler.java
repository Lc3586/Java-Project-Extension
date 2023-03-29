package project.extension.redis.extension.requestRateLimit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.extension.cryptography.MD5Utils;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * 限制请求速率注解AOP处理类
 *
 * @author LCTR
 * @date 2023-03-29
 */
@Component
@Aspect
public class RequestRateLimiterAspectHandler {
    public RequestRateLimiterAspectHandler(RedisScript<Boolean> redisScript,
                                           RedisTemplate redisTemplate,
                                           RequestRateLimiterKeyProvider keyProvider) {
        this.redisScript = redisScript;
        this.redisTemplate = redisTemplate;
        this.keyProvider = keyProvider;
    }

    private final RedisScript<Boolean> redisScript;

    private final RedisTemplate redisTemplate;

    public final RequestRateLimiterKeyProvider keyProvider;

    private final Logger logger = LoggerFactory.getLogger(RequestRateLimiterAspectHandler.class);

    @Around(value = "@annotation(requestRateLimiter)",
            argNames = "point,requestRateLimiter")
    public Object around(ProceedingJoinPoint point,
                         RequestRateLimiter requestRateLimiter)
            throws
            Throwable {
        isAllow(point,
                requestRateLimiter);
        return point.proceed();
    }

    @Around(value = " @annotation(requestRateLimiters)",
            argNames = "point,requestRateLimiters")
    public Object around(ProceedingJoinPoint point,
                         RequestRateLimiters requestRateLimiters)
            throws
            Throwable {
        RequestRateLimiter[] arrays = requestRateLimiters.value();
        for (RequestRateLimiter item : arrays) {
            isAllow(point,
                    item);
        }
        return point.proceed();
    }

    /**
     * 是否允许请求
     */
    private void isAllow(ProceedingJoinPoint point,
                         RequestRateLimiter rateLimiter) {
        // 获取key
        String key = keyProvider.getKey(point,
                                        rateLimiter);
        // 类路径+方法，然后计算md5
        String uniqueKey = getUniqueKey((MethodSignature) point.getSignature());
        // key名称
        key = StringUtils.hasText(key)
              ? uniqueKey + "." + key
              : uniqueKey;
        // 拼接成最后的Redis的键,传入需要操作的key到lua脚本中
        List<String> operateKeys = getOperateKeys(key);
        // 执行lua脚本
        Boolean allowed = (Boolean) this.redisTemplate.execute(redisScript,
                                                               operateKeys,
                                                               rateLimiter.capacity(),
                                                               rateLimiter.rate(),
                                                               Instant.now()
                                                                      .getEpochSecond(),
                                                               1);
        logger.info("rateLimiter {}, result is {}",
                    key,
                    allowed);
        if (Boolean.FALSE.equals(allowed)) {
            logger.warn("触发限流，key is : {} ",
                        key);
            throw new RequestRateLimitException(rateLimiter.showPromptMsg());
        }
    }

    /**
     * 获取方法的唯一标识
     */
    private String getUniqueKey(MethodSignature signature) {
        String format = String.format("%s.%s",
                                      signature.getDeclaringTypeName(),
                                      signature.getMethod()
                                               .getName());
        return MD5Utils.hash(format);
    }

    /**
     * 获取操作标识集合
     */
    private List<String> getOperateKeys(String id) {
        String tokenKey = "request_rate_limiter.{" + id + "}.token";
        String timestampKey = "request_rate_limiter.{" + id + "}.timestamp";
        return Arrays.asList(tokenKey,
                             timestampKey);
    }
}
