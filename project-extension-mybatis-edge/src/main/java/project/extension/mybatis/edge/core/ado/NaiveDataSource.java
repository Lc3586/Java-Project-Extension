package project.extension.mybatis.edge.core.ado;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

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
public class NaiveDataSource
        extends AbstractRoutingDataSource {
    public NaiveDataSource(INaiveDataSourceProvider naiveDataSourceProvider) {
        Map<String, DataSource> dataSourceMap = naiveDataSourceProvider.loadAllDataSources();
        super.setTargetDataSources(new HashMap<>(dataSourceMap));
        this.dataSource = naiveDataSourceProvider.defaultDataSource();
        super.setDefaultTargetDataSource(dataSourceMap.get(this.dataSource));
        super.afterPropertiesSet();
    }

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return this.dataSource;
    }
}
