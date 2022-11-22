package project.extension.mybatis.core.mapper;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Select;
import project.extension.mybatis.core.driver.NaiveMixedLanguageDriver;
import project.extension.mybatis.model.DynamicMethod;
import project.extension.mybatis.model.DynamicSqlSetting;

import java.util.Collection;
import java.util.List;

/**
 * 初级映射器基类接口类
 *
 * @author LCTR
 * @date 2022-03-29
 * @deprecated 请使用RepositoryProvider来操作数据库
 */
@Deprecated
public interface INaiveBaseMapper
        extends IDynamicMapper {
    /**
     * 自定义条件获取记录集合
     *
     * @param setting            设置
     * @param executorParameters 查询参数对象
     */
    @Select(DynamicMethod.BaseQueryList)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TResult> List<TResult> list(DynamicSqlSetting<TEntity> setting,
                                          Object executorParameters);

    /**
     * 自定义条件获取一条记录
     *
     * @param setting            设置
     * @param executorParameters 查询参数对象
     */
    @Select(DynamicMethod.BaseQuerySingle)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TResult> TResult one(DynamicSqlSetting<TEntity> setting,
                                   Object executorParameters);

    /**
     * 获取数据
     *
     * @param setting 设置
     * @param id      主键ID
     */
    @Select(DynamicMethod.BaseSingleByKey)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TKey, TResult> TResult getById(DynamicSqlSetting<TEntity> setting,
                                             TKey id);

    /**
     * 插入一条记录
     *
     * @param setting 设置
     * @param data    数据
     */
    @Select(DynamicMethod.BaseInsert)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TData> int insert(DynamicSqlSetting<TEntity> setting,
                                TData data);

    /**
     * 批量插入记录
     *
     * @param setting 设置
     * @param data    数据集合
     */
    @Select(DynamicMethod.BaseBatchInsert)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TData> int batchInsert(DynamicSqlSetting<TEntity> setting,
                                     Collection<TData> data);

    /**
     * 更新一条记录
     *
     * @param setting 设置
     * @param data    数据
     */
    @Select(DynamicMethod.BaseUpdate)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TData> int update(DynamicSqlSetting<TEntity> setting,
                                TData data);

    /**
     * 批量更新记录
     *
     * @param setting 设置
     * @param data    数据集合
     */
    @Select(DynamicMethod.BaseBatchUpdate)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TData> int batchUpdate(DynamicSqlSetting<TEntity> setting,
                                     Collection<TData> data);

    /**
     * 自定义条件更新
     *
     * @param setting            设置
     * @param executorParameters 查询参数对象
     */
    @Select(DynamicMethod.BaseQueryUpdate)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity> int whereUpdate(DynamicSqlSetting<TEntity> setting,
                              Object executorParameters);

    /**
     * 根据 ID 删除
     *
     * @param setting 设置
     * @param id      主键ID
     */
    @Select(DynamicMethod.BaseDeleteByKey)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TKey> int deleteById(DynamicSqlSetting<TEntity> setting,
                                   TKey id);

    /**
     * 根据 ID集合 删除
     *
     * @param setting 设置
     * @param ids     主键ID集合
     */
    @Select(DynamicMethod.BaseDeleteByKeys)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TKey> int deleteByIds(DynamicSqlSetting<TEntity> setting,
                                    Collection<TKey> ids);

    /**
     * 删除一条记录
     *
     * @param setting 设置
     * @param data    数据
     */
    @Select(DynamicMethod.BaseDelete)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TData> int delete(DynamicSqlSetting<TEntity> setting,
                                TData data);

    /**
     * 批量删除记录
     *
     * @param setting 设置
     * @param data    数据集合
     */
    @Select(DynamicMethod.BaseBatchDelete)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity, TData> int batchDelete(DynamicSqlSetting<TEntity> setting,
                                     Collection<TData> data);

    /**
     * 自定义条件删除
     *
     * @param setting            设置
     * @param executorParameters 查询参数对象
     */
    @Select(DynamicMethod.BaseQueryDelete)
    @Lang(NaiveMixedLanguageDriver.class)
    <TEntity> int whereDelete(DynamicSqlSetting<TEntity> setting,
                              Object executorParameters);
}
