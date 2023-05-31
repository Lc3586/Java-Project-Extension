package project.extension.mybatis.edge;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import project.extension.mybatis.edge.application.SpringBootTestApplication;
import project.extension.mybatis.edge.test.*;

/**
 * 6x测试合集
 *
 * @author LCTR
 * @date 2023-05-31
 */
@DisplayName("6x测试合集")
@SpringBootTest(classes = SpringBootTestApplication.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class TestCollectionFor6x {
    @Nested
    @Order(600)
    @DisplayName("600.内存溢出测试")
    public class ForXX600OutOfMemoryTest
            extends X600OutOfMemoryTest {
    }
}
