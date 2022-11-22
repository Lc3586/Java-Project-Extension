package project.extension.mybatis.model;

/**
 * 字段更新目标类型
 *
 * @author LCTR
 * @date 2022-04-11
 */
public enum SetterTargetType {
    /**
     * 使用字段名
     */
    FieldName(0, "FieldName"),
    /**
     * 使用值
     */
    Value(1, "Value"),
    /**
     * 使用子表达式
     */
    Expression(2, "Expression");

    /**
     * @param index 索引
     * @param value 值
     */
    SetterTargetType(int index, String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * 索引
     */
    int index;

    /**
     * 值
     */
    String value;

    /**
     * 获取索引
     *
     * @return 索引
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 获取字符串
     *
     * @return 值
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * 获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static SetterTargetType toEnum(String value) throws IllegalArgumentException {
        return SetterTargetType.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static SetterTargetType toEnum(int index) throws IllegalArgumentException {
        for (SetterTargetType value : SetterTargetType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}