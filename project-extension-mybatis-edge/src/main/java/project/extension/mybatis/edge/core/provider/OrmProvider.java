package project.extension.mybatis.edge.core.provider;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import project.extension.action.IAction0Throw;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.core.mapper.EntityTypeHandler;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.ado.NaiveAdoProvider;
import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.core.provider.standard.curd.IDelete;
import project.extension.mybatis.edge.core.provider.standard.curd.IInsert;
import project.extension.mybatis.edge.core.provider.standard.curd.ISelect;
import project.extension.mybatis.edge.core.provider.standard.curd.IUpdate;
import project.extension.mybatis.edge.dbContext.repository.DefaultRepository;
import project.extension.mybatis.edge.dbContext.repository.DefaultRepository_Key;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.Collection;
import java.util.Map;

/**
 * ORM构造器
 *
 * @author LCTR
 * @date 2022-03-28
 */
@Component
public class OrmProvider
        implements INaiveSql {
    public OrmProvider() {
        this(CommonUtils.getConfig()
                        .getDataSource());
    }

    public OrmProvider(String dataSource) {
        this.dataSourceConfig = CommonUtils.getConfig()
                                           .getDataSourceConfig(dataSource);
        this.ado = new NaiveAdoProvider(dataSource);
        this.aop = IOCExtension.applicationContext.getBean(INaiveAop.class);
    }

    private final DataSourceConfig dataSourceConfig;

    private final INaiveAdo ado;

    private final INaiveAop aop;

    protected IBaseDbProvider createDbProvider() {
        return DbProvider.getDbProvider(this.dataSourceConfig);
    }

    @Override
    public <TEntity> ISelect<TEntity> select(Class<TEntity> entityType)
            throws
            ModuleException {
        return createDbProvider().createSelect(entityType,
                                               getAdo());
    }

    @Override
    public <TEntity> IInsert<TEntity> insert(Class<TEntity> entityType)
            throws
            ModuleException {
        return createDbProvider().createInsert(entityType,
                                               getAdo());
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
        return createDbProvider().createUpdate(entityType,
                                               getAdo());
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
        return createDbProvider().createDelete(entityType,
                                               getAdo());
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
    public Tuple2<Boolean, Exception> transaction(IAction0Throw handler) {
        return transaction(Propagation.REQUIRED,
                           null,
                           handler);
    }

    @Override
    public Tuple2<Boolean, Exception> transaction(Propagation propagation,
                                                  IAction0Throw handler) {
        return transaction(propagation,
                           null,
                           handler);
    }

    @Override
    public Tuple2<Boolean, Exception> transaction(TransactionIsolationLevel isolationLevel,
                                                  IAction0Throw handler) {
        return transaction(Propagation.REQUIRED,
                           isolationLevel,
                           handler);
    }

    @Override
    public Tuple2<Boolean, Exception> transaction(Propagation propagation,
                                                  TransactionIsolationLevel isolationLevel,
                                                  IAction0Throw handler) {
        boolean nested = false;
        boolean nonTransactional = false;

        try {
            switch (propagation) {
                case REQUIRES_NEW:
                    //创建一个新事务，并挂起当前事务（如果存在）。
                    if (getAdo().isTransactionAlreadyExisting())
                        getAdo().transactionCommit();
                case NESTED:
                    //如果存在当前事务，则在嵌套事务中执行，否则按REQUIRED执行。
                default:
                    //默认REQUIRED
                case REQUIRED:
                    //支持当前事务，如果不存在则创建一个新事务。
                    if (getAdo().isTransactionAlreadyExisting())
                        nested = true;
                    else {
                        if (isolationLevel == null)
                            getAdo().beginTransaction();
                        else
                            getAdo().beginTransaction(isolationLevel);
                    }
                    break;
                case SUPPORTS:
                    //支持当前事务，如果不存在，则以非事务方式执行。
                    if (getAdo().isTransactionAlreadyExisting())
                        nested = true;
                    else
                        nonTransactional = true;
                    break;
                case MANDATORY:
                    //支持当前事务，如果不存在则抛出异常。
                    if (getAdo().isTransactionAlreadyExisting())
                        nested = true;
                    else
                        throw new ModuleException(Strings.getTransactionNotStarted());
                    break;
                case NOT_SUPPORTED:
                    //以非事务方式执行，如果存在，则挂起当前事务。
                    if (getAdo().isTransactionAlreadyExisting())
                        getAdo().transactionCommit();
                    nonTransactional = true;
                    break;
                case NEVER:
                    //以非事务方式执行，如果存在事务则抛出异常。
                    if (getAdo().isTransactionAlreadyExisting())
                        throw new ModuleException(Strings.getTransactionHasBeenStarted());
                    nonTransactional = true;
                    break;
            }
        } catch (Exception ex) {
            getAdo().triggerAfterTransactionAop(Strings.getTransactionBeginFailed(),
                                                ex);
            return new Tuple2<>(false,
                                ex);
        }

        try {
            handler.invoke();

            if (nested || nonTransactional)
                return new Tuple2<>(true,
                                    null);

            try {
                getAdo().transactionCommit();
                return new Tuple2<>(true,
                                    null);
            } catch (Exception ex) {
                getAdo().triggerAfterTransactionAop(Strings.getTransactionCommitFailed(),
                                                    ex);
                return new Tuple2<>(false,
                                    ex);
            }
        } catch (Exception ex1) {
            if (nested || nonTransactional)
                return new Tuple2<>(false,
                                    ex1);

            try {
                getAdo().transactionRollback();
                return new Tuple2<>(false,
                                    ex1);
            } catch (Exception ex2) {
                getAdo().triggerAfterTransactionAop(Strings.getTransactionRollbackFailed(),
                                                    ex2);
                return new Tuple2<>(false,
                                    ex2);
            }
        }
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
        return createDbProvider().createDbFirst(ado);
    }

    @Override
    public ICodeFirst getCodeFirst()
            throws
            ModuleException {
        return createDbProvider().createCodeFirst(ado);
    }

    @Override
    public <TEntity> IBaseRepository<TEntity> getRepository(Class<TEntity> entityType)
            throws
            ModuleException {
        return new DefaultRepository<>(this,
                                       entityType);
    }

    @Override
    public <TEntity, TKey> IBaseRepository_Key<TEntity, TKey> getRepository_Key(Class<TEntity> entityType,
                                                                                Class<TKey> keyType)
            throws
            ModuleException {
        return new DefaultRepository_Key<>(this,
                                           entityType,
                                           keyType);
    }

    @Override
    public <TMapper> TMapper getMapper(Class<TMapper> mapperType) {
        return ado.getMapper(mapperType);
    }

    @Override
    public Object getMapValueByFieldName(Map<String, Object> mapResult,
                                         String fieldName) {
        return EntityTypeHandler.getMapValueByFieldName(mapResult,
                                                        fieldName,
                                                        dataSourceConfig.getNameConvertType());
    }
}
