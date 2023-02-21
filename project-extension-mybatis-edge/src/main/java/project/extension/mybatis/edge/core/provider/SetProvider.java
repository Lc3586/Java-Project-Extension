package project.extension.mybatis.edge.core.provider;

import project.extension.mybatis.edge.core.provider.standard.curd.ISet;
import project.extension.mybatis.edge.core.provider.standard.curd.ISetAction;
import project.extension.mybatis.edge.core.provider.standard.curd.ISetSource;
import project.extension.mybatis.edge.model.*;

/**
 * 更新字段构造器
 *
 * @param <T>       实体类型
 * @param <TMember> 字段类型
 * @author LCTR
 * @date 2022-04-11
 */
public class SetProvider<T, TMember> implements ISet<T, TMember> {
    private final ISetSource<T> source;
    private final Class<TMember> memberType;
    private final DynamicSetter setter;

    public SetProvider(ISetSource<T> source, String fieldName, Class<TMember> memberType) {
        this.source = source;
        this.memberType = memberType;
        this.setter = new DynamicSetter(fieldName, memberType);
    }

    /**
     * 检查表达式是否已开始，如果未开始则设置默认值，也就是默认使用字段名为表达式的起始对象
     */
    private DynamicSetter checkStartedStartAndSetUpDefaultValue() {
        if (setter.getExpression() == null)
            setter.setExpression(new DynamicSetterExpression(new DynamicSetterTarget(setter.getFieldName())));
        return setter;
    }

    @Override
    public ISet<T, TMember> start(String fieldName) {
        setter.setExpression(new DynamicSetterExpression(new DynamicSetterTarget(fieldName)));
        return this;
    }

    @Override
    public ISet<T, TMember> start(ISetAction<T, TMember> action) {
        setter.setExpression(new DynamicSetterExpression(new DynamicSetterTarget(source.newSet(setter.getFieldName(), memberType, action).getSetter().getExpression())));
        return this;
    }

    @Override
    public ISet<T, TMember> startWithValue(Object value) {
        setter.setExpression(new DynamicSetterExpression(new DynamicSetterTarget(value, null)));
        return this;
    }

    @Override
    public ISet<T, TMember> operation(OperationSymbol operationSymbol, String fieldName) {
        checkStartedStartAndSetUpDefaultValue().getExpression().getOperations().add(new DynamicSetterOperation(operationSymbol, new DynamicSetterTarget(fieldName)));
        return this;
    }

    @Override
    public ISet<T, TMember> operation(OperationSymbol operationSymbol, ISetAction<T, TMember> action) {
        checkStartedStartAndSetUpDefaultValue().getExpression().getOperations().add(new DynamicSetterOperation(operationSymbol, new DynamicSetterTarget(source.newSet(setter.getFieldName(), memberType, action).getSetter().getExpression())));
        return this;
    }

    @Override
    public ISet<T, TMember> operationWithValue(OperationSymbol operationSymbol, Object value) {
        checkStartedStartAndSetUpDefaultValue().getExpression().getOperations().add(new DynamicSetterOperation(operationSymbol, new DynamicSetterTarget(value, null)));
        return this;
    }

    @Override
    public DynamicSetter getSetter() {
        return setter;
    }
}
