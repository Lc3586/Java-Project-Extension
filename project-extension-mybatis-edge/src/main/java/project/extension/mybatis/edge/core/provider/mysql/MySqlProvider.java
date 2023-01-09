package project.extension.mybatis.edge.core.provider.mysql;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.*;

/**
 * MySql数据库对象构造器
 *
 * @author LCTR
 * @date 2022-03-30
 */
public class MySqlProvider
        implements IBaseDbProvider {
    public MySqlProvider(DataSourceConfig config) {
        this.config = config;
    }

    private final DataSourceConfig config;

    @Override
    public <TEntity> ISelect<TEntity> createSelect(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new MySqlSelect<>(config,
                                 ado,
                                 entityType);
    }

    @Override
    public <TEntity> IInsert<TEntity> createInsert(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new MySqlInsert<>(config,
                                 ado,
                                 entityType);
    }

    @Override
    public <TEntity> IUpdate<TEntity> createUpdate(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new MySqlUpdate<>(config,
                                 ado,
                                 entityType);
    }

    @Override
    public <TEntity> IDelete<TEntity> createDelete(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new MySqlDelete<>(config,
                                 ado,
                                 entityType);
    }

    @Override
    public IDbFirst createDbFirst(INaiveAdo ado) {
        return new MySqlDbFirst(config,
                                ado);
    }

    @Override
    public ICodeFirst createCodeFirst(INaiveAdo ado) {
        return new MySqlCodeFirst(ado);
    }
}
