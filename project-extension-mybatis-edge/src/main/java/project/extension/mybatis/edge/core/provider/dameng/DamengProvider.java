package project.extension.mybatis.edge.core.provider.dameng;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.*;

/**
 * Dameng数据库对象构造器
 *
 * @author LCTR
 * @date 2022-05-27
 */
public class DamengProvider<T>
        implements IBaseDbProvider<T> {
    public DamengProvider(DataSourceConfig config) {
        this.config = config;
    }

    private final DataSourceConfig config;

    @Override
    public ISelect<T> createSelect(Class<T> entityType,
                                   INaiveAdo ado) {
        return new DamengSelect<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public IInsert<T> createInsert(Class<T> entityType,
                                   INaiveAdo ado) {
        return new DamengInsert<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public IUpdate<T> createUpdate(Class<T> entityType,
                                   INaiveAdo ado) {
        return new DamengUpdate<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public IDelete<T> createDelete(Class<T> entityType,
                                   INaiveAdo ado) {
        return new DamengDelete<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public IDbFirst createDbFirst(INaiveAdo ado) {
        return new DamengDbFirst(config,
                                 ado);
    }

    @Override
    public ICodeFirst createCodeFirst(INaiveAdo ado) {
        return new DamengCodeFirst(ado);
    }
}
