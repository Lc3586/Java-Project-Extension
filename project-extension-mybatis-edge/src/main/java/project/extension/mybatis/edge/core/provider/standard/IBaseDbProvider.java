package project.extension.mybatis.edge.core.provider.standard;

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
     * @param entityType        实体类型
     * @param withTransactional 所有操作是否在由springframework管理的事务下运行
     */
    ISelect<T> createSelect(Class<T> entityType,
                            boolean withTransactional);

    /**
     * 数据插入对象
     *
     * @param entityType        实体类型
     * @param withTransactional 所有操作是否在由springframework管理的事务下运行
     */
    IInsert<T> createInsert(Class<T> entityType,
                            boolean withTransactional);

    /**
     * 数据更新对象
     *
     * @param entityType        实体类型
     * @param withTransactional 所有操作是否在由springframework管理的事务下运行
     */
    IUpdate<T> createUpdate(Class<T> entityType,
                            boolean withTransactional);

    /**
     * 数据删除对象
     *
     * @param entityType        实体类型
     * @param withTransactional 所有操作是否在由springframework管理的事务下运行
     */
    IDelete<T> createDelete(Class<T> entityType,
                            boolean withTransactional);

    /**
     * DbFirst 开发模式相关功能
     */
    IDbFirst createDbFirst();

    /**
     * CodeFirst 开发模式相关功能接口类
     */
    ICodeFirst createCodeFirst();
}
