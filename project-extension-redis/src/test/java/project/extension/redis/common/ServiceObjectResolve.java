package project.extension.redis.common;

import org.junit.jupiter.api.Assertions;
import org.springframework.data.redis.core.RedisTemplate;
import project.extension.ioc.IOCExtension;
import project.extension.redis.dto.TestDTO;
import project.extension.redis.extension.requestRateLimit.RequestRateLimiterAspectHandler;

/**
 * 获取对象帮助类
 * <p>必须先执行injection方法</p>
 *
 * @author LCTR
 * @date 2023-03-17
 */
public class ServiceObjectResolve {
    public static RedisTemplate<String, TestDTO> redisTemplate;

    public static RequestRateLimiterAspectHandler requestRateLimiterAspectHandler;

    /**
     * 注入
     */
    public static void injection() {
        redisTemplate = (RedisTemplate<String, TestDTO>) IOCExtension.applicationContext.getBean("redisTemplate");

        Assertions.assertNotNull(redisTemplate,
                                 "未获取到RedisTemplate");

        System.out.printf("\r\n%s已注入\r\n",
                          RedisTemplate.class.getName());

        requestRateLimiterAspectHandler = IOCExtension.applicationContext.getBean(RequestRateLimiterAspectHandler.class);

        Assertions.assertNotNull(requestRateLimiterAspectHandler,
                                 "未获取到限制请求速率注解AOP处理类的RequestRateLimiterAspectHandler");

        System.out.printf("\r\n%s已注入\r\n",
                          RequestRateLimiterAspectHandler.class.getName());
    }
}
