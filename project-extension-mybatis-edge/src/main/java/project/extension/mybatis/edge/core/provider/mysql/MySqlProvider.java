package project.extension.mybatis.edge.core.provider.mysql;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.standard.*;

/**
 * MySql数据库对象构造器
 *
 * @author LCTR
 * @date 2022-03-30
 */
public class MySqlProvider<T>
        implements IBaseDbProvider<T> {
    public MySqlProvider(DataSourceConfig config,
                         IAop aop) {
        this.config = config;
        this.aop = aop;
    }

    private final DataSourceConfig config;

    private final IAop aop;

    @Override
    public ISelect<T> createSelect(Class<T> entityType,
                                   boolean withTransactional) {
        return new MySqlSelect<>(config,
                                 aop,
                                 entityType,
                                 withTransactional);
    }

    @Override
    public IInsert<T> createInsert(Class<T> entityType,
                                   boolean withTransactional) {
        return new MySqlInsert<>(config,
                                 aop,
                                 entityType,
                                 withTransactional);
    }

    @Override
    public IUpdate<T> createUpdate(Class<T> entityType,
                                   boolean withTransactional) {
        return new MySqlUpdate<>(config,
                                 aop,
                                 entityType,
                                 withTransactional);
    }

    @Override
    public IDelete<T> createDelete(Class<T> entityType,
                                   boolean withTransactional) {
        return new MySqlDelete<>(config,
                                 aop,
                                 entityType,
                                 withTransactional);
    }

    @Override
    public IDbFirst createDbFirst() {
        return new MySqlDbFirst(config);
    }

    @Override
    public ICodeFirst createCodeFirst() {
        return new MySqlCodeFirst();
    }
}
