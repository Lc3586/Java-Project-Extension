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
public class MySqlProvider<T>
        implements IBaseDbProvider<T> {
    public MySqlProvider(DataSourceConfig config) {
        this.config = config;
    }

    private final DataSourceConfig config;

    @Override
    public ISelect<T> createSelect(Class<T> entityType,
                                   INaiveAdo ado) {
        return new MySqlSelect<>(config,
                                 ado,
                                 entityType);
    }

    @Override
    public IInsert<T> createInsert(Class<T> entityType,
                                   INaiveAdo ado) {
        return new MySqlInsert<>(config,
                                 ado,
                                 entityType);
    }

    @Override
    public IUpdate<T> createUpdate(Class<T> entityType,
                                   INaiveAdo ado) {
        return new MySqlUpdate<>(config,
                                 ado,
                                 entityType);
    }

    @Override
    public IDelete<T> createDelete(Class<T> entityType,
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
