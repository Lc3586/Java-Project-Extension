package project.extension.mybatis.edge;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import project.extension.mybatis.edge.application.SpringBootTestApplication;
import project.extension.mybatis.edge.test.X1ConnectBasicsTest;
import project.extension.mybatis.edge.test.X2CurdBasicsTest;
import project.extension.mybatis.edge.test.X3TransactionBasicsTest;

/**
 * 1x至3x测试合集
 *
 * @author LCTR
 * @date 2023-01-11
 */
@DisplayName("1x至3x测试合集")
@SpringBootTest(classes = SpringBootTestApplication.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class TestCollectionFor1xTo3x {
    @Nested
    @Order(1)
    @DisplayName("1x.数据库连接测试")
    public class ForX1ConnectBasicsTest
            extends X1ConnectBasicsTest {
    }

    @Nested
    @Order(2)
    @DisplayName("2x.基础增删改查测试")
    public class ForX2CurdBasicsTest
            extends X2CurdBasicsTest {
    }

    @Nested
    @Order(3)
    @DisplayName("3x.基础事务测试")
    public class ForX3TransactionBasicsTest
            extends X3TransactionBasicsTest {
    }
}
