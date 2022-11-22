package project.extension.mybatis.edge.core.provider.dameng;

import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.provider.standard.*;

/**
 * Dameng数据库对象构造器
 *
 * @author LCTR
 * @date 2022-05-27
 */
public class DamengProvider<T>
        implements IBaseDbProvider<T> {
    public DamengProvider(BaseConfig config,
                          IAop aop) {
        this.config = config;
        this.aop = aop;
    }

    private final BaseConfig config;

    private final IAop aop;

    @Override
    public ISelect<T> createSelect(Class<T> entityType,
                                   boolean withTransactional) {
        return new DamengSelect<>(config,
                                  aop,
                                  entityType,
                                  withTransactional);
    }

    @Override
    public IInsert<T> createInsert(Class<T> entityType,
                                   boolean withTransactional) {
        return new DamengInsert<>(config,
                                  aop,
                                  entityType,
                                  withTransactional);
    }

    @Override
    public IUpdate<T> createUpdate(Class<T> entityType,
                                   boolean withTransactional) {
        return new DamengUpdate<>(config,
                                  aop,
                                  entityType,
                                  withTransactional);
    }

    @Override
    public IDelete<T> createDelete(Class<T> entityType,
                                   boolean withTransactional) {
        return new DamengDelete<>(config,
                                  aop,
                                  entityType,
                                  withTransactional);
    }

    @Override
    public IDbFirst createDbFirst() {
        return new DamengDbFirst(config);
    }

    @Override
    public ICodeFirst createCodeFirst() {
        return new DamengCodeFirst();
    }
}
