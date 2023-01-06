package project.extension.mybatis.edge.dbContext;

import org.springframework.stereotype.Component;
import project.extension.func.IFunc0;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.DbProvider;
import project.extension.mybatis.edge.core.provider.standard.ICodeFirst;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.dbContext.unitOfWork.IUnitOfWork;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.object.ObjectExtension;
import project.extension.standard.exception.ModuleException;

/**
 * 有生命周期的Orm对象
 *
 * @author LCTR
 * @date 2022-12-19
 */
@Component
public class DbContextScopedNaiveSql
        implements INaiveSql {
    private DbContextScopedNaiveSql(INaiveSql orm,
                                    IFunc0<DbContext> resolveDbContext,
                                    IFunc0<IUnitOfWork> resolveUnitOfWork,
                                    INaiveAdo ado) {
        this.originalOrm = orm;
        this.resolveDbContext = resolveDbContext;
        this.resolveUnitOfWork = resolveUnitOfWork;
        this.ado = ado;
        this.aop = orm.getAop();
    }

    /**
     * 原始的Orm对象
     */
    private final INaiveSql originalOrm;

    /**
     * 获取数据源上下文
     */
    private final IFunc0<DbContext> resolveDbContext;

    /**
     * 获取工作单元
     */
    private final IFunc0<IUnitOfWork> resolveUnitOfWork;

    /**
     * 数据库访问对象
     */
    private final INaiveAdo ado;

    private final INaiveAop aop;

    /**
     * 创建
     *
     * @param orm               Orm对象
     * @param resolveDbContext  获取数据源上下文
     * @param resolveUnitOfWork 获取工作单元
     * @return 有生命周期的Orm对象
     */
    public static DbContextScopedNaiveSql create(INaiveSql orm,
                                                 IFunc0<DbContext> resolveDbContext,
                                                 IFunc0<IUnitOfWork> resolveUnitOfWork) {
        if (orm == null) return null;
        DbContextScopedNaiveSql scopedOrm = ObjectExtension.as(orm,
                                                               DbContextScopedNaiveSql.class);
        if (scopedOrm == null)
            return new DbContextScopedNaiveSql(orm,
                                               resolveDbContext,
                                               resolveUnitOfWork,
                                               new ScopeTransactionAdo(orm.getAdo()
                                                                          .getDataSource(),
                                                                       () -> {
                                                                           if (resolveDbContext != null) {
                                                                               DbContext dbContext = resolveDbContext.invoke();
                                                                               dbContext.flushCommand();
                                                                           }
                                                                           if (resolveUnitOfWork != null) {
                                                                               IUnitOfWork uow = resolveUnitOfWork.invoke();
                                                                               return uow.getOrBeginTransaction();
                                                                           }
                                                                           return null;
                                                                       }));
        return create(scopedOrm.getOriginalOrm(),
                      resolveDbContext,
                      resolveUnitOfWork);
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
            ModuleException {
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
            ModuleException {
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
            ModuleException {
        return this.aop;
    }

    /**
     * 获取DbFirst 开发模式相关功能
     */
    @Override
    public IDbFirst getDbFirst()
            throws
            ModuleException {
        return DbProvider.getDbProvider(CommonUtils.getConfig()
                                                   .getDataSourceConfig())
                         .createDbFirst(ado);
    }

    /**
     * 获取DbFirst 开发模式相关功能
     */
    @Override
    public ICodeFirst getCodeFirst()
            throws
            ModuleException {
        return DbProvider.getDbProvider(CommonUtils.getConfig()
                                                   .getDataSourceConfig())
                         .createCodeFirst(ado);
    }
}
