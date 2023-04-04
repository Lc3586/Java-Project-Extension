package project.extension.mybatis.edge.model;

import lombok.Data;

/**
 * 动态更新器
 *
 * @author LCTR
 * @date 2022-04-11
 */
@Data
public class DynamicSetter {
    public DynamicSetter() {

    }

    /**
     * @param fieldName 字段名
     */
    public DynamicSetter(String fieldName, Class<?> memberType) {
        this.fieldName = fieldName;
        this.memberType = memberType;
    }

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 成员类型
     */
    private Class<?> memberType;

    /**
     * 表达式
     */
    private DynamicSetterExpression expression;
}
