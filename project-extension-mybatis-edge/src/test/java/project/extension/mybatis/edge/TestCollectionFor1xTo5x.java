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
    public class ForX100BasicsOfConnectTest
            extends X100BasicsOfConnectTest {
    }

    @Nested
    @Order(110)
    @DisplayName("110.基础动态数据源测试")
    public class ForX110BasicsOfDynamicDataSourceTest
            extends X110BasicsOfDynamicDataSourceTest {
    }

    @Nested
    @Order(200)
    @DisplayName("200.基础增删改查测试")
    public class ForX200BasicsOfCurdTest
            extends X200BasicsOfCurdTest {
    }

    @Nested
    @Order(210)
    @DisplayName("210.Mapper增删改查测试")
    public class ForX210MapperOfCurdTest
            extends X210MapperOfCurdTest {
    }

    @Nested
    @Order(220)
    @DisplayName("220.批量增删改查测试")
    public class ForX220BatchCurdTest
            extends X220BatchCurdTest {
    }

    @Nested
    @Order(300)
    @DisplayName("300.基础事务测试")
    public class ForX300BasicsOfTransactionTest
            extends X300BasicsOfTransactionTest {
    }

    @Nested
    @Order(400)
    @DisplayName("400.基础Repository增删改查测试")
    public class ForX400BasicsOfRepositoryCurdTest
            extends X400BasicsOfRepositoryCurdTest {
    }

    @Nested
    @Order(500)
    @DisplayName("500.基础Repository事务测试")
    public class ForX500BasicsOfRepositoryTransactionTest
            extends X500BasicsOfRepositoryTransactionTest {
    }
}
