package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 数据源构造器接口类
 *
 * @author LCTR
 * @date 2022-12-19
 */
@Repository
public interface INaiveDataSourceProvider {
    /**
     * 默认数据源
     */
    String DEFAULT_DATA_SOURCE = "master";

    /**
     * 数据源的事务管理器在IOC容器中的名称前缀
     */
    String DATA_SOURCE_IOC_PREFIX = "dataSource#";

    /**
     * 数据源的事务管理器在IOC容器中的名称前缀
     */
    String TRANSACTION_MANAGER_IOC_PREFIX = "transactionManager#";

    /**
     * 数据源的Sql会话工厂在IOC容器中的名称前缀
     */
    String SQL_SESSION_FACTORY_IOC_PREFIX = "sqlSessionFactory#";

    /**
     * 数据源的Mapper扫描器注册类在IOC容器中的名称前缀
     */
    String MAPPER_SCANNER_REGISTRAR_IOC_PREFIX = "mapperScannerRegistrar#";

    /**
     * 默认数据源在IOC容器中的名称
     */
    String DEFAULT_DATA_SOURCE_IOC_NAME = "dataSource";

    /**
     * 默认数据源的事务管理器在IOC容器中的名称
     */
    String DEFAULT_TRANSACTION_MANAGER_IOC_NAME = "transactionManager";

    /**
     * 默认数据源的Sql会话工厂在IOC容器中的名称
     */
    String DEFAULT_SQL_SESSION_FACTORY_IOC_NAME = "sqlSessionFactory";

    /**
     * 默认数据源的Mapper扫描器注册类在IOC容器中的名称
     */
    String DEFAULT_MAPPER_SCANNER_REGISTRAR_IOC_NAME = "mapperScannerRegistrar";

    /**
     * 获取默认的数据源
     */
    String defaultDataSource();

    /**
     * 获取所有的数据源
     */
    List<String> allDataSources();

    /**
     * 加载所有的数据源
     */
    Map<String, DataSource> loadAllDataSources();

    /**
     * 获取数据源
     */
    DataSource getDataSources(String dataSource);

    /**
     * 获取事务管理器
     *
     * @param dataSource 数据源
     */
    DataSourceTransactionManager getTransactionManager(String dataSource);

    /**
     * 获取Sql会话工厂
     *
     * @param dataSource 数据源
     */
    SqlSessionFactory getSqlSessionFactory(String dataSource);
}
