package project.extension.mybatis.edge.core.provider;

import org.springframework.stereotype.Component;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.ado.NaiveAdoProvider;
import project.extension.mybatis.edge.core.ado.NaiveDataSource;
import project.extension.mybatis.edge.core.provider.standard.ICodeFirst;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.dbContext.repository.DefaultRepository;
import project.extension.mybatis.edge.dbContext.repository.DefaultRepository_Key;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.standard.exception.ApplicationException;

/**
 * 数据仓储构造器
 *
 * @author LCTR
 * @date 2022-03-28
 */
@Component
public class BaseProvider
        implements INaiveSql {
    public BaseProvider(NaiveDataSource naiveDataSource,
                        INaiveAop aop) {
        this.ado = new NaiveAdoProvider(naiveDataSource.getResolvedDataSources()
                                                       .get(CommonUtils.getConfig()
                                                                       .getDataSource()));
        this.aop = aop;
    }

    private final INaiveAdo ado;

    private final INaiveAop aop;

    @Override
    public <T> IBaseRepository<T> getRepository(Class<T> entityType)
            throws
            ApplicationException {
        return new DefaultRepository<>(entityType,
                                       DbProvider.getDbProvider(CommonUtils.getConfig()
                                                                           .getDataSourceConfig(),
                                                                this.aop));
    }

    @Override
    public <T, TKey> IBaseRepository_Key<T, TKey> getRepository_Key(Class<T> entityType,
                                                                    Class<TKey> keyType)
            throws
            ApplicationException {
        return new DefaultRepository_Key<>(entityType,
                                           keyType,
                                           DbProvider.getDbProvider(CommonUtils.getConfig()
                                                                               .getDataSourceConfig(),
                                                                    this.aop));
    }

    /**
     * 获取数据库访问对象
     */
    @Override
    public INaiveAdo getAdo()
            throws
            ApplicationException {
        return this.ado;
    }

    /**
     * 获取数据库访问对象
     */
    @Override
    public INaiveAop getAop()
            throws
            ApplicationException {
        return this.aop;
    }

    @Override
    public IDbFirst getDbFirst()
            throws
            ApplicationException {
        return DbProvider.getDbProvider(CommonUtils.getConfig()
                                                   .getDataSourceConfig(),
                                        this.aop)
                         .createDbFirst();
    }

    @Override
    public ICodeFirst getCodeFirst()
            throws
            ApplicationException {
        return DbProvider.getDbProvider(CommonUtils.getConfig()
                                                   .getDataSourceConfig(),
                                        this.aop)
                         .createCodeFirst();
    }
}
