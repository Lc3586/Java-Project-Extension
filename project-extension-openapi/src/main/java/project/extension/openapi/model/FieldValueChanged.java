package project.extension.openapi.model;

/**
 * 字段值更改信息
 *
 * @author LCTR
 * @date 2022-03-18
 */
public class FieldValueChanged {
    public FieldValueChanged() {

    }

    public FieldValueChanged(String name, String description, Object formerValue, Object currentValue, Class<?> declaringClass) {
        this.name = name;
        this.description = description;
        this.formerValue = formerValue;
        this.currentValue = currentValue;
        this.declaringClass = declaringClass;
    }

    private String name;

    private String description;

    private Object formerValue;

    private Object currentValue;

    private Class<?> declaringClass;

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 说明
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 以前的值
     */
    public Object getFormerValue() {
        return formerValue;
    }

    public void setFormerValue(Object formerValue) {
        this.formerValue = formerValue;
    }

    /**
     * 现在的值
     */
    public Object getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * 定义此字段的类
     */
    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(Class<?> declaringClass) {
        this.declaringClass = declaringClass;
    }
}
