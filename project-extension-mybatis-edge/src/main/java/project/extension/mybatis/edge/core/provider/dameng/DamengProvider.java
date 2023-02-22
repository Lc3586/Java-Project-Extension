package project.extension.mybatis.edge.core.provider.dameng;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.dameng.curd.DamengDelete;
import project.extension.mybatis.edge.core.provider.dameng.curd.DamengInsert;
import project.extension.mybatis.edge.core.provider.dameng.curd.DamengSelect;
import project.extension.mybatis.edge.core.provider.dameng.curd.DamengUpdate;
import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.core.provider.standard.curd.IDelete;
import project.extension.mybatis.edge.core.provider.standard.curd.IInsert;
import project.extension.mybatis.edge.core.provider.standard.curd.ISelect;
import project.extension.mybatis.edge.core.provider.standard.curd.IUpdate;

/**
 * Dameng数据库对象构造器
 *
 * @author LCTR
 * @date 2022-05-27
 */
public class DamengProvider
        implements IBaseDbProvider {
    public DamengProvider(DataSourceConfig config) {
        this.config = config;
    }

    private final DataSourceConfig config;

    @Override
    public <TEntity> ISelect<TEntity> createSelect(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new DamengSelect<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public <TEntity> IInsert<TEntity> createInsert(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new DamengInsert<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public <TEntity> IUpdate<TEntity> createUpdate(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new DamengUpdate<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public <TEntity> IDelete<TEntity> createDelete(Class<TEntity> entityType,
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
        return new DamengCodeFirst(config,
                                   ado);
    }
}
