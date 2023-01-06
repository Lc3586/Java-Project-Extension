package project.extension.mybatis.edge.dbContext.repository;

import org.apache.ibatis.session.TransactionIsolationLevel;
import project.extension.action.IAction0;
import project.extension.func.IFunc1;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.dbContext.DbContextScopedNaiveSql;
import project.extension.mybatis.edge.dbContext.RepositoryDbContext;
import project.extension.mybatis.edge.dbContext.unitOfWork.IUnitOfWork;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.mybatis.edge.model.DynamicSqlSetting;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.Collection;

/**
 * 数据仓储基础实现类
 * <p>1、事务由springframework进行管理</p>
 * <p>2、在需要使用事务的方法上请添加注解 org.springframework.transaction.annotation.Transactional</p>
 * <p>3、在使用事务时，请调用withTransactional()方法指定当前操作在事务下运行</p>
 *
 * @param <T> 数据类型
 * @author LCTR
 * @date 2022-03-29
 */
public class BaseRepository<T>
        implements IBaseRepository<T> {
    /**
     * @param entityType 实体类型
     * @param dbProvider 基础构造器
     */
    public BaseRepository(INaiveSql orm,
                          Class<T> entityType,
                          IBaseDbProvider<T> dbProvider) {
        this.ormScoped = DbContextScopedNaiveSql.create(orm,
                                                        this::getDb,
                                                        this::getUnitOfWork);
        this.setting = new DynamicSqlSetting<>(entityType);
        this.dbProvider = dbProvider;
    }

    protected DbContextScopedNaiveSql ormScoped;

    protected RepositoryDbContext db;

    protected IUnitOfWork unitOfWork;

    protected final DynamicSqlSetting<T> setting;
    protected final IBaseDbProvider<T> dbProvider;

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
        throw new ModuleException(DbContextStrings.getFunctionNotImplemented());
    }

    @Override
    public void asTable(IFunc1<String, String> change) {
        throw new ModuleException(DbContextStrings.getFunctionNotImplemented());
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
                getUnitOfWork().setIsolationLevel(isolationLevel);
            getUnitOfWork().getOrBeginTransaction();
            handler.invoke();
            getUnitOfWork().commit();
            return new Tuple2<>(true,
                                null);
        } catch (Exception ex) {
            getUnitOfWork().rollback();
            return new Tuple2<>(false,
                                ex);
        }
    }

    @Override
    public ISelect<T> select() {
        return dbProvider.createSelect(setting.getEntityType(),
                                       this.ormScoped.getAdo());
    }

    @Override
    public <T2> void insert(T2 data)
            throws
            Exception {
        if (dbProvider.createInsert(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .appendData(data)
                      .executeAffrows() <= 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void insert(T2 data,
                            Class<T2> dtoType,
                            Integer mainTagLevel)
            throws
            Exception {
        if (dbProvider.createInsert(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .appendData(data)
                      .asDto(dtoType)
                      .mainTagLevel(mainTagLevel)
                      .executeAffrows() <= 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void batchInsert(Collection<T2> data)
            throws
            Exception {
        if (dbProvider.createInsert(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .appendData(data)
                      .executeAffrows() != data.size())
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void batchInsert(Collection<T2> data,
                                 Class<T2> dtoType,
                                 Integer mainTagLevel)
            throws
            Exception {
        if (dbProvider.createInsert(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .appendData(data)
                      .asDto(dtoType)
                      .mainTagLevel(mainTagLevel)
                      .executeAffrows() != data.size())
            throw new Exception("执行操作失败");
    }

    @Override
    public IInsert<T> insertDiy() {
        return dbProvider.createInsert(setting.getEntityType(),
                                       this.ormScoped.getAdo());
    }

    @Override
    public <T2> void update(T2 data)
            throws
            Exception {
        if (dbProvider.createUpdate(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .setSource(data)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void update(T2 data,
                            Class<T2> dtoType,
                            Integer mainTagLevel)
            throws
            Exception {
        if (dbProvider.createUpdate(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .setSource(data)
                      .asDto(dtoType)
                      .mainTagLevel(mainTagLevel)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void batchUpdate(Collection<T2> data)
            throws
            Exception {
        if (dbProvider.createUpdate(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .setSource(data)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void batchUpdate(Collection<T2> data,
                                 Class<T2> dtoType,
                                 Integer mainTagLevel)
            throws
            Exception {
        if (dbProvider.createUpdate(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .setSource(data)
                      .asDto(dtoType)
                      .mainTagLevel(mainTagLevel)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public IUpdate<T> updateDiy() {
        return dbProvider.createUpdate(setting.getEntityType(),
                                       this.ormScoped.getAdo());
    }

    @Override
    public <T2> void delete(T2 data)
            throws
            Exception {
        if (dbProvider.createDelete(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .setSource(data)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void delete(T2 data,
                            Class<T2> dtoType)
            throws
            Exception {
        if (dbProvider.createDelete(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .setSource(data)
                      .asDto(dtoType)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void batchDelete(Collection<T2> data)
            throws
            Exception {
        if (dbProvider.createDelete(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .setSource(data)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public <T2> void batchDelete(Collection<T> data,
                                 Class<T2> dtoType)
            throws
            Exception {
        if (dbProvider.createDelete(setting.getEntityType(),
                                    this.ormScoped.getAdo())
                      .setSource(data)
                      .asDto(dtoType)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public IDelete<T> deleteDiy() {
        return dbProvider.createDelete(setting.getEntityType(),
                                       this.ormScoped.getAdo());
    }
}
