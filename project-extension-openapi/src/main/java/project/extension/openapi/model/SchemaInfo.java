package project.extension.openapi.model;

/**
 * 架构信息
 *
 * @author LCTR
 * @date 2022-05-11
 */
public class SchemaInfo {
    public SchemaInfo() {

    }

    public SchemaInfo(String type,
                      String format,
                      boolean specialValue,
                      String value) {
        this.setType(type);
        this.setFormat(format);
        this.setSpecialValue(specialValue);
        this.setValue(value);
    }

    /**
     * 类型
     */
    private String type;

    /**
     * 格式化
     */
    private String format;

    /**
     * 是否指定默认值
     */
    private boolean specialValue;

    /**
     * 默认值
     */
    private String value;

    /**
     * 类型
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 格式化
     */
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 是否指定默认值
     */
    public boolean isSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(boolean specialValue) {
        this.specialValue = specialValue;
    }

    /**
     * 默认值
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
