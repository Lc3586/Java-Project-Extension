package project.extension.redis.configure;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import project.extension.redis.config.NaiveRedisConfigure;
import project.extension.redis.dto.TestDTO;

/**
 * Redis配置类
 *
 * @author LCTR
 * @date 2023-03-29
 */
@Configuration
@EnableCaching
public class TestNaiveRedisConfigure
        extends NaiveRedisConfigure {
    /**
     * 配置redis
     *
     * @param connectionFactory 连接工厂
     */
    @Bean
    public RedisTemplate<String, TestDTO> redisTemplate(RedisConnectionFactory connectionFactory) {
        return super.redisTemplate(connectionFactory,
                                   TestDTO.class);
    }

    /**
     * 配置限流脚本
     */
    @Bean
    public DefaultRedisScript<Boolean> limitScript() {
        return super.limitScript();
    }
}
