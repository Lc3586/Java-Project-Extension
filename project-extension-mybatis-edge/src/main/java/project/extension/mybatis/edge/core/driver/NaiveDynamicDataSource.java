package project.extension.mybatis.edge.core.driver;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.config.DruidConfig;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 初级动态数据源
 *
 * @author LCTR
 * @date 2022-12-15
 */
public class NaiveDynamicDataSource
        extends AbstractRoutingDataSource {
    public NaiveDynamicDataSource(DataSource defaultTargetDataSource,
                                  Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return NaiveDynamicDataSourceContext.getDataSourceType();
    }
}
