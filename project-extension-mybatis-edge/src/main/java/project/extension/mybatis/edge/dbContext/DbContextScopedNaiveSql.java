package project.extension.mybatis.edge.dbContext;

import org.apache.ibatis.session.TransactionIsolationLevel;
import project.extension.action.IAction0;
import project.extension.func.IFunc0;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.core.provider.standard.curd.IDelete;
import project.extension.mybatis.edge.core.provider.standard.curd.IInsert;
import project.extension.mybatis.edge.core.provider.standard.curd.ISelect;
import project.extension.mybatis.edge.core.provider.standard.curd.IUpdate;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.dbContext.unitOfWork.IUnitOfWork;
import project.extension.object.ObjectExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.Collection;

/**
 * 有生命周期的Orm对象
 *
 * @author LCTR
 * @date 2022-12-19
 */
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
     * 工作单元
     */
    private IUnitOfWork uow;

    /**
     * 数据库访问对象
     */
    private final INaiveAdo ado;

    /**
     * AOP编程对象
     */
    private final INaiveAop aop;

    private void before() {
        if (this.resolveDbContext != null) {
            DbContext dbContext = this.resolveDbContext.invoke();
            if (dbContext != null)
                dbContext.flushCommand();
        }

        if (this.resolveUnitOfWork != null) {
            IUnitOfWork uow = this.resolveUnitOfWork.invoke();
            if (uow != null)
                uow.getOrBeginTransaction();
        }
    }

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
                                                                          .getDataSourceName(),
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

    @Override
    public <TEntity> ISelect<TEntity> select(Class<TEntity> entityType)
            throws
            ModuleException {
        before();
        return getOriginalOrm().select(entityType);
    }

    @Override
    public <TEntity> IInsert<TEntity> insert(Class<TEntity> entityType)
            throws
            ModuleException {
        before();
        return getOriginalOrm().insert(entityType);
    }

    @Override
    public <TEntity> IInsert<TEntity> insert(Class<TEntity> entityType,
                                             TEntity data)
            throws
            ModuleException {
        return insert(entityType).appendData(data);
    }

    @Override
    public <TEntity, TDto> IInsert<TEntity> insert(Class<TEntity> entityType,
                                                   TDto data,
                                                   Class<TDto> dtoType,
                                                   Integer mainTagLevel)
            throws
            ModuleException {
        return insert(entityType).appendData(data)
                                 .asDto(dtoType)
                                 .mainTagLevel(mainTagLevel);
    }

    @Override
    public <TEntity> IInsert<TEntity> batchInsert(Class<TEntity> entityType,
                                                  Collection<TEntity> data)
            throws
            ModuleException {
        return insert(entityType).appendData(data);
    }

    @Override
    public <TEntity, TDto> IInsert<TEntity> batchInsert(Class<TEntity> entityType,
                                                        Collection<TDto> data,
                                                        Class<TDto> dtoType,
                                                        Integer mainTagLevel)
            throws
            ModuleException {
        return insert(entityType).appendData(data)
                                 .asDto(dtoType)
                                 .mainTagLevel(mainTagLevel);
    }

    @Override
    public <TEntity> IUpdate<TEntity> update(Class<TEntity> entityType)
            throws
            ModuleException {
        before();
        return getOriginalOrm().update(entityType);
    }

    @Override
    public <TEntity> IUpdate<TEntity> update(Class<TEntity> entityType,
                                             TEntity data)
            throws
            ModuleException {
        return update(entityType).setSource(data);
    }

    @Override
    public <TEntity, TDto> IUpdate<TEntity> update(Class<TEntity> entityType,
                                                   TDto data,
                                                   Class<TDto> dtoType,
                                                   Integer mainTagLevel)
            throws
            ModuleException {
        return update(entityType).setSource(data)
                                 .asDto(dtoType)
                                 .mainTagLevel(mainTagLevel);
    }

    @Override
    public <TEntity> IUpdate<TEntity> batchUpdate(Class<TEntity> entityType,
                                                  Collection<TEntity> data)
            throws
            ModuleException {
        return update(entityType).setSource(data);
    }

    @Override
    public <TEntity, TDto> IUpdate<TEntity> batchUpdate(Class<TEntity> entityType,
                                                        Collection<TDto> data,
                                                        Class<TDto> dtoType,
                                                        Integer mainTagLevel)
            throws
            ModuleException {
        return update(entityType).setSource(data)
                                 .asDto(dtoType)
                                 .mainTagLevel(mainTagLevel);
    }

    @Override
    public <TEntity> IDelete<TEntity> delete(Class<TEntity> entityType)
            throws
            ModuleException {
        before();
        return getOriginalOrm().delete(entityType);
    }

    @Override
    public <TEntity> IDelete<TEntity> delete(Class<TEntity> entityType,
                                             TEntity data)
            throws
            ModuleException {
        return delete(entityType).setSource(data);
    }

    @Override
    public <TEntity, TDto> IDelete<TEntity> delete(Class<TEntity> entityType,
                                                   TDto data,
                                                   Class<TDto> dtoType)
            throws
            ModuleException {
        return delete(entityType).setSource(data)
                                 .asDto(dtoType);
    }

    @Override
    public <TEntity> IDelete<TEntity> batchDelete(Class<TEntity> entityType,
                                                  Collection<TEntity> data)
            throws
            ModuleException {
        return delete(entityType).setSource(data);
    }

    @Override
    public <TEntity, TDto> IDelete<TEntity> batchDelete(Class<TEntity> entityType,
                                                        Collection<TDto> data,
                                                        Class<TDto> dtoType)
            throws
            ModuleException {
        return delete(entityType).setSource(data)
                                 .asDto(dtoType);
    }

    @Override
    public Tuple2<Boolean, Exception> transaction(IAction0 handler) {
        return getOriginalOrm().transaction(handler);
    }

    @Override
    public Tuple2<Boolean, Exception> transaction(TransactionIsolationLevel isolationLevel,
                                                  IAction0 handler) {
        return getOriginalOrm().transaction(isolationLevel,
                                            handler);
    }

    @Override
    public INaiveAdo getAdo() {
        return this.ado;
    }

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
        return getOriginalOrm().getDbFirst();
    }

    @Override
    public ICodeFirst getCodeFirst()
            throws
            ModuleException {
        return getOriginalOrm().getCodeFirst();
    }

    @Override
    public <T> IBaseRepository<T> getRepository(Class<T> entityType)
            throws
            ModuleException {
        return getOriginalOrm().getRepository(entityType);
    }

    @Override
    public <T, TKey> IBaseRepository_Key<T, TKey> getRepository_Key(Class<T> entityType,
                                                                    Class<TKey> keyType)
            throws
            ModuleException {
        return getOriginalOrm().getRepository_Key(entityType,
                                                  keyType);
    }

    @Override
    public <TMapper> TMapper getMapper(Class<TMapper> mapperType) {
        return getOriginalOrm().getMapper(mapperType);
    }
}
