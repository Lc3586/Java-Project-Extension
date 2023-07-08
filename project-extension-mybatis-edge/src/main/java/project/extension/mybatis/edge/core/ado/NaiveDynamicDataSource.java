package project.extension.mybatis.edge.core.ado;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 *
 * @author LCTR
 * @date 2022-12-15
 * @see project.extension.mybatis.edge.annotations.NaiveDataSource 获取此注解配置的数据源
 */
public class NaiveDynamicDataSource
        extends AbstractRoutingDataSource {
    public NaiveDynamicDataSource(Map<String, DataSource> dataSourceMap,
                                  String defaultDataSource) {
        super.setDefaultTargetDataSource(dataSourceMap.get(defaultDataSource));
        NaiveDataSourceContextHolder.setDefaultDataSource(defaultDataSource);
        super.setTargetDataSources(new HashMap<>(dataSourceMap));
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return NaiveDataSourceContextHolder.getDataSource();
    }
}
