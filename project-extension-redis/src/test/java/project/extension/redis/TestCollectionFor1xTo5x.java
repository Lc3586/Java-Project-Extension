package project.extension.redis;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import project.extension.redis.application.SpringBootTestApplication;
import project.extension.redis.test.X100ConnectTest;

/**
 * 1x至5x测试合集
 *
 * @author LCTR
 * @date 2023-03-29
 */
@DisplayName("1x至5x测试合集")
@SpringBootTest(classes = SpringBootTestApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class TestCollectionFor1xTo5x {
    @Nested
    @Order(100)
    @DisplayName("100.连接测试")
    public class ForX100ConnectTest
            extends X100ConnectTest {
    }
}
