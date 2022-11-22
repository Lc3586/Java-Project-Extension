package project.extension.mybatis.core.provider;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import project.extension.mybatis.INaiveSql;
import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.provider.standard.IAop;
import project.extension.mybatis.core.provider.standard.ICodeFirst;
import project.extension.mybatis.core.provider.standard.IDbFirst;
import project.extension.mybatis.core.repository.DefaultRepository;
import project.extension.mybatis.core.repository.DefaultRepository_Key;
import project.extension.mybatis.core.repository.IBaseRepository;
import project.extension.mybatis.core.repository.IBaseRepository_Key;

/**
 * 数据仓储构造器
 *
 * @author LCTR
 * @date 2022-03-28
 */
@Component
@DependsOn({"NaiveSqlSession",
            "RepositoryExtension"})
public class BaseProvider
        implements INaiveSql {
    public BaseProvider(BaseConfig config,
                        IAop aop) {
        this.config = config;
        this.aop = aop;
    }

    private final BaseConfig config;

    private final IAop aop;

    @Override
    public <T> IBaseRepository<T> getRepository(Class<T> entityType)
            throws
            Exception {
        return new DefaultRepository<>(config,
                                       entityType,
                                       DbProvider.getDbProvider(config,
                                                                aop));
    }

    @Override
    public <T> IBaseRepository<T> getRepository(Class<T> entityType,
                                                String dataSource)
            throws
            Exception {
        return new DefaultRepository<>(config,
                                       entityType,
                                       DbProvider.getDbProvider(config.getMultiConfig(dataSource),
                                                                aop));
    }

    @Override
    public <T, TKey> IBaseRepository_Key<T, TKey> getRepository_Key(Class<T> entityType,
                                                                    Class<TKey> keyType)
            throws
            Exception {
        return new DefaultRepository_Key<>(config,
                                           entityType,
                                           keyType,
                                           DbProvider.getDbProvider(config,
                                                                    aop));
    }

    @Override
    public <T, TKey> IBaseRepository_Key<T, TKey> getRepository_Key(Class<T> entityType,
                                                                    Class<TKey> keyType,
                                                                    String dataSource)
            throws
            Exception {
        return new DefaultRepository_Key<>(config,
                                           entityType,
                                           keyType,
                                           DbProvider.getDbProvider(config.getMultiConfig(dataSource),
                                                                    aop));
    }

    @Override
    public IDbFirst getDbFirst()
            throws
            Exception {
        return DbProvider.getDbProvider(config,
                                        aop)
                         .createDbFirst();
    }

    @Override
    public IDbFirst getDbFirst(String dataSource)
            throws
            Exception {
        return DbProvider.getDbProvider(config.getMultiConfig(dataSource),
                                        aop)
                         .createDbFirst();
    }

    @Override
    public ICodeFirst getCodeFirst()
            throws
            Exception {
        return DbProvider.getDbProvider(config,
                                        aop)
                         .createCodeFirst();
    }

    @Override
    public ICodeFirst getCodeFirst(String dataSource)
            throws
            Exception {
        return DbProvider.getDbProvider(config.getMultiConfig(dataSource),
                                        aop)
                         .createCodeFirst();
    }
}
