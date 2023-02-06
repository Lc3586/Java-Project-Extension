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
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import project.extension.collections.CollectionsExtension;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.config.DruidConfig;
import project.extension.mybatis.edge.core.mapper.NaiveMapperScanner;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.resource.ScanExtension;
import project.extension.standard.exception.ModuleException;

import javax.annotation.Nullable;
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
@SuppressWarnings("SpringDependsOnUnresolvedBeanInspection")
@Configuration
@EnableConfigurationProperties({BaseConfig.class,
                                DruidConfig.class})
@DependsOn("iocExtension")
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
     * 加载并注册数据源、事务管理器、Sql会话工厂、Mapper扫描器注册类
     *
     * @param dataSource 数据源名称
     */
    private void loadAndRegisterDataSource(String dataSource) {
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
                    getDataSourceBeanName(null),
                    druidDataSource);

        getBeanFactory().registerSingleton(
                getDataSourceBeanName(dataSourceConfig.getName()),
                druidDataSource);

        //事务管理器
        loadAndRegisterTransactionManager(dataSourceConfig,
                                          druidDataSource);

        //Sql会话工厂
        loadAndRegisterSqlSessionFactory(dataSourceConfig,
                                         druidDataSource);

        //Mapper扫描器注册类
        if (dataSourceConfig.getName()
                            .equals(defaultDataSource()))
            registerMapperScannerRegistrar(dataSourceConfig);
        //TODO 注入多数据源的Mapper扫描器注册类

    }

    /**
     * 加载并注册事务管理器
     *
     * @param dataSourceConfig 数据源配置
     * @param dataSource       数据源
     */
    private void loadAndRegisterTransactionManager(DataSourceConfig dataSourceConfig,
                                                   DataSource dataSource) {
        final DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        transactionManagerMap.put(dataSourceConfig.getName(),
                                  dataSourceTransactionManager);

        if (dataSourceConfig.getName()
                            .equals(defaultDataSource()))
            getBeanFactory().registerSingleton(
                    getTransactionManagerBeanName(null),
                    dataSourceTransactionManager);

        getBeanFactory().registerSingleton(
                getTransactionManagerBeanName(dataSourceConfig.getName()),
                dataSourceTransactionManager);
    }

    /**
     * 加载并注册Sql会话工厂
     *
     * @param dataSourceConfig 数据源配置
     * @param dataSource       数据源
     */
    private void loadAndRegisterSqlSessionFactory(DataSourceConfig dataSourceConfig,
                                                  DataSource dataSource) {
        SqlSessionFactory sqlSessionFactory;
        try {
            //VFS.addImplClass(SpringBootVFS.class);
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setConfigLocation(ScanExtension.getResource(CommonUtils.getConfig()
                                                                                         .getConfigLocation()));
            sqlSessionFactoryBean.setTypeAliases(ScanExtension.scanClassFromPackage(dataSourceConfig.getScanEntitiesPackages())
                                                              .toArray(new Class[0]));
            sqlSessionFactoryBean.setMapperLocations(ScanExtension.scanResourceFromLocation(dataSourceConfig.getScanMapperXmlLocations())
                                                                  .toArray(new Resource[0]));
            sqlSessionFactory = sqlSessionFactoryBean.getObject();
            if (sqlSessionFactory == null)
                throw new Exception("method SqlSessionFactoryBean.getObject() result null");
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSqlSessionFactoryFailed(),
                                      ex);
        }

        sqlSessionFactoryMap.put(dataSourceConfig.getName(),
                                 sqlSessionFactory);

        if (dataSourceConfig.getName()
                            .equals(defaultDataSource()))
            getBeanFactory().registerSingleton(
                    getSqlSessionFactoryBeanName(null),
                    sqlSessionFactory);

        getBeanFactory().registerSingleton(
                getSqlSessionFactoryBeanName(dataSourceConfig.getName()),
                sqlSessionFactory);
    }

    /**
     * 注册Mapper扫描器注册类
     *
     * @param dataSourceConfig 数据源配置
     */
    private void registerMapperScannerRegistrar(DataSourceConfig dataSourceConfig) {
        if (!CollectionsExtension.anyPlus(dataSourceConfig.getScanMapperPackages()))
            return;

        NaiveMapperScanner scanner = new NaiveMapperScanner(getSqlSessionFactoryBeanName(dataSourceConfig.getName()),
                                                            getBeanFactory());
        scanner.doScan(dataSourceConfig.getScanMapperPackages()
                                       .toArray(new String[0]));
    }

    /**
     * 获取数据源在Spring IOC容器中的名称
     *
     * @param dataSource 数据源
     */
    public static String getDataSourceBeanName(
            @Nullable
                    String dataSource) {
        return dataSource == null
               ? INaiveDataSourceProvider.DEFAULT_DATA_SOURCE_IOC_NAME
               : String.format("%s%s",
                               INaiveDataSourceProvider.DATA_SOURCE_IOC_PREFIX,
                               dataSource);
    }

    /**
     * 获取数据源的事务管理器在Spring IOC容器中的名称
     *
     * @param dataSource 数据源
     */
    public static String getTransactionManagerBeanName(
            @Nullable
                    String dataSource) {
        return dataSource == null
               ? INaiveDataSourceProvider.DEFAULT_TRANSACTION_MANAGER_IOC_NAME
               : String.format("%s%s",
                               INaiveDataSourceProvider.TRANSACTION_MANAGER_IOC_PREFIX,
                               dataSource);
    }

    /**
     * 获取数据源的Sql会话工厂在Spring IOC容器中的名称
     *
     * @param dataSource 数据源
     */
    public static String getSqlSessionFactoryBeanName(
            @Nullable
                    String dataSource) {
        return dataSource == null
               ? INaiveDataSourceProvider.DEFAULT_SQL_SESSION_FACTORY_IOC_NAME
               : String.format("%s%s",
                               INaiveDataSourceProvider.SQL_SESSION_FACTORY_IOC_PREFIX,
                               dataSource);
    }

    /**
     * 获取数据源的Mapper扫描器的注册类在Spring IOC容器中的名称
     *
     * @param dataSource 数据源
     */
    public static String getMapperScannerRegistrarBeanName(
            @Nullable
                    String dataSource) {
        return dataSource == null
               ? INaiveDataSourceProvider.DEFAULT_MAPPER_SCANNER_REGISTRAR_IOC_NAME
               : String.format("%s%s",
                               INaiveDataSourceProvider.MAPPER_SCANNER_REGISTRAR_IOC_PREFIX,
                               dataSource);
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
                loadAndRegisterDataSource(dataSource);
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
