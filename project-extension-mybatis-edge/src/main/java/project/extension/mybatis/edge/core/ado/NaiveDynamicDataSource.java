package project.extension.mybatis.edge.core.ado;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Primary
@Component
public class NaiveDynamicDataSource
        extends AbstractRoutingDataSource {
    public NaiveDynamicDataSource(INaiveDataSourceProvider naiveDataSourceProvider) {
        Map<String, DataSource> dataSourceMap = naiveDataSourceProvider.loadAllDataSources();
        super.setDefaultTargetDataSource(dataSourceMap.get(naiveDataSourceProvider.defaultDataSource()));
        super.setTargetDataSources(new HashMap<>(dataSourceMap));
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return NaiveDataSourceContextHolder.getDataSource();
    }
}
