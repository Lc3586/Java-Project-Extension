package project.extension.openapi.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 接口架构数据原始类型
 *
 * @author LCTR
 * @date 2022-03-21
 */
public enum PrimitiveType {
    /**
     * 32位整型
     */
    @OpenApiDescription("32位整型")
    Integer(0, "Integer"),
    /**
     * 64位整型
     */
    @OpenApiDescription("64位整型")
    Long(1, "Long"),
    /**
     * 单精度浮点数
     */
    @OpenApiDescription("单精度浮点数")
    Float(2, "Float"),
    /**
     * 双精度浮点数
     */
    @OpenApiDescription("双精度浮点数")
    Double(3, "Double"),
    /**
     * 高精度浮点数
     */
    @OpenApiDescription("高精度浮点数")
    Decimal(3, "Decimal"),
    /**
     * 字符串
     */
    @OpenApiDescription("字符串")
    String(4, "String"),
    /**
     * 整型[-128，127]
     */
    @OpenApiDescription("整型[-128，127]")
    Byte(5, "Byte"),
    /**
     * 二进制
     */
    @OpenApiDescription("二进制")
    Binary(6, "Binary"),
    /**
     * 布尔类型
     */
    @OpenApiDescription("布尔类型")
    Boolean(7, "Boolean"),
    /**
     * 日期
     */
    @OpenApiDescription("日期")
    Date(8, "Date"),
    /**
     * 日期时间
     */
    @OpenApiDescription("日期时间")
    DateTime(9, "DateTime"),
    /**
     * 密文
     */
    @OpenApiDescription("密文")
    Password(10, "Password");

    /**
     * @param index 索引
     * @param value 值
     */
    PrimitiveType(int index, String value) {
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
    public static PrimitiveType toEnum(String value) throws IllegalArgumentException {
        return PrimitiveType.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static PrimitiveType toEnum(int index) throws IllegalArgumentException {
        for (PrimitiveType value : PrimitiveType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(java.lang.String.format("指定索引%s无效", index));
    }
}
