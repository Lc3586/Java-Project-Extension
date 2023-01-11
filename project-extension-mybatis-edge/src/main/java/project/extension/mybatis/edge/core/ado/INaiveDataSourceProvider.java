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
    String DEFAULT_DATASOURCE = "master";

    /**
     * 数据源事务管理器在IOC容器中的前缀
     */
    String DataSourceTransactionManagerIOCPrefix = "DataSourceTransactionManager_";

    /**
     * Sql会话工厂在IOC容器中的前缀
     */
    String SqlSessionFactoryIOCPrefix = "SqlSessionFactory_";

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
