package project.extension.mybatis.edge;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;

import java.util.List;
import java.util.Map;

/**
 * SpringBoot构建测试
 *
 * @author LCTR
 * @date 2022-12-15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootTestApplication.class)
@DisplayName("基础功能测试")
public class BaseTest {
    @Autowired
    public ConfigurableApplicationContext application;

    /**
     * 测试基础功能
     */
    @Test
    @DisplayName("测试基础功能")
    public void baseFunctionTest()
            throws
            Throwable {
        BaseConfig baseConfig = application.getBean(BaseConfig.class);

        Assertions.assertNotEquals(null,
                                   baseConfig,
                                   "未获取到BaseConfig");

        Map<String, DruidDataSource> druidDataSourceMap = application.getBeansOfType(DruidDataSource.class);

        Assertions.assertNotEquals(0,
                                   druidDataSourceMap.size(),
                                   "未获取到任何DruidDataSource");

        for (String dataSource : baseConfig.getAllDataSource()) {
            Assertions.assertNotEquals(false,
                                       druidDataSourceMap.containsKey(dataSource),
                                       String.format("未获取到name为%s的DruidDataSource",
                                                     dataSource));
        }

        INaiveSql naiveSql = application.getBean(INaiveSql.class);

        Assertions.assertNotEquals(null,
                                   naiveSql,
                                   "未获取到INaiveSql");

        IDbFirst dbFirst = naiveSql.getDbFirst();

        Assertions.assertNotEquals(null,
                                   dbFirst,
                                   "未获取到IDbFirst");

        List<String> databases = dbFirst.getDatabases();
        for (String database : databases) {
            System.out.printf("查询到数据库：%s%n",
                              database);
        }
    }
}