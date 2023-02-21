package project.extension.mybatis.edge.core.provider.standard;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.stereotype.Repository;
import project.extension.action.IAction0;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.*;
import project.extension.mybatis.edge.core.provider.standard.curd.IDelete;
import project.extension.mybatis.edge.core.provider.standard.curd.IInsert;
import project.extension.mybatis.edge.core.provider.standard.curd.ISelect;
import project.extension.mybatis.edge.core.provider.standard.curd.IUpdate;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.Collection;

/**
 * NaiveSql
 * <p>1、事务由springframework进行管理</p>
 * <p>2、在需要使用事务的方法上请添加注解 org.springframework.transaction.annotation.Transactional</p>
 *
 * @author LCTR
 * @date 2022-06-10
 */
@Repository
public interface INaiveSql {
    /**
     * 数据查询对象
     *
     * @param entityType 实体类型
     * @param <TEntity>  实体类型
     */
    <TEntity> ISelect<TEntity> select(Class<TEntity> entityType)
            throws
            ModuleException;

    /**
     * 数据插入对象
     *
     * @param entityType 实体类型
     * @param <TEntity>  实体类型
     */
    <TEntity> IInsert<TEntity> insert(Class<TEntity> entityType)
            throws
            ModuleException;

    /**
     * 插入一条记录
     *
     * @param data       数据
     * @param entityType 实体类型
     * @param <TEntity>  实体类型
     */
    <TEntity> IInsert<TEntity> insert(Class<TEntity> entityType,
                                      TEntity data)
            throws
            ModuleException;

    /**
     * 插入一条记录
     *
     * @param entityType   实体类型
     * @param data         数据
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TEntity>    实体类型
     * @param <TDto>       业务模型类型
     */
    <TEntity, TDto> IInsert<TEntity> insert(Class<TEntity> entityType,
                                            TDto data,
                                            Class<TDto> dtoType,
                                            Integer mainTagLevel)
            throws
            ModuleException;

    /**
     * 批量插入记录
     *
     * @param entityType 实体类型
     * @param data       数据集合
     * @param <TEntity>  实体类型
     */
    <TEntity> IInsert<TEntity> batchInsert(Class<TEntity> entityType,
                                           Collection<TEntity> data)
            throws
            ModuleException;

    /**
     * 批量插入记录
     *
     * @param entityType   实体类型
     * @param data         数据集合
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TEntity>    实体类型
     * @param <TDto>       业务模型类型
     */
    <TEntity, TDto> IInsert<TEntity> batchInsert(Class<TEntity> entityType,
                                                 Collection<TDto> data,
                                                 Class<TDto> dtoType,
                                                 Integer mainTagLevel)
            throws
            ModuleException;

    /**
     * 数据更新对象
     *
     * @param entityType 实体类型
     * @param <TEntity>  实体类型
     */
    <TEntity> IUpdate<TEntity> update(Class<TEntity> entityType)
            throws
            ModuleException;

    /**
     * 更新一条记录
     *
     * @param entityType 实体类型
     * @param data       数据
     * @param <TEntity>  实体类型
     */
    <TEntity> IUpdate<TEntity> update(Class<TEntity> entityType,
                                      TEntity data)
            throws
            ModuleException;

    /**
     * 更新一条记录
     *
     * @param entityType   实体类型
     * @param data         数据
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TEntity>    实体类型
     * @param <TDto>       业务模型类型
     */
    <TEntity, TDto> IUpdate<TEntity> update(Class<TEntity> entityType,
                                            TDto data,
                                            Class<TDto> dtoType,
                                            Integer mainTagLevel)
            throws
            ModuleException;

    /**
     * 批量更新记录
     *
     * @param entityType 实体类型
     * @param data       数据集合
     * @param <TEntity>  实体类型
     */
    <TEntity> IUpdate<TEntity> batchUpdate(Class<TEntity> entityType,
                                           Collection<TEntity> data)
            throws
            ModuleException;

    /**
     * 批量更新记录
     *
     * @param entityType   实体类型
     * @param data         数据集合
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TEntity>    实体类型
     * @param <TDto>       业务模型类型
     */
    <TEntity, TDto> IUpdate<TEntity> batchUpdate(Class<TEntity> entityType,
                                                 Collection<TDto> data,
                                                 Class<TDto> dtoType,
                                                 Integer mainTagLevel)
            throws
            ModuleException;

    /**
     * 数据删除对象
     *
     * @param entityType 实体类型
     * @param <TEntity>  实体类型
     */
    <TEntity> IDelete<TEntity> delete(Class<TEntity> entityType)
            throws
            ModuleException;

    /**
     * 删除一条记录
     *
     * @param entityType 实体类型
     * @param data       数据
     * @param <TEntity>  实体类型
     */
    <TEntity> IDelete<TEntity> delete(Class<TEntity> entityType,
                                      TEntity data)
            throws
            ModuleException;

    /**
     * 删除一条记录
     *
     * @param entityType 实体类型
     * @param data       数据
     * @param dtoType    业务模型类型
     * @param <TEntity>  实体类型
     * @param <TDto>     业务模型类型
     */
    <TEntity, TDto> IDelete<TEntity> delete(Class<TEntity> entityType,
                                            TDto data,

                                            Class<TDto> dtoType)
            throws
            ModuleException;

    /**
     * 批量删除记录
     *
     * @param entityType 实体类型
     * @param data       数据集合
     * @param <TEntity>  实体类型
     */
    <TEntity> IDelete<TEntity> batchDelete(Class<TEntity> entityType,
                                           Collection<TEntity> data)
            throws
            ModuleException;

    /**
     * 批量删除记录
     *
     * @param entityType 实体类型
     * @param data       数据
     * @param dtoType    业务模型类型
     * @param <TEntity>  实体类型
     * @param <TDto>     业务模型类型
     */
    <TEntity, TDto> IDelete<TEntity> batchDelete(Class<TEntity> entityType,
                                                 Collection<TDto> data,
                                                 Class<TDto> dtoType)
            throws
            ModuleException;

    /**
     * 开启事务
     *
     * @param handler 执行函数
     * @return a：true：已提交，false：已回滚，b：异常信息
     */
    Tuple2<Boolean, Exception> transaction(IAction0 handler);

    /**
     * 开启事务
     * <p>使用springframework管理事务</p>
     * <p>兼容声明式事务（方法上添加了注解 org.springframework.transaction.annotation.Transactional）</p>
     *
     * @param isolationLevel 事务隔离等级
     * @param handler        执行函数
     * @return a：true：已提交，false：已回滚，b：异常信息
     */
    Tuple2<Boolean, Exception> transaction(TransactionIsolationLevel isolationLevel,
                                           IAction0 handler);

    /**
     * 获取数据库访问对象
     */
    INaiveAdo getAdo()
            throws
            ModuleException;

    /**
     * 获取数据库访问对象
     */
    INaiveAop getAop()
            throws
            ModuleException;

    /**
     * 获取DbFirst 开发模式相关功能
     */
    IDbFirst getDbFirst()
            throws
            ModuleException;

    /**
     * 获取DbFirst 开发模式相关功能
     */
    ICodeFirst getCodeFirst()
            throws
            ModuleException;

    /**
     * 获取数据仓储
     *
     * @param entityType 实体类型
     * @param <TEntity>  数据类型
     * @return 数据仓储
     */
    <TEntity> IBaseRepository<TEntity> getRepository(Class<TEntity> entityType)
            throws
            ModuleException;

    /**
     * 获取数据仓储
     *
     * @param entityType 实体类型
     * @param keyType    主键类型
     * @param <TEntity>  数据类型
     * @param <TKey>     主键类型
     * @return 数据仓储
     */
    <TEntity, TKey> IBaseRepository_Key<TEntity, TKey> getRepository_Key(Class<TEntity> entityType,
                                                                         Class<TKey> keyType)
            throws
            ModuleException;

    /**
     * 获取Mapper
     *
     * @param mapperType  类型
     * @param <TMapper>类型
     */
    <TMapper> TMapper getMapper(Class<TMapper> mapperType);
}
