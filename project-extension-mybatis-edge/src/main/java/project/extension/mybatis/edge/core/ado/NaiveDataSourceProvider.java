package project.extension.mybatis.edge.core.ado;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.config.DruidConfig;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.mybatis.edge.globalization.Strings;
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
public class NaiveDataSourceProvider
        implements INaiveDataSourceProvider {
    public NaiveDataSourceProvider(BaseConfig baseConfig,
                                   DruidConfig druidConfig) {
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

    private final Map<String, DataSourceTransactionManager> transactionManagerMap = new HashMap<>();

    private DefaultListableBeanFactory beanFactory;

    /**
     * 容器工厂
     */
    private DefaultListableBeanFactory getBeanFactory() {
        if (beanFactory == null)
            beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) IOCExtension.applicationContext).getBeanFactory();
        return beanFactory;
    }

    /**
     * 加载数据源、事务管理器、Sql会话工厂
     *
     * @param dataSource 数据源名称
     */
    private void loadDataSource(String dataSource) {
        DataSourceConfig dataSourceConfig = baseConfig.getDataSourceConfig(dataSource);
        if (!dataSourceConfig.isEnable())
            return;

        DruidDataSource druidDataSource;
        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(dataSourceConfig.getProperties());
        } catch (Exception ex) {
            throw new ModuleException(Strings.getCreateDataSourceFailed(dataSourceConfig.getName()),
                    ex);
        }
        druidConfig.applyConfig(druidDataSource,
                dataSourceConfig.getDbType());
        dataSourceMap.put(
                dataSourceConfig.getName(),
                druidDataSource);

        if (dataSourceConfig.getName()
                .equals(defaultDataSource()))
            getBeanFactory().registerSingleton(
                    INaiveDataSourceProvider.DEFAULT_DATA_SOURCE_IOC_NAME,
                    druidDataSource);

        getBeanFactory().registerSingleton(
                String.format("%s%s",
                        INaiveDataSourceProvider.DATA_SOURCE_IOC_PREFIX,
                        dataSourceConfig.getName()),
                druidDataSource);

        //事务管理器
        loadTransactionManager(dataSource,
                druidDataSource);

        //Sql会话工厂
        loadSqlSessionFactory(dataSource,
                druidDataSource);
    }

    /**
     * 加载事务管理器
     *
     * @param name       数据源名称
     * @param dataSource 数据源
     */
    private void loadTransactionManager(String name,
                                        DataSource dataSource) {
        final DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        transactionManagerMap.put(name,
                dataSourceTransactionManager);

        if (name.equals(defaultDataSource()))
            getBeanFactory().registerSingleton(
                    INaiveDataSourceProvider.DEFAULT_TRANSACTION_MANAGER_IOC_NAME,
                    dataSourceTransactionManager);

        getBeanFactory().registerSingleton(
                String.format("%s%s",
                        INaiveDataSourceProvider.TRANSACTION_MANAGER_IOC_PREFIX,
                        name),
                dataSourceTransactionManager);
    }

    /**
     * 加载Sql会话工厂
     *
     * @param name       数据源名称
     * @param dataSource 数据源
     */
    private void loadSqlSessionFactory(String name,
                                       DataSource dataSource) {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(CommonUtils.getConfig()
                .getConfigLocation()));
        SqlSessionFactory sqlSessionFactory;
        try {
            sqlSessionFactory = sqlSessionFactoryBean.getObject();
            if (sqlSessionFactory == null)
                throw new Exception("method SqlSessionFactoryBean.getObject() result null");
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSqlSessionFactoryFailed());
        }
        sqlSessionFactoryMap.put(name,
                sqlSessionFactory);

        if (name.equals(defaultDataSource()))
            getBeanFactory().registerSingleton(
                    INaiveDataSourceProvider.DEFAULT_SQL_SESSION_FACTORY_IOC_NAME,
                    sqlSessionFactory);

        getBeanFactory().registerSingleton(
                String.format("%s%s",
                        INaiveDataSourceProvider.SQL_SESSION_FACTORY_IOC_PREFIX,
                        name),
                sqlSessionFactory);
    }

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
                loadDataSource(dataSource);
            }
        }

        return dataSourceMap;
    }

    @Override
    public DataSource getDataSources(String dataSource) {
        return loadAllDataSources().get(dataSource);
    }

    @Override
    public DataSourceTransactionManager getTransactionManager(String dataSource) {
        return transactionManagerMap.get(dataSource);
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory(String dataSource) {
        return sqlSessionFactoryMap.get(dataSource);
    }
}
