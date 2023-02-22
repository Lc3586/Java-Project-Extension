package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.sqlserver.curd.SqlServerDelete;
import project.extension.mybatis.edge.core.provider.sqlserver.curd.SqlServerInsert;
import project.extension.mybatis.edge.core.provider.sqlserver.curd.SqlServerSelect;
import project.extension.mybatis.edge.core.provider.sqlserver.curd.SqlServerUpdate;
import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.core.provider.standard.curd.IDelete;
import project.extension.mybatis.edge.core.provider.standard.curd.IInsert;
import project.extension.mybatis.edge.core.provider.standard.curd.ISelect;
import project.extension.mybatis.edge.core.provider.standard.curd.IUpdate;

/**
 * SqlServer数据库对象构造器
 *
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerProvider
        implements IBaseDbProvider {
    public SqlServerProvider(DataSourceConfig config) {
        this.config = config;
    }

    private final DataSourceConfig config;

    @Override
    public <TEntity> ISelect<TEntity> createSelect(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new SqlServerSelect<>(config,
                                     ado,
                                     entityType);
    }

    @Override
    public <TEntity> IInsert<TEntity> createInsert(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new SqlServerInsert<>(config,
                                     ado,
                                     entityType);
    }

    @Override
    public <TEntity> IUpdate<TEntity> createUpdate(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new SqlServerUpdate<>(config,
                                     ado,
                                     entityType);
    }

    @Override
    public <TEntity> IDelete<TEntity> createDelete(Class<TEntity> entityType,
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
        return new SqlServerCodeFirst(config,
                                      ado);
    }
}
