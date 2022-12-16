package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.standard.*;

/**
 * SqlServer数据库对象构造器
 *
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerProvider<T>
        implements IBaseDbProvider<T> {
    public SqlServerProvider(DataSourceConfig config,
                             IAop aop) {
        this.config = config;
        this.aop = aop;
    }

    private final DataSourceConfig config;

    private final IAop aop;

    @Override
    public ISelect<T> createSelect(Class<T> entityType,
                                   boolean withTransactional) {
        return new SqlServerSelect<>(config,
                                     aop,
                                     entityType,
                                     withTransactional);
    }

    @Override
    public IInsert<T> createInsert(Class<T> entityType,
                                   boolean withTransactional) {
        return new SqlServerInsert<>(config,
                                     aop,
                                     entityType,
                                     withTransactional);
    }

    @Override
    public IUpdate<T> createUpdate(Class<T> entityType,
                                   boolean withTransactional) {
        return new SqlServerUpdate<>(config,
                                     aop,
                                     entityType,
                                     withTransactional);
    }

    @Override
    public IDelete<T> createDelete(Class<T> entityType,
                                   boolean withTransactional) {
        return new SqlServerDelete<>(config,
                                     aop,
                                     entityType,
                                     withTransactional);
    }

    @Override
    public IDbFirst createDbFirst() {
        return new SqlServerDbFirst(config);
    }

    @Override
    public ICodeFirst createCodeFirst() {
        return new SqlServerCodeFirst();
    }
}
