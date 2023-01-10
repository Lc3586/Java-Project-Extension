package project.extension.mybatis.edge.core.provider;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import project.extension.action.IAction0;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.ado.NaiveAdoProvider;
import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.dbContext.repository.DefaultRepository;
import project.extension.mybatis.edge.dbContext.repository.DefaultRepository_Key;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.mybatis.edge.dbContext.unitOfWork.IUnitOfWork;
import project.extension.mybatis.edge.dbContext.unitOfWork.UnitOfWork;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.Collection;

/**
 * ORM构造器
 *
 * @author LCTR
 * @date 2022-03-28
 */
@Component
@DependsOn("IOCExtension")
public class OrmProvider
        implements INaiveSql {
    public OrmProvider() {
        this(CommonUtils.getConfig()
                        .getDataSource());
    }

    public OrmProvider(String dataSource) {
        this.dataSourceConfig = CommonUtils.getConfig()
                                           .getDataSourceConfig(dataSource);
        this.uow = new UnitOfWork(this);
        this.ado = new NaiveAdoProvider(dataSource);
        this.ado.setResolveTransaction(() -> uow.getOrBeginTransaction(false));
        this.aop = IOCExtension.applicationContext.getBean(INaiveAop.class);
    }

    private final DataSourceConfig dataSourceConfig;

    private final IUnitOfWork uow;

    private final INaiveAdo ado;

    private final INaiveAop aop;

    private IBaseDbProvider createDbProvider() {
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
    public Tuple2<Boolean, Exception> transaction(IAction0 handler) {
        return transaction(null,
                           handler);
    }

    @Override
    public Tuple2<Boolean, Exception> transaction(TransactionIsolationLevel isolationLevel,
                                                  IAction0 handler) {
        try {
            if (isolationLevel != null)
                this.uow.setIsolationLevel(isolationLevel);
            this.uow.getOrBeginTransaction();
            handler.invoke();
            this.uow.commit();
            return new Tuple2<>(true,
                                null);
        } catch (Exception ex) {
            this.uow.rollback();
            return new Tuple2<>(false,
                                ex);
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
}
