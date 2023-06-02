package project.extension.redis.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import project.extension.json.JSONUtils;
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
     * @param useJackson        使用Jackson
     */
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory,
                                                      Class<T> type,
                                                      boolean useJackson) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        //设置连接工厂
        template.setConnectionFactory(connectionFactory);

        if (useJackson) {
            Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(type);
            serializer.setObjectMapper(JSONUtils.getObjectMapper());
            //value采用FastJson2序列化
            template.setValueSerializer(serializer);
            //Hash value采用FastJson2序列化
            template.setHashValueSerializer(serializer);
        } else {
            FastJsonRedisSerializer<T> serializer = new FastJsonRedisSerializer<>(type);
            //value采用FastJson2序列化
            template.setValueSerializer(serializer);
            //Hash value采用FastJson2序列化
            template.setHashValueSerializer(serializer);
        }

        //key采用String的序列化
        template.setKeySerializer(new StringRedisSerializer());
        //Hash key采用String的序列化
        template.setHashKeySerializer(new StringRedisSerializer());
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
