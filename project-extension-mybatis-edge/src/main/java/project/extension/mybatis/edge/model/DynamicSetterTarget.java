package project.extension.mybatis.edge.model;

import lombok.Data;

/**
 * 动态更新器操作目标对象
 *
 * @author LCTR
 * @date 2022-04-14
 */
@Data
public class DynamicSetterTarget {
    public DynamicSetterTarget() {

    }

    /**
     * @param fieldName 字段名
     */
    public DynamicSetterTarget(String fieldName) {
        this.type = SetterTargetType.FieldName;
        this.fieldName = fieldName;
    }

    /**
     * @param value        值
     * @param justOverload 防止方法重载冲突，固定赋值为null即可
     */
    public DynamicSetterTarget(Object value, Object justOverload) {
        this.type = SetterTargetType.Value;
        this.value = value;
    }

    /**
     * @param expression 子表达式
     */
    public DynamicSetterTarget(DynamicSetterExpression expression) {
        this.type = SetterTargetType.Expression;
        this.expression = expression;
    }

    /**
     * 操作目标类型
     */
    private SetterTargetType type;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 值
     */
    private Object value;

    /**
     * 子表达式
     */
    private DynamicSetterExpression expression;
}
