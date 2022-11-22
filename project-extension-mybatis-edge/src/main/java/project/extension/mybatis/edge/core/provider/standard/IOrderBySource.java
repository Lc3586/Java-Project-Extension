package project.extension.mybatis.edge.core.provider.standard;

/**
 * 生成排序的源对象所必须包含的接口
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-04-11
 */
public interface IOrderBySource<T> {
    /**
     * 获取实体类型
     */
    Class<T> getEntityType();

    /**
     * 创建新的条件对象
     */
    IOrderBy<T, IOrderBySource<T>> newOrderBy(IOrderByAction<T, IOrderBySource<T>> action);
}
