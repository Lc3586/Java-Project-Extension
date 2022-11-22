package project.extension.openapi.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 接口架构数据类型
 *
 * @author LCTR
 * @date 2022-03-21
 */
public enum AnyType {
    /**
     * 原始类型
     */
    @OpenApiDescription("原始类型")
    Primitive(0, "Primitive"),
    /**
     * 无
     */
    @OpenApiDescription("无")
    Null(1, "Null"),
    /**
     * 集合
     */
    @OpenApiDescription("集合")
    Array(2, "Array"),
    /**
     * 对象
     */
    @OpenApiDescription("对象")
    Object(3, "Object");

    /**
     * @param index 索引
     * @param value 值
     */
    AnyType(int index, String value) {
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
    public static AnyType toEnum(String value) throws IllegalArgumentException {
        return AnyType.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static AnyType toEnum(int index) throws IllegalArgumentException {
        for (AnyType value : AnyType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}