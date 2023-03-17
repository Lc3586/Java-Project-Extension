package project.extension.wechat;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import project.extension.wechat.application.SpringBootTestApplication;
import project.extension.wechat.test.X100OAuth2Test;

/**
 * 1x至5x测试合集
 *
 * @author LCTR
 * @date 2023-03-17
 */
@DisplayName("1x至5x测试合集")
@SpringBootTest(classes = SpringBootTestApplication.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class TestCollectionFor1xTo5x {
    @Nested
    @Order(100)
    @DisplayName("100.数据库连接测试")
    public class ForX100OAuth2Test
            extends X100OAuth2Test {
    }
}
