package project.extension.mybatis.edge.core.provider.standard.curd;

/**
 * 生成条件的源对象所必须包含的接口
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-04-11
 */
public interface ISetSource<T> {
    /**
     * 获取实体类型
     */
    Class<T> getEntityType();

    /**
     * 创建新的条件对象
     *
     * @param fieldName  字段名
     * @param memberType 字段类型
     * @param action     操作
     * @param <TMember>  字段类型
     */
    <TMember> ISet<T, TMember> newSet(String fieldName, Class<TMember> memberType, ISetAction<T, TMember> action);
}
