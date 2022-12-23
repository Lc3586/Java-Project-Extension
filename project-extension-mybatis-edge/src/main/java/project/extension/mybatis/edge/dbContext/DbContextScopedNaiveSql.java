package project.extension.mybatis.edge.dbContext;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import project.extension.func.IFunc0;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.ICodeFirst;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.dbContext.unitOfWork.IUnitOfWork;
import project.extension.object.ObjectExtension;
import project.extension.standard.exception.ApplicationException;

/**
 * 有生命周期的Orm对象
 *
 * @author LCTR
 * @date 2022-12-19
 */
@Component
public class DbContextScopedNaiveSql
        implements INaiveSql {
    private DbContextScopedNaiveSql(BaseConfig config,
                                    AbstractRoutingDataSource abstractRoutingDataSource,

                                    INaiveSql orm,
                                    IFunc0<DbContext> resolveDbContext,
                                    IFunc0<IUnitOfWork> resolveUnitOfWork,
                                    INaiveAdo ado) {
        this.config = config;
        this.abstractRoutingDataSource = abstractRoutingDataSource;
        this.originalOrm = orm;
        this.resolveDbContext = resolveDbContext;
        this.resolveUnitOfWork = resolveUnitOfWork;
        this.ado = ado;
    }

    private final BaseConfig config;

    private final AbstractRoutingDataSource abstractRoutingDataSource;

    /**
     * 原始的Orm对象
     */
    private INaiveSql originalOrm;

    /**
     * 获取数据源上下文
     */
    private IFunc0<DbContext> resolveDbContext;

    /**
     * 获取工作单元
     */
    private IFunc0<IUnitOfWork> resolveUnitOfWork;

    /**
     * 数据库访问对象
     */
    private INaiveAdo ado;

    /**
     * 创建
     *
     * @param config            配置
     * @param orm               Orm对象
     * @param resolveDbContext  获取数据源上下文
     * @param resolveUnitOfWork 获取工作单元
     * @return 有生命周期的Orm对象
     */
    public static DbContextScopedNaiveSql create(BaseConfig config,
                                                 INaiveSql orm,
                                                 IFunc0<DbContext> resolveDbContext,
                                                 IFunc0<IUnitOfWork> resolveUnitOfWork) {
        if (orm == null) return null;
        DbContextScopedNaiveSql scopedOrm = ObjectExtension.as(orm,
                                                               DbContextScopedNaiveSql.class);
        if (scopedOrm == null)
            return new DbContextScopedNaiveSql(orm,
                                               resolveDbContext,
                                               resolveUnitOfWork,
                                               new ScopeTransactionAdo(config,
                                                                       orm.getAdo()
                                                                          .getDataSource(),
                                                                       (sqlSessionFactory, executorType) -> {
                                                                           DbContext dbContext = resolveDbContext.invoke();
                                                                           dbContext.
                                                                           return sqlSessionFactory.openSession(executorType, );
                                                                       }));
        return create(scopedOrm.getOriginalOrm(),
                      resolveDbContext,
                      resolveUnitOfWork,);
    }

    /**
     * 原始的Orm对象
     */
    public INaiveSql getOriginalOrm() {
        return originalOrm;
    }

    /**
     * 获取数据仓储
     *
     * @param entityType 实体类型
     * @return 数据仓储
     */
    @Override
    public <T> IBaseRepository<T> getRepository(Class<T> entityType)
            throws
            ApplicationException {
        return null;
    }

    /**
     * 获取数据仓储
     *
     * @param entityType 实体类型
     * @param keyType    主键类型
     * @return 数据仓储
     */
    @Override
    public <T, TKey> IBaseRepository_Key<T, TKey> getRepository_Key(Class<T> entityType,
                                                                    Class<TKey> keyType)
            throws
            ApplicationException {
        return null;
    }

    /**
     * 数据库访问对象
     *
     * @return 数据库访问对象
     */
    public INaiveAdo getAdo() {
        return this.ado;
    }

    /**
     * 获取数据库访问对象
     */
    @Override
    public INaiveAop getAop()
            throws
            ApplicationException {
        return null;
    }

    /**
     * 获取DbFirst 开发模式相关功能
     */
    @Override
    public IDbFirst getDbFirst()
            throws
            ApplicationException {
        return null;
    }

    /**
     * 获取DbFirst 开发模式相关功能
     */
    @Override
    public ICodeFirst getCodeFirst()
            throws
            ApplicationException {
        return null;
    }
}
