package project.extension.mybatis.edge.core.provider.standard;

import project.extension.mybatis.edge.model.DynamicSetter;
import project.extension.mybatis.edge.model.OperationSymbol;

/**
 * 更新字段对象
 *
 * @param <T>       实体类型
 * @param <TMember> 字段类型
 * @author LCTR
 * @date 2022-04-11
 */
public interface ISet<T, TMember> {
    /**
     * 起始
     *
     * @param fieldName 字段名
     */
    ISet<T, TMember> start(String fieldName);

    /**
     * 起始
     *
     * @param action 操作
     */
    ISet<T, TMember> start(ISetAction<T, TMember> action);

    /**
     * 起始
     *
     * @param value 值
     */
    ISet<T, TMember> startWithValue(Object value);

    /**
     * 操作
     *
     * @param operationSymbol 操作符号
     * @param fieldName       右字段名
     */
    ISet<T, TMember> operation(OperationSymbol operationSymbol, String fieldName);

    /**
     * 追加操作
     *
     * @param operationSymbol 操作符号
     * @param action          操作
     */
    ISet<T, TMember> operation(OperationSymbol operationSymbol, ISetAction<T, TMember> action);

    /**
     * 追加操作
     *
     * @param operationSymbol 操作符号
     * @param value           值
     */
    ISet<T, TMember> operationWithValue(OperationSymbol operationSymbol, Object value);

    /**
     * 获取更新器对象
     *
     * @return 更新器对象
     */
    DynamicSetter getSetter();
}
