package project.extension.mybatis.edge.dbContext.repository;

import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.provider.standard.IDelete;
import project.extension.mybatis.edge.core.provider.standard.IInsert;
import project.extension.mybatis.edge.core.provider.standard.ISelect;
import project.extension.mybatis.edge.core.provider.standard.IUpdate;

import java.util.Collection;

/**
 * 数据仓储基础接口
 *
 * @param <T> 数据类型
 * @author LCTR
 * @date 2022-03-28
 */
public interface IBaseRepository<T> {
    /**
     * 获取当前配置信息
     */
    BaseConfig getConfig();

    /**
     * 所有操作是否在由springframework管理的事务下运行
     * <p>如果是 需要在方法上添加此注解 org.springframework.transaction.annotation.Transactional</p>
     *
     * @param withTransactional 所有操作是否在由springframework管理的事务下运行
     */
    IBaseRepository<T> withTransactional(boolean withTransactional);

    /**
     * 数据查询对象
     */
    ISelect<T> select();

    /**
     * 插入一条记录
     *
     * @param data 数据
     */
    <T2> void insert(T2 data)
            throws
            Exception;

    /**
     * 插入一条记录
     *
     * @param data         数据
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <T2>         业务模型类型
     */
    <T2> void insert(T2 data,
                     Class<T2> dtoType,
                     Integer mainTagLevel)
            throws
            Exception;

    /**
     * 批量插入记录
     *
     * @param data 数据集合
     */
    <T2> void batchInsert(Collection<T2> data)
            throws
            Exception;

    /**
     * 批量插入记录
     *
     * @param data         数据集合
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <T2>         业务模型类型
     */
    <T2> void batchInsert(Collection<T2> data,
                          Class<T2> dtoType,
                          Integer mainTagLevel)
            throws
            Exception;

    /**
     * 数据插入对象
     */
    IInsert<T> insertDiy();

    /**
     * 更新一条记录
     *
     * @param data 数据
     */
    <T2> void update(T2 data)
            throws
            Exception;

    /**
     * 更新一条记录
     *
     * @param data         数据
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <T2>         业务模型类型
     */
    <T2> void update(T2 data,
                     Class<T2> dtoType,
                     Integer mainTagLevel)
            throws
            Exception;

    /**
     * 批量更新记录
     *
     * @param data 数据集合
     */
    <T2> void batchUpdate(Collection<T2> data)
            throws
            Exception;

    /**
     * 批量更新记录
     *
     * @param data         数据集合
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <T2>         业务模型类型
     */
    <T2> void batchUpdate(Collection<T2> data,
                          Class<T2> dtoType,
                          Integer mainTagLevel)
            throws
            Exception;

    /**
     * 数据更新对象
     */
    IUpdate<T> updateDiy();

    /**
     * 删除一条记录
     *
     * @param data 数据
     */
    <T2> void delete(T2 data)
            throws
            Exception;

    /**
     * 删除一条记录
     *
     * @param data    数据
     * @param dtoType 业务模型类型
     * @param <T2>    业务模型类型
     */
    <T2> void delete(T2 data,
                     Class<T2> dtoType)
            throws
            Exception;

    /**
     * 批量删除记录
     *
     * @param data 数据集合
     */
    <T2> void batchDelete(Collection<T2> data)
            throws
            Exception;

    /**
     * 批量删除记录
     *
     * @param data    数据
     * @param dtoType 业务模型类型
     * @param <T2>    业务模型类型
     */
    <T2> void batchDelete(Collection<T> data,
                          Class<T2> dtoType)
            throws
            Exception;

    /**
     * 数据删除对象
     */
    IDelete<T> deleteDiy();
}
