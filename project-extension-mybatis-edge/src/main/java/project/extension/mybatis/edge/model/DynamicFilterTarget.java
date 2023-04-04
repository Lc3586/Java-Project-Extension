package project.extension.mybatis.edge.model;

import lombok.Data;

/**
 * 动态过滤器操作目标对象
 *
 * @author LCTR
 * @date 2022-05-17
 */
@Data
public class DynamicFilterTarget {
    public DynamicFilterTarget() {

    }

    /**
     * @param fieldName 字段名
     */
    public DynamicFilterTarget(String fieldName) {
        this.type = FilterTargetType.FieldName;
        this.fieldName = fieldName;
    }

    /**
     * @param value        值
     * @param justOverload 防止方法重载冲突，固定赋值为null即可
     */
    public DynamicFilterTarget(Object value, Object justOverload) {
        this.type = FilterTargetType.Value;
        this.value = value;
    }

    /**
     * @param expression 子表达式
     */
    public DynamicFilterTarget(DynamicFilterExpression expression) {
        this.type = FilterTargetType.Expression;
        this.expression = expression;
    }

    /**
     * 操作目标类型
     */
    private FilterTargetType type;

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
    private DynamicFilterExpression expression;
}
