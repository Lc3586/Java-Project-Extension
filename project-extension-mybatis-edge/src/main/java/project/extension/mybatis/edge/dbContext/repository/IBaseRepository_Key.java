package project.extension.mybatis.edge.dbContext.repository;

import project.extension.mybatis.edge.model.NullResultException;

import java.util.Collection;

/**
 * 数据仓储基础泛型接口
 *
 * @param <TEntity> 实体类型
 * @param <TKey>    主键类型
 * @author LCTR
 * @date 2022-03-28
 */
public interface IBaseRepository_Key<TEntity, TKey>
        extends IBaseRepository<TEntity> {
    /**
     * 获取数据
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param id 主键ID
     */
    TEntity getById(TKey id)
            throws
            Exception;

    /**
     * 获取数据并进行非空检查
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     * <p>错误信息默认值：数据不存在或已被移除</p>
     *
     * @param id 主键ID
     * @throws NullResultException 数据为空
     */
    TEntity getByIdAndCheckNull(TKey id)
            throws
            Exception;

    /**
     * 获取数据并进行非空检查
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param id               主键ID
     * @param nullErrorMessage 数据为空时的错误信息（默认值 数据不存在或已被移除）
     * @throws NullResultException 数据为空
     */
    TEntity getByIdAndCheckNull(TKey id,
                                String nullErrorMessage)
            throws
            Exception;

    /**
     * 获取数据
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param id      主键ID
     * @param dtoType 业务模型类型
     * @param <TDto>  业务模型类型
     */
    <TDto> TDto getById(TKey id,
                        Class<TDto> dtoType)
            throws
            Exception;

    /**
     * 获取数据
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param id           主键ID
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TDto>       业务模型类型
     */
    <TDto> TDto getById(TKey id,
                        Class<TDto> dtoType,
                        int mainTagLevel)
            throws
            Exception;

    /**
     * 获取数据并进行非空检查
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     * <p>错误信息默认值：数据不存在或已被移除</p>
     *
     * @param id      主键ID
     * @param dtoType 业务模型类型
     * @param <TDto>  业务模型类型
     * @throws NullResultException 数据为空
     */
    <TDto> TDto getByIdAndCheckNull(TKey id,
                                    Class<TDto> dtoType)
            throws
            Exception;

    /**
     * 获取数据并进行非空检查
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     * <p>错误信息默认值：数据不存在或已被移除</p>
     *
     * @param id           主键ID
     * @param dtoType      业务模型类型
     * @param mainTagLevel 主标签等级
     * @param <TDto>       业务模型类型
     * @throws NullResultException 数据为空
     */
    <TDto> TDto getByIdAndCheckNull(TKey id,
                                    Class<TDto> dtoType,
                                    int mainTagLevel)
            throws
            Exception;

    /**
     * 获取数据并进行非空检查
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param id               主键ID
     * @param dtoType          业务模型类型
     * @param nullErrorMessage 数据为空时的错误信息（默认值 数据不存在或已被移除）
     * @param <TDto>           业务模型类型
     * @throws NullResultException 数据为空
     */
    <TDto> TDto getByIdAndCheckNull(TKey id,
                                    Class<TDto> dtoType,
                                    String nullErrorMessage)
            throws
            Exception;

    /**
     * 获取数据并进行非空检查
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param id               主键ID
     * @param dtoType          业务模型类型
     * @param mainTagLevel     主标签等级
     * @param nullErrorMessage 数据为空时的错误信息（默认值 数据不存在或已被移除）
     * @param <TDto>           业务模型类型
     * @throws NullResultException 数据为空
     */
    <TDto> TDto getByIdAndCheckNull(TKey id,
                                    Class<TDto> dtoType,
                                    int mainTagLevel,
                                    String nullErrorMessage)
            throws
            Exception;

    /**
     * 根据 ID 删除
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param id 主键ID
     */
    void deleteById(TKey id)
            throws
            Exception;

    /**
     * 根据 ID集合 删除
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param ids 主键ID集合
     */
    void deleteByIds(Collection<TKey> ids)
            throws
            Exception;
}
