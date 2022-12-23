package project.extension.mybatis.edge.core.ado;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.config.DruidConfig;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.standard.exception.ApplicationException;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源构造器
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Configuration
@EnableConfigurationProperties({BaseConfig.class,
                                DruidConfig.class})
public class NaiveDataSourceProvider
        implements INaiveDataSourceProvider {
    public NaiveDataSourceProvider(BaseConfig baseConfig,
                                   DruidConfig druidConfig,
                                   ApplicationContext applicationContext) {
        this.baseConfig = baseConfig;
        this.druidConfig = druidConfig;
    }

    /**
     * 基础配置
     */
    private final BaseConfig baseConfig;

    /**
     * 连接池配置
     */
    private final DruidConfig druidConfig;

    @Override
    public String defaultDataSource() {
        return this.baseConfig.getDataSource();
    }

    @Override
    public List<String> allDataSources() {
        return this.baseConfig.getAllDataSource();
    }

    @Override
    public Map<String, DataSource> loadAllDataSources()
            throws
            ApplicationException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        for (String dataSource : baseConfig.getAllDataSource()) {
            DataSourceConfig dataSourceConfig = baseConfig.getDataSourceConfig(dataSource);
            DruidDataSource druidDataSource;
            try {
                druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(dataSourceConfig.getProperties());
            } catch (Exception ex) {
                throw new ApplicationException(DbContextStrings.getCreateDataSourceFailed(dataSource),
                                               ex);
            }
            druidConfig.applyConfig(druidDataSource,
                                    dataSourceConfig.getDbType());
            dataSourceMap.put(
                    String.format("MybatisDataSource-%s",
                                  dataSourceConfig.getName()),
                    druidDataSource);
        }

        return dataSourceMap;
    }
}
