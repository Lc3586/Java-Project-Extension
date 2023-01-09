package project.extension.mybatis.edge.dbContext.repository;

import project.extension.mybatis.edge.core.provider.standard.IDelete;
import project.extension.mybatis.edge.core.provider.standard.IInsert;
import project.extension.mybatis.edge.core.provider.standard.ISelect;
import project.extension.mybatis.edge.core.provider.standard.IUpdate;

import java.util.Collection;

/**
 * 数据仓储基础泛型接口
 *
 * @param <TEntity> 实体类型
 * @author LCTR
 * @date 2022-03-28
 */
public interface IBaseRepository<TEntity>
        extends IBaseRepositoryBase {
    /**
     * 数据查询对象
     */
    ISelect<TEntity> select();

    /**
     * 插入一条记录
     *
     * @param data 数据
     */
    void insert(TEntity data)
            throws
            Exception;

    /**
     * 插入一条记录
     *
     * @param data         数据
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TDto>       业务模型类型
     */
    <TDto> void insert(TDto data,
                       Class<TDto> dtoType,
                       Integer mainTagLevel)
            throws
            Exception;

    /**
     * 批量插入记录
     *
     * @param data 数据集合
     */
    void batchInsert(Collection<TEntity> data)
            throws
            Exception;

    /**
     * 批量插入记录
     *
     * @param data         数据集合
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TDto>       业务模型类型
     */
    <TDto> void batchInsert(Collection<TDto> data,
                            Class<TDto> dtoType,
                            Integer mainTagLevel)
            throws
            Exception;

    /**
     * 数据插入对象
     */
    IInsert<TEntity> insertDiy();

    /**
     * 更新一条记录
     *
     * @param data 数据
     */
    void update(TEntity data)
            throws
            Exception;

    /**
     * 更新一条记录
     *
     * @param data         数据
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TDto>       业务模型类型
     */
    <TDto> void update(TDto data,
                       Class<TDto> dtoType,
                       Integer mainTagLevel)
            throws
            Exception;

    /**
     * 批量更新记录
     *
     * @param data 数据集合
     */
    void batchUpdate(Collection<TEntity> data)
            throws
            Exception;

    /**
     * 批量更新记录
     *
     * @param data         数据集合
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TDto>       业务模型类型
     */
    <TDto> void batchUpdate(Collection<TDto> data,
                            Class<TDto> dtoType,
                            Integer mainTagLevel)
            throws
            Exception;

    /**
     * 数据更新对象
     */
    IUpdate<TEntity> updateDiy();

    /**
     * 删除一条记录
     *
     * @param data 数据
     */
    void delete(TEntity data)
            throws
            Exception;

    /**
     * 删除一条记录
     *
     * @param data    数据
     * @param dtoType 业务模型类型
     * @param <TDto>  业务模型类型
     */
    <TDto> void delete(TDto data,
                       Class<TDto> dtoType)
            throws
            Exception;

    /**
     * 批量删除记录
     *
     * @param data 数据集合
     */
    void batchDelete(Collection<TEntity> data)
            throws
            Exception;

    /**
     * 批量删除记录
     *
     * @param data    数据
     * @param dtoType 业务模型类型
     * @param <TDto>  业务模型类型
     */
    <TDto> void batchDelete(Collection<TDto> data,
                            Class<TDto> dtoType)
            throws
            Exception;

    /**
     * 数据删除对象
     */
    IDelete<TEntity> deleteDiy();
}
