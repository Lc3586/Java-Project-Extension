package project.extension.mybatis.edge.core.provider.postgresql;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.postgresql.curd.PostgreSQLDelete;
import project.extension.mybatis.edge.core.provider.postgresql.curd.PostgreSQLInsert;
import project.extension.mybatis.edge.core.provider.postgresql.curd.PostgreSQLSelect;
import project.extension.mybatis.edge.core.provider.postgresql.curd.PostgreSQLUpdate;
import project.extension.mybatis.edge.core.provider.standard.IBaseDbProvider;
import project.extension.mybatis.edge.core.provider.standard.ICodeFirst;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.core.provider.standard.curd.IDelete;
import project.extension.mybatis.edge.core.provider.standard.curd.IInsert;
import project.extension.mybatis.edge.core.provider.standard.curd.ISelect;
import project.extension.mybatis.edge.core.provider.standard.curd.IUpdate;

/**
 * PostgreSQL数据库对象构造器
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class PostgreSQLProvider
        implements IBaseDbProvider {
    public PostgreSQLProvider(DataSourceConfig config) {
        this.config = config;
    }

    private final DataSourceConfig config;

    @Override
    public <TEntity> ISelect<TEntity> createSelect(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new PostgreSQLSelect<>(config,
                                      ado,
                                      entityType);
    }

    @Override
    public <TEntity> IInsert<TEntity> createInsert(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new PostgreSQLInsert<>(config,
                                      ado,
                                      entityType);
    }

    @Override
    public <TEntity> IUpdate<TEntity> createUpdate(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new PostgreSQLUpdate<>(config,
                                      ado,
                                      entityType);
    }

    @Override
    public <TEntity> IDelete<TEntity> createDelete(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new PostgreSQLDelete<>(config,
                                      ado,
                                      entityType);
    }

    @Override
    public IDbFirst createDbFirst(INaiveAdo ado) {
        return new PostgreSQLDbFirst(config,
                                     ado);
    }

    @Override
    public ICodeFirst createCodeFirst(INaiveAdo ado) {
        return new PostgreSQLCodeFirst(config,
                                       ado);
    }
}
