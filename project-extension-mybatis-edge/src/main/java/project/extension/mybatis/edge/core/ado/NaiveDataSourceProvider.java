package project.extension.mybatis.edge.core.ado;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.config.DruidConfig;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.standard.exception.ModuleException;

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
@DependsOn("IOCExtension")
@EnableTransactionManagement
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

    private final Map<String, DataSource> dataSourceMap = new HashMap<>();

    private final Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();

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
            ModuleException {
        if (dataSourceMap.size() == 0) {
            for (String dataSource : baseConfig.getAllDataSource()) {
                DataSourceConfig dataSourceConfig = baseConfig.getDataSourceConfig(dataSource);
                DruidDataSource druidDataSource;
                try {
                    druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(dataSourceConfig.getProperties());
                } catch (Exception ex) {
                    throw new ModuleException(DbContextStrings.getCreateDataSourceFailed(dataSource),
                                              ex);
                }
                druidConfig.applyConfig(druidDataSource,
                                        dataSourceConfig.getDbType());
                dataSourceMap.put(
                        dataSourceConfig.getName(),
                        druidDataSource);

                final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
                sqlSessionFactoryBean.setDataSource(druidDataSource);
                sqlSessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(CommonUtils.getConfig()
                                                                                                           .getConfigLocation()));
                SqlSessionFactory sqlSessionFactory;
                try {
                    sqlSessionFactory = sqlSessionFactoryBean.getObject();
                } catch (Exception ex) {
                    throw new ModuleException(DbContextStrings.getSqlSessionFactoryFailed());
                }
                sqlSessionFactoryMap.put(dataSourceConfig.getName(),
                                         sqlSessionFactory);
            }
        }

        return dataSourceMap;
    }

    @Override
    public DataSource getDataSources(String dataSource) {
        return loadAllDataSources().get(dataSource);
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory(String dataSource) {
        return sqlSessionFactoryMap.get(dataSource);
    }
}
