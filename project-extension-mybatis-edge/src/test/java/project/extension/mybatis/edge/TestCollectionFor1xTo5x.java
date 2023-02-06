package project.extension.mybatis.edge;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import project.extension.mybatis.edge.application.SpringBootTestApplication;
import project.extension.mybatis.edge.test.*;

/**
 * 1x至5x测试合集
 *
 * @author LCTR
 * @date 2023-01-11
 */
@DisplayName("1x至5x测试合集")
@SpringBootTest(classes = SpringBootTestApplication.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class TestCollectionFor1xTo5x {
    @Nested
    @Order(100)
    @DisplayName("100.数据库连接测试")
    public class ForX100ConnectBasicsTest
            extends X100ConnectBasicsTest {
    }

    @Nested
    @Order(200)
    @DisplayName("200.基础增删改查测试")
    public class ForX200CurdBasicsTest
            extends X200CurdBasicsTest {
    }

    @Nested
    @Order(210)
    @DisplayName("210.Mapper增删改查测试")
    public class ForX210CurdMapperTest
            extends X210CurdMapperTest {
    }

    @Nested
    @Order(300)
    @DisplayName("300.基础事务测试")
    public class ForX300TransactionBasicsTest
            extends X300TransactionBasicsTest {
    }

    @Nested
    @Order(400)
    @DisplayName("400.基础Repository增删改查测试")
    public class ForX400CurdRepositoryBasicsTest
            extends X400CurdRepositoryBasicsTest {
    }

    @Nested
    @Order(500)
    @DisplayName("500.基础Repository事务测试")
    public class ForX500TransactionRepositoryBasicsTest
            extends X500TransactionRepositoryBasicsTest {
    }
}
