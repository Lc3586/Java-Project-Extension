package project.extension.mybatis.edge.core.provider.standard;

/**
 * 排序操作
 *
 * @author LCTR
 * @date 2022-04-04
 */
public interface IOrderByAction<T, TSource> {
    /**
     * 执行
     *
     * @param orderBy 排序
     */
    IOrderBy<T, TSource> invoke(IOrderBy<T, TSource> orderBy);
}
