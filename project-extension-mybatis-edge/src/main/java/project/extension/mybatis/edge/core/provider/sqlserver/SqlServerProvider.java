package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.*;

/**
 * SqlServer数据库对象构造器
 *
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerProvider<T>
        implements IBaseDbProvider<T> {
    public SqlServerProvider(DataSourceConfig config) {
        this.config = config;
    }

    private final DataSourceConfig config;

    @Override
    public ISelect<T> createSelect(Class<T> entityType,
                                   INaiveAdo ado) {
        return new SqlServerSelect<>(config,
                                     ado,
                                     entityType);
    }

    @Override
    public IInsert<T> createInsert(Class<T> entityType,
                                   INaiveAdo ado) {
        return new SqlServerInsert<>(config,
                                     ado,
                                     entityType);
    }

    @Override
    public IUpdate<T> createUpdate(Class<T> entityType,
                                   INaiveAdo ado) {
        return new SqlServerUpdate<>(config,
                                     ado,
                                     entityType);
    }

    @Override
    public IDelete<T> createDelete(Class<T> entityType,
                                   INaiveAdo ado) {
        return new SqlServerDelete<>(config,
                                     ado,
                                     entityType);
    }

    @Override
    public IDbFirst createDbFirst(INaiveAdo ado) {
        return new SqlServerDbFirst(config,
                                    ado);
    }

    @Override
    public ICodeFirst createCodeFirst(INaiveAdo ado) {
        return new SqlServerCodeFirst(ado);
    }
}
