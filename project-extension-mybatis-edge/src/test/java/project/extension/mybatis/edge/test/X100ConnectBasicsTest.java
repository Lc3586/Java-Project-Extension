package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.mybatis.edge.common.OrmObjectResolve;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 100.数据库连接测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X100ConnectBasicsTest {
    /**
     * 全部数据源
     */
    private static final Map<String, String> dataSourceMap = new HashMap<>();

    /**
     * 多数据源测试
     *
     * @return 数据源
     */
    private static String[] multiDataSourceTest() {
        dataSourceMap.put("MySQL 8.0",
                          "mysql");
        dataSourceMap.put("MariaDB 10.10",
                          "mariadb");
        dataSourceMap.put("SqlServer 2012 降级处理",
                          "sqlserver");
        dataSourceMap.put("SqlServer 2012",
                          "sqlserver2012");
        dataSourceMap.put("达梦 8",
                          "dameng");
        dataSourceMap.put("Oracle 19c",
                          "oracle");
        dataSourceMap.put("PostgreSQL 15",
                          "postgresql");
        return dataSourceMap.keySet()
                            .toArray(new String[0]);
    }

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
    @MethodSource("multiDataSourceTest")
    @DisplayName("100.测试数据库连接是否正常")
    @Order(100)
    public void _100(String name) {
        String dataSource = dataSourceMap.get(name);

        IDbFirst dbFirst = OrmObjectResolve.getDbFirst(dataSource);

        List<String> databases = dbFirst.getDatabases();
        for (String database : databases) {
            System.out.printf("\r\n查询到数据库：%s%n\r\n",
                              database);
        }
    }
}