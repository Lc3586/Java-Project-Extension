package project.extension.mybatis.core.provider.standard;

import project.extension.mybatis.model.AdvancedOrder;
import project.extension.mybatis.model.DynamicOrder;

import java.util.Collection;

/**
 * 排序对象
 *
 * @param <T>       实体类型
 * @param <TSource> 源对象类型
 * @author LCTR
 * @date 2022-04-04
 */
public interface IOrderBy<T, TSource> {
    /**
     * 升序排序
     *
     * @param fieldName 字段名
     */
    IOrderBy<T, TSource> orderBy(String fieldName);

    /**
     * 降序排序
     *
     * @param fieldName 字段名
     */
    IOrderBy<T, TSource> orderByDescending(String fieldName);

    /**
     * 追加升序排序
     *
     * @param fieldName 字段名
     */
    IOrderBy<T, TSource> thenOrderBy(String fieldName);

    /**
     * 追加降序排序
     *
     * @param fieldName 字段名
     */
    IOrderBy<T, TSource> thenOrderByDescending(String fieldName);

    /**
     * 排序
     *
     * @param order 动态排序条件
     */
    IOrderBy<T, TSource> orderBy(DynamicOrder order);

    /**
     * 排序
     *
     * @param order 高级排序条件
     */
    IOrderBy<T, TSource> orderBy(AdvancedOrder order);

    /**
     * 排序
     *
     * @param orders 高级排序条件
     */
    IOrderBy<T, TSource> orderBy(Collection<AdvancedOrder> orders);

    /**
     * 排序
     *
     * @param order 动态排序条件
     * @param <T2>  另一个表的实体类型
     */
    <T2, TSource2> IOrderBy<T, TSource> orderByOther(IOrderBy<T2, TSource2> order) throws Exception;

    /**
     * 指定别名
     *
     * @param alias 别名
     */
    IOrderBy<T, TSource> setAlias(String alias);

    /**
     * 是否设置了别名
     */
    boolean hasAlias();

    /**
     * 获取动态排序
     *
     * @return 动态排序
     */
    DynamicOrder getDynamicOrder();
}
