package project.extension.openapi.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 示例类型
 *
 * @author LCTR
 * @date 2022-05-13
 */
public enum ExampleType {
    /**
     * Json对象
     */
    @OpenApiDescription("Json对象")
    Json(0,
         "Json"),
    /**
     * Json对象附加注释
     */
    @OpenApiDescription("Json对象附加注释")
    JsonWithComments(1,
                     "JsonWithComments"),
    /**
     * Ts类
     */
    @OpenApiDescription("Ts类")
    TsClass(2,
            "TsClass");

    /**
     * @param index 索引
     * @param value 值
     */
    ExampleType(int index,
                String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * 索引
     */
    final int index;

    /**
     * 值
     */
    final String value;

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
    public static ExampleType toEnum(String value)
            throws
            IllegalArgumentException {
        return ExampleType.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static ExampleType toEnum(int index)
            throws
            IllegalArgumentException {
        for (ExampleType value : ExampleType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}