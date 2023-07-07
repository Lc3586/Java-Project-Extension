package project.extension.mybatis.edge.core.ado;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.config.MyBatisEdgeBaseConfig;
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
@EnableConfigurationProperties({MyBatisEdgeBaseConfig.class,
                                DruidConfig.class})
@DependsOn("iocExtension")
public class NaiveDataSourceProvider
        implements INaiveDataSourceProvider {
    public NaiveDataSourceProvider(MyBatisEdgeBaseConfig myBatisEdgeBaseConfig,
                                   DruidConfig druidConfig) {
        this.myBatisEdgeBaseConfig = myBatisEdgeBaseConfig;
        this.druidConfig = druidConfig;
        this.databaseIdProvider = IOCExtension.tryGetBean(DatabaseIdProvider.class);
    }

    /**
     * 基础配置
     */
    private final MyBatisEdgeBaseConfig myBatisEdgeBaseConfig;

    /**
     * 连接池配置
     */
    private final DruidConfig druidConfig;

    private final DatabaseIdProvider databaseIdProvider;

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
        DataSourceConfig dataSourceConfig = myBatisEdgeBaseConfig.getDataSourceConfig(dataSource);
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
            registerMapperScannerRegistrar(dataSourceConfig,
                                           getSqlSessionFactoryBeanName(null));
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
            if (this.databaseIdProvider != null)
                sqlSessionFactoryBean.setDatabaseIdProvider(this.databaseIdProvider);

            try {
                sqlSessionFactoryBean.setConfigLocation(ScanExtension.getResource(CommonUtils.getConfig()
                                                                                             .getConfigLocation()));
            } catch (Exception ex) {
                throw new ModuleException(Strings.getConfigDataSourceOptionInvalid(dataSourceConfig.getName(),
                                                                                   "configLocation",
                                                                                   dataSourceConfig.getConfigLocation()),
                                          ex);
            }

            try {
                if (CollectionsExtension.anyPlus(dataSourceConfig.getScanEntitiesPackages()))
//                    sqlSessionFactoryBean.setTypeAliases(ScanExtension.scanClassFromPackage(dataSourceConfig.getScanEntitiesPackages())
//                                                                      .stream()
//                                                                      .distinct()
//                                                                      .toArray(Class<?>[]::new));
                    sqlSessionFactoryBean.setTypeAliasesPackage(StringUtils.collectionToDelimitedString(dataSourceConfig.getScanEntitiesPackages(),
                                                                                                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
            } catch (Exception ex) {
                throw new ModuleException(Strings.getConfigDataSourceOptionInvalid(dataSourceConfig.getName(),
                                                                                   "scanEntitiesPackages",
                                                                                   dataSourceConfig.getScanEntitiesPackages()
                                                                                           == null
                                                                                   ? ""
                                                                                   : String.join(" ",
                                                                                                 dataSourceConfig.getScanEntitiesPackages())),
                                          ex);
            }

            try {
                if (CollectionsExtension.anyPlus(dataSourceConfig.getScanMapperXmlLocations()))
                    sqlSessionFactoryBean.setMapperLocations(ScanExtension.scanResourceFromLocation(dataSourceConfig.getScanMapperXmlLocations())
                                                                          .toArray(new Resource[0]));
            } catch (Exception ex) {
                throw new ModuleException(Strings.getConfigDataSourceOptionInvalid(dataSourceConfig.getName(),
                                                                                   "scanMapperXmlLocations",
                                                                                   dataSourceConfig.getScanMapperXmlLocations()
                                                                                           == null
                                                                                   ? ""
                                                                                   : String.join(" ",
                                                                                                 dataSourceConfig.getScanMapperXmlLocations())),
                                          ex);
            }

            //分页插件
            if (myBatisEdgeBaseConfig.isEnablePageHelper()) {
                PageInterceptor pageInterceptor = new PageInterceptor();
                if (myBatisEdgeBaseConfig.pageHelperProperties != null)
                    pageInterceptor.setProperties(myBatisEdgeBaseConfig.getPageHelperProperties());
                sqlSessionFactoryBean.setPlugins(pageInterceptor);
            }

            sqlSessionFactory = sqlSessionFactoryBean.getObject();
            if (sqlSessionFactory == null)
                throw new Exception("method SqlSessionFactoryBean.getObject() result null");
        } catch (ModuleException ex) {
            throw ex;
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

//        getBeanFactory().registerSingleton(
//                getSqlSessionFactoryBeanName(dataSourceConfig.getName()),
//                sqlSessionFactory);
    }

    /**
     * 注册Mapper扫描器注册类
     *
     * @param dataSourceConfig 数据源配置
     */
    private void registerMapperScannerRegistrar(DataSourceConfig dataSourceConfig,
                                                String sqlSessionFactoryBeanName) {
        if (!CollectionsExtension.anyPlus(dataSourceConfig.getScanMapperPackages()))
            return;

        NaiveMapperScanner scanner = new NaiveMapperScanner(sqlSessionFactoryBeanName,
                                                            getBeanFactory());
        scanner.doScan(dataSourceConfig.getScanMapperPackages()
                                       .toArray(new String[0]));
    }

    /**
     * 检查数据源是否可用
     *
     * @param dataSource 数据源名称
     */
    private void checkDataSourceBeforeGet(String dataSource) {
        if (!isExists(dataSource))
            throw new ModuleException(Strings.getConfigDataSourceUndefined(dataSource));

        if (!isEnable(dataSource))
            throw new ModuleException(Strings.getConfigDataSourceNotActive(dataSource));
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
        return this.myBatisEdgeBaseConfig.getDataSource();
    }

    @Override
    public boolean isExists(String dataSource) {
        return this.myBatisEdgeBaseConfig.getMultiDataSource()
                                         .containsKey(dataSource);
    }

    @Override
    public boolean isEnable(String dataSource) {
        return this.myBatisEdgeBaseConfig.getMultiDataSource()
                                         .get(dataSource)
                                         .isEnable();
    }

    @Override
    public List<String> allDataSources(boolean enabledOnly) {
        return this.myBatisEdgeBaseConfig.getAllDataSource(enabledOnly);
    }

    @Override
    public Map<String, DataSource> loadAllDataSources()
            throws
            ModuleException {
        if (dataSourceMap.size() == 0) {
            for (String dataSource : allDataSources(true)) {
                loadAndRegisterDataSource(dataSource);
            }
        }

        return dataSourceMap;
    }

    @Override
    public DataSource getDataSources(String dataSource) {
        checkDataSourceBeforeGet(dataSource);
        return loadAllDataSources().get(dataSource);
    }

    @Override
    public DataSourceTransactionManager getTransactionManager(String dataSource) {
        checkDataSourceBeforeGet(dataSource);
        return transactionManagerMap.get(dataSource);
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory(String dataSource) {
        checkDataSourceBeforeGet(dataSource);
        return sqlSessionFactoryMap.get(dataSource);
    }
}
