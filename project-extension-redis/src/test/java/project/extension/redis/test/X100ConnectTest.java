package project.extension.redis.test;

import org.junit.jupiter.api.*;
import org.springframework.data.redis.core.ValueOperations;
import project.extension.redis.common.AssertExtension;
import project.extension.redis.common.ServiceObjectResolve;
import project.extension.redis.dto.TestDTO;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 100.连接测试
 *
 * @author LCTR
 * @date 2023-03-29
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X100ConnectTest {
    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        ServiceObjectResolve.injection();
    }

    /**
     * 测试连接是否正常
     */
    @Test
    @DisplayName("100.测试连接是否正常")
    @Order(100)
    public void _100()
            throws
            Throwable {
        ValueOperations<String, TestDTO> operations = ServiceObjectResolve.redisTemplate.opsForValue();

        Assertions.assertNotNull(operations,
                                 "获取操作对象失败");

        System.out.println("已获取到操作对象");

        String key = UUID.randomUUID()
                         .toString();

        System.out.printf("\r\n已生成key: %s\r\n",
                          key);

        TestDTO testData = TestDTO.getInstance();

        operations.set(key,
                       testData,
                       60,
                       TimeUnit.SECONDS);

        Assertions.assertTrue(ServiceObjectResolve.redisTemplate.hasKey(key),
                              "key不存在");

        System.out.println("已新增数据");

        TestDTO testData4Get = operations.get(key);

        Assertions.assertNotNull(testData4Get,
                                 "未获取到新增的数据");

        AssertExtension.assertEquals(testData,
                                     testData4Get);

        System.out.println("已获取数据");

        Assertions.assertTrue(ServiceObjectResolve.redisTemplate.delete(key),
                              "删除key失败");

        Assertions.assertFalse(ServiceObjectResolve.redisTemplate.hasKey(key),
                               "key未被删除");

        System.out.println("已删除key");
    }
}