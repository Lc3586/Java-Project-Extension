package project.extension.mybatis.edge.dbContext.repository;

import org.apache.ibatis.session.TransactionIsolationLevel;
import project.extension.action.IAction0;
import project.extension.func.IFunc1;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.dbContext.DbContextScopedNaiveSql;
import project.extension.mybatis.edge.dbContext.RepositoryDbContext;
import project.extension.mybatis.edge.dbContext.unitOfWork.IUnitOfWork;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.DynamicSqlSetting;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.Collection;

/**
 * 数据仓储基础实现类
 *
 * @param <TEntity> 数据类型
 * @author LCTR
 * @date 2022-03-29
 */
public class BaseRepository<TEntity>
        implements IBaseRepository<TEntity> {
    /**
     * @param entityType 实体类型
     */
    public BaseRepository(INaiveSql orm,
                          Class<TEntity> entityType) {
        this.ormScoped = DbContextScopedNaiveSql.create(orm,
                                                        this::getDb,
                                                        this::getUnitOfWork);
        this.setting = new DynamicSqlSetting<>(entityType);
    }

    protected DbContextScopedNaiveSql ormScoped;

    protected RepositoryDbContext db;

    protected IUnitOfWork unitOfWork;

    protected final DynamicSqlSetting<TEntity> setting;

    private RepositoryDbContext getDb() {
        if (this.db == null)
            this.db = new RepositoryDbContext(getOrm(),
                                              this);
        return this.db;
    }

    @Override
    public Class<?> getEntityType() {
        return this.setting.getEntityType();
    }

    @Override
    public IUnitOfWork getUnitOfWork() {
        return this.unitOfWork;
    }

    @Override
    public void setUnitOfWork(IUnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
        if (this.db != null)
            this.db.setUnitOfWork(unitOfWork);
    }

    @Override
    public INaiveSql getOrm() {
        return this.ormScoped.getOriginalOrm();
    }

    @Override
    public void asType(Class<?> entityType) {
        throw new ModuleException(Strings.getFunctionNotImplemented());
    }

    @Override
    public void asTable(IFunc1<String, String> change) {
        throw new ModuleException(Strings.getFunctionNotImplemented());
    }

    @Override
    public Tuple2<Boolean, Exception> transaction(IAction0 handler) {
        return getOrm().transaction(handler);
    }

    @Override
    public Tuple2<Boolean, Exception> transaction(TransactionIsolationLevel isolationLevel,
                                                  IAction0 handler) {
        return getOrm().transaction(isolationLevel,
                                    handler);
    }

    @Override
    public ISelect<TEntity> select() {
        return getOrm().select(setting.getEntityType());
    }

    @Override
    public void insert(TEntity data)
            throws
            ModuleException {
        if (getOrm().insert(setting.getEntityType(),
                            data)
                    .executeAffrows() <= 0)
            throw new ModuleException(Strings.getInsertDataFailed());
    }

    @Override
    public <TDto> void insert(TDto data,
                              Class<TDto> dtoType,
                              Integer mainTagLevel)
            throws
            ModuleException {
        if (this.ormScoped.insert(setting.getEntityType(),
                                  data,
                                  dtoType,
                                  mainTagLevel)
                          .executeAffrows() <= 0)
            throw new ModuleException(Strings.getInsertDataFailed());
    }

    @Override
    public void batchInsert(Collection<TEntity> data)
            throws
            ModuleException {
        if (getOrm().batchInsert(setting.getEntityType(),
                                 data)
                    .executeAffrows() != data.size())
            throw new ModuleException(Strings.getInsertDataFailed());
    }

    @Override
    public <TDto> void batchInsert(Collection<TDto> data,
                                   Class<TDto> dtoType,
                                   Integer mainTagLevel)
            throws
            ModuleException {
        if (getOrm().batchInsert(setting.getEntityType(),
                                 data,
                                 dtoType,
                                 mainTagLevel)
                    .executeAffrows() != data.size())
            throw new ModuleException(Strings.getInsertDataFailed());
    }

    @Override
    public IInsert<TEntity> insertDiy() {
        return getOrm().insert(setting.getEntityType());
    }

    @Override
    public void update(TEntity data)
            throws
            ModuleException {
        if (getOrm().update(setting.getEntityType(),
                            data)
                    .executeAffrows() < 0)
            throw new ModuleException(Strings.getUpdateDataFailed());
    }

    @Override
    public <TDto> void update(TDto data,
                              Class<TDto> dtoType,
                              Integer mainTagLevel)
            throws
            ModuleException {
        if (getOrm().update(setting.getEntityType(),
                            data,
                            dtoType,
                            mainTagLevel)
                    .executeAffrows() < 0)
            throw new ModuleException(Strings.getUpdateDataFailed());
    }

    @Override
    public void batchUpdate(Collection<TEntity> data)
            throws
            ModuleException {
        if (getOrm().batchUpdate(setting.getEntityType(),
                                 data)
                    .executeAffrows() < 0)
            throw new ModuleException(Strings.getUpdateDataFailed());
    }

    @Override
    public <TDto> void batchUpdate(Collection<TDto> data,
                                   Class<TDto> dtoType,
                                   Integer mainTagLevel)
            throws
            ModuleException {
        if (getOrm().batchUpdate(setting.getEntityType(),
                                 data,
                                 dtoType,
                                 mainTagLevel)
                    .executeAffrows() < 0)
            throw new ModuleException(Strings.getUpdateDataFailed());
    }

    @Override
    public IUpdate<TEntity> updateDiy() {
        return getOrm().update(setting.getEntityType());
    }

    @Override
    public void delete(TEntity data)
            throws
            ModuleException {
        if (getOrm().delete(setting.getEntityType(),
                            data)
                    .executeAffrows() < 0)
            throw new ModuleException(Strings.getDeleteDataFailed());
    }

    @Override
    public <TDto> void delete(TDto data,
                              Class<TDto> dtoType)
            throws
            ModuleException {
        if (getOrm().delete(setting.getEntityType(),
                            data,
                            dtoType)
                    .executeAffrows() < 0)
            throw new ModuleException(Strings.getDeleteDataFailed());
    }

    @Override
    public void batchDelete(Collection<TEntity> data)
            throws
            ModuleException {
        if (getOrm().batchDelete(setting.getEntityType(),
                                 data)
                    .executeAffrows() < 0)
            throw new ModuleException(Strings.getDeleteDataFailed());
    }

    @Override
    public <TDto> void batchDelete(Collection<TDto> data,
                                   Class<TDto> dtoType)
            throws
            ModuleException {
        if (getOrm().batchDelete(setting.getEntityType(),
                                 data,
                                 dtoType)
                    .executeAffrows() < 0)
            throw new ModuleException(Strings.getDeleteDataFailed());
    }

    @Override
    public IDelete<TEntity> deleteDiy() {
        return getOrm().delete(setting.getEntityType());
    }
}
