package project.extension.mybatis.edge.core.provider.standard.curd;

/**
 * 分组查询对象
 *
 * @param <T>           实体类型
 * @param <TGroupByKey> 分组字段类型
 * @author LCTR
 * @date 2022-04-20
 */
public interface ISelectGrouping<T, TGroupByKey> {
    /**
     * 分组
     *
     * @param fieldName 字段名
     */
    ISelectGrouping<T, TGroupByKey> groupBy(String... fieldName);

    /**
     * 指定条件
     *
     * @param sql sql语句
     */
    ISelectGrouping<T, TGroupByKey> having(String sql);

    /**
     * 指定条件
     */
    ISelectGrouping<T, TGroupByKey> having(IWhereAction<T, IWhereSource<T>> action);

    /**
     * 排序
     *
     * @param sql sql语句
     */
    ISelectGrouping<T, TGroupByKey> orderBy(String sql);

    /**
     * 排序
     */
    ISelectGrouping<T, TGroupByKey> orderBy(IOrderByAction<T, IOrderBySource<T>> action);
}
