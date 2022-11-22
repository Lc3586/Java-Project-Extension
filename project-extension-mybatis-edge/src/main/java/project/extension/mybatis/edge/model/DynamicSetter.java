package project.extension.mybatis.edge.model;

/**
 * 动态更新器
 *
 * @author LCTR
 * @date 2022-04-11
 */
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

    /**
     * 要操作的列
     */
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 成员类型
     */
    public Class<?> getMemberType() {
        return memberType;
    }

    public void setMemberType(Class<?> memberType) {
        this.memberType = memberType;
    }

    /**
     * 表达式
     */
    public DynamicSetterExpression getExpression() {
        return expression;
    }

    public void setExpression(DynamicSetterExpression expression) {
        this.expression = expression;
    }
}
