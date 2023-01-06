package project.extension.mybatis.edge.core.provider.standard;

import project.extension.mybatis.edge.core.ado.INaiveAdo;

/**
 * 基础构造器
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-30
 */
public interface IBaseDbProvider<T> {
    /**
     * 数据查询对象
     *
     * @param entityType 实体类型
     * @param ado        数据访问对象
     */
    ISelect<T> createSelect(Class<T> entityType,
                            INaiveAdo ado);

    /**
     * 数据插入对象
     *
     * @param entityType 实体类型
     * @param ado        数据访问对象
     */
    IInsert<T> createInsert(Class<T> entityType,
                            INaiveAdo ado);

    /**
     * 数据更新对象
     *
     * @param entityType 实体类型
     * @param ado        数据访问对象
     */
    IUpdate<T> createUpdate(Class<T> entityType,
                            INaiveAdo ado);

    /**
     * 数据删除对象
     *
     * @param entityType 实体类型
     * @param ado        数据访问对象
     */
    IDelete<T> createDelete(Class<T> entityType,
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
