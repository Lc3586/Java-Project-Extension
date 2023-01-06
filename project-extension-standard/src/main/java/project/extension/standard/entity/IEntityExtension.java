package project.extension.standard.entity;

import project.extension.standard.exception.ModuleException;

import java.util.Collection;

/**
 * 实体类拓展方法
 *
 * @author LCTR
 * @date 2022-12-08
 */
public interface IEntityExtension {
    /**
     * 生成id
     *
     * @param format 格式
     * @return id
     */
    String newStringId(String format);

    /**
     * 生成id
     *
     * @return id
     */
    String newStringId();

    /**
     * 生成id
     *
     * @return id
     */
    Long newLongId();

    /**
     * 初始化实体
     *
     * @param entity 实体
     * @param <T>    实体类型
     * @return 实体
     */
    <T> T initialization(T entity)
            throws
            ModuleException;

    /**
     * 初始化实体
     *
     * @param entities 实体集合
     * @param <T>      实体类型
     * @return 实体集合
     */
    <T> Collection<T> initialization(Collection<T> entities)
            throws
            ModuleException;

    /**
     * 修改实体
     *
     * @param entity 实体
     * @param <T>    实体类型
     * @return 实体
     */
    <T> T modify(T entity)
            throws
            ModuleException;

    /**
     * 修改实体
     *
     * @param entities 实体集合
     * @param <T>      实体类型
     * @return 实体集合
     */
    <T> Collection<T> modify(Collection<T> entities)
            throws
            ModuleException;
}
