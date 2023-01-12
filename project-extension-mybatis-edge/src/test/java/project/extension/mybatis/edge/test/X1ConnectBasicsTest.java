package project.extension.mybatis.edge.test;

import org.junit.jupiter.api.*;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.common.OrmInjection;
import project.extension.mybatis.edge.core.ado.NaiveDataSource;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 1x.数据库连接测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X1ConnectBasicsTest {
    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        OrmInjection.injection();
    }

    /**
     * 测试数据库连接是否正常
     */
    @Test
    @DisplayName("100.测试数据库连接是否正常")
    @Order(100)
    public void _100() {
        NaiveDataSource naiveDataSource = IOCExtension.applicationContext.getBean(NaiveDataSource.class);

        Assertions.assertNotEquals(null,
                naiveDataSource,
                "未获取到NaiveDataSource");

        System.out.printf("\r\n%s已注入\r\n",
                NaiveDataSource.class.getName());

        Map<Object, DataSource> druidDataSourceMap = naiveDataSource.getResolvedDataSources();

        Assertions.assertNotEquals(0,
                druidDataSourceMap.size(),
                "未获取到任何DruidDataSource");

        for (String dataSource : OrmInjection.baseConfig.getAllDataSource()) {
            if (!OrmInjection.baseConfig.getDataSourceConfig(dataSource).isEnable()) {
                System.out.printf("\r\n%s数据源未启用\r\n",
                        dataSource);
                continue;
            }

            Assertions.assertNotEquals(false,
                    druidDataSourceMap.containsKey(dataSource),
                    String.format("未获取到name为%s的DruidDataSource",
                            dataSource));

            System.out.printf("\r\n已创建%s数据源\r\n",
                    dataSource);

            INaiveSql orm;
            String ormName;
            if (dataSource.equals(OrmInjection.baseConfig.getDataSource())) {
                orm = OrmInjection.masterNaiveSql;
                ormName = String.format("主库-%s",
                        dataSource);
            } else {
                orm = OrmInjection.multiNaiveSql.getSlaveOrm(dataSource);
                ormName = String.format("从库-%s",
                        dataSource);
            }

            Assertions.assertNotNull(orm,
                    String.format("未获取到%s的orm",
                            ormName));

            IDbFirst dbFirst = orm.getDbFirst();

            Assertions.assertNotEquals(null,
                    dbFirst,
                    String.format("未获取到%s的IDbFirst",
                            ormName));

            System.out.printf("\r\n成功获取%s的%s\r\n",
                    ormName,
                    IDbFirst.class.getName());

            List<String> databases = dbFirst.getDatabases();
            for (String database : databases) {
                System.out.printf("\r\n%s查询到数据库：%s%n\r\n",
                        ormName,
                        database);
            }
        }
    }
}