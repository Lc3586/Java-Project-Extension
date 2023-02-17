package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.config.TestDataSourceConfig;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;

import java.util.List;

/**
 * 100.数据库连接测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X100BasicsOfConnectTest {
    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        OrmObjectResolve.injection();
    }

    /**
     * 测试数据库连接是否正常
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.mybatis.edge.config.TestDataSourceConfig#getMultiTestDataSourceName")
    @DisplayName("100.测试数据库连接是否正常")
    @Order(100)
    public void _100(String name) {
        String dataSource = TestDataSourceConfig.getTestDataSource(name);

        IDbFirst dbFirst = OrmObjectResolve.getDbFirst(dataSource);

        List<String> databases = dbFirst.getDatabases();
        for (String database : databases) {
            System.out.printf("\r\n查询到数据库：%s%n\r\n",
                              database);
        }
    }
}