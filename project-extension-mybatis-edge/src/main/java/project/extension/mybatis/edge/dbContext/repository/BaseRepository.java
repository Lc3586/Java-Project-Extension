package project.extension.mybatis.edge.dbContext.repository;

import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.model.DynamicSqlSetting;

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
    protected final DynamicSqlSetting<T> setting;
    protected final IBaseDbProvider<T> dbProvider;

    protected boolean useTransactional = false;

    /**
     * @param entityType 实体类型
     * @param dbProvider 基础构造器
     */
    public BaseRepository(Class<T> entityType,
                          IBaseDbProvider<T> dbProvider) {
        this.setting = new DynamicSqlSetting<>(entityType);
        this.dbProvider = dbProvider;
    }

    @Override
    public IBaseRepository<T> withTransactional(boolean withTransactional) {
        this.useTransactional = withTransactional;
        return this;
    }

    @Override
    public ISelect<T> select() {
        return dbProvider.createSelect(setting.getEntityType(),
                                       useTransactional);
    }

    @Override
    public <T2> void insert(T2 data)
            throws
            Exception {
        if (dbProvider.createInsert(setting.getEntityType(),
                                    useTransactional)
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
                                    useTransactional)
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
                                    useTransactional)
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
                                    useTransactional)
                      .appendData(data)
                      .asDto(dtoType)
                      .mainTagLevel(mainTagLevel)
                      .executeAffrows() != data.size())
            throw new Exception("执行操作失败");
    }

    @Override
    public IInsert<T> insertDiy() {
        return dbProvider.createInsert(setting.getEntityType(),
                                       useTransactional);
    }

    @Override
    public <T2> void update(T2 data)
            throws
            Exception {
        if (dbProvider.createUpdate(setting.getEntityType(),
                                    useTransactional)
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
                                    useTransactional)
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
                                    useTransactional)
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
                                    useTransactional)
                      .setSource(data)
                      .asDto(dtoType)
                      .mainTagLevel(mainTagLevel)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public IUpdate<T> updateDiy() {
        return dbProvider.createUpdate(setting.getEntityType(),
                                       useTransactional);
    }

    @Override
    public <T2> void delete(T2 data)
            throws
            Exception {
        if (dbProvider.createDelete(setting.getEntityType(),
                                    useTransactional)
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
                                    useTransactional)
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
                                    useTransactional)
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
                                    useTransactional)
                      .setSource(data)
                      .asDto(dtoType)
                      .executeAffrows() < 0)
            throw new Exception("执行操作失败");
    }

    @Override
    public IDelete<T> deleteDiy() {
        return dbProvider.createDelete(setting.getEntityType(),
                                       useTransactional);
    }
}
