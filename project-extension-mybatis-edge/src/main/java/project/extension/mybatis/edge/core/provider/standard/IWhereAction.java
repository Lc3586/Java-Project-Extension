package project.extension.mybatis.edge.core.provider.standard;

/**
 * 条件操作
 *
 * @author LCTR
 * @date 2022-04-04
 */
public interface IWhereAction<T, TSource> {
    /**
     * 执行
     *
     * @param where 条件
     */
    IWhere<T, TSource> invoke(IWhere<T, TSource> where);
}
