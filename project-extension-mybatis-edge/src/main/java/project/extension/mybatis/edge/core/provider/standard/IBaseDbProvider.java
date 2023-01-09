package project.extension.mybatis.edge.core.provider.standard;

import project.extension.mybatis.edge.core.ado.INaiveAdo;

/**
 * 基础构造器
 *
 * @author LCTR
 * @date 2022-03-30
 */
public interface IBaseDbProvider {
    /**
     * 数据查询对象
     *
     * @param entityType 实体类型
     * @param ado        数据访问对象
     * @param <TEntity>  实体类型
     */
    <TEntity> ISelect<TEntity> createSelect(Class<TEntity> entityType,
                                            INaiveAdo ado);

    /**
     * 数据插入对象
     *
     * @param entityType 实体类型
     * @param ado        数据访问对象
     * @param <TEntity>  实体类型
     */
    <TEntity> IInsert<TEntity> createInsert(Class<TEntity> entityType,
                                            INaiveAdo ado);

    /**
     * 数据更新对象
     *
     * @param entityType 实体类型
     * @param ado        数据访问对象
     * @param <TEntity>  实体类型
     */
    <TEntity> IUpdate<TEntity> createUpdate(Class<TEntity> entityType,
                                            INaiveAdo ado);

    /**
     * 数据删除对象
     *
     * @param entityType 实体类型
     * @param ado        数据访问对象
     * @param <TEntity>  实体类型
     */
    <TEntity> IDelete<TEntity> createDelete(Class<TEntity> entityType,
                                            INaiveAdo ado);

    /**
     * DbFirst 开发模式相关功能
     *
     * @param ado 数据访问对象
     */
    IDbFirst createDbFirst(INaiveAdo ado);

    /**
     * CodeFirst 开发模式相关功能接口类
     *
     * @param ado 数据访问对象
     */
    ICodeFirst createCodeFirst(INaiveAdo ado);
}
