package project.extension.mybatis.core.provider.standard;

/**
 * 更新字段操作
 *
 * @param <T>       实体类型
 * @param <TMember> 字段类型
 * @author LCTR
 * @date 2022-04-11
 */
public interface ISetAction<T, TMember> {
    /**
     * 执行
     *
     * @param set 操作
     */
    ISet<T, TMember> invoke(ISet<T, TMember> set);
}
