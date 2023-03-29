package project.extension.redis.config;

import com.alibaba.fastjson2.support.spring.data.redis.FastJsonRedisSerializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import project.extension.redis.extension.requestRateLimit.RequestRateLimitScript;

/**
 * 初级Redis配置类
 *
 * @author LCTR
 * @date 2023-03-29
 */
public class NaiveRedisConfigure
        extends CachingConfigurerSupport {
    /**
     * 配置redis
     *
     * @param <T>               数据类型
     * @param connectionFactory 连接工厂
     * @param type              数据类型
     */
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory,
                                                      Class<T> type) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        //设置连接工厂
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerializer<T> serializer = new FastJsonRedisSerializer<>(type);
        //key采用String的序列化
        template.setKeySerializer(new StringRedisSerializer());
        //value采用FastJson2序列化
        template.setValueSerializer(serializer);
        //Hash key采用String的序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        //Hash value采用FastJson2序列化
        template.setHashValueSerializer(serializer);
        //执行函数，初始化RedisTemplate
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置限流脚本
     */
    public DefaultRedisScript<Boolean> limitScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(RequestRateLimitScript.getScriptText());
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }
}
