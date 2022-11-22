package project.extension.mybatis.core.provider.standard;

/**
 * 生成条件的源对象所必须包含的接口
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-04-11
 */
public interface IWhereSource<T> {
    /**
     * 获取实体类型
     */
    Class<T> getEntityType();

    /**
     * 创建新的条件对象
     */
    IWhere<T, IWhereSource<T>> newWhere(IWhereAction<T, IWhereSource<T>> action);
}
