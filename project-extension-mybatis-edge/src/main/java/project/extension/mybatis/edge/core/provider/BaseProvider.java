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
import project.extension.standard.exception.ModuleException;

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
            ModuleException {
        return new DefaultRepository<>(this,
                                       entityType,
                                       DbProvider.getDbProvider(CommonUtils.getConfig()
                                                                           .getDataSourceConfig()));
    }

    @Override
    public <T, TKey> IBaseRepository_Key<T, TKey> getRepository_Key(Class<T> entityType,
                                                                    Class<TKey> keyType)
            throws
            ModuleException {
        return new DefaultRepository_Key<>(this,
                                           entityType,
                                           keyType,
                                           DbProvider.getDbProvider(CommonUtils.getConfig()
                                                                               .getDataSourceConfig()));
    }

    /**
     * 获取数据库访问对象
     */
    @Override
    public INaiveAdo getAdo()
            throws
            ModuleException {
        return this.ado;
    }

    /**
     * 获取数据库访问对象
     */
    @Override
    public INaiveAop getAop()
            throws
            ModuleException {
        return this.aop;
    }

    @Override
    public IDbFirst getDbFirst()
            throws
            ModuleException {
        return DbProvider.getDbProvider(CommonUtils.getConfig()
                                                   .getDataSourceConfig())
                         .createDbFirst(ado);
    }

    @Override
    public ICodeFirst getCodeFirst()
            throws
            ModuleException {
        return DbProvider.getDbProvider(CommonUtils.getConfig()
                                                   .getDataSourceConfig())
                         .createCodeFirst(ado);
    }
}
