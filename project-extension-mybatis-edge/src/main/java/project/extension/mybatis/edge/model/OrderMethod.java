package project.extension.mybatis.edge.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 排序方法
 *
 * @author LCTR
 * @date 2022-03-24
 */
public enum OrderMethod {
    /**
     * 升序
     */
    @OpenApiDescription("升序")
    ASC(0, "ASC"),
    /**
     * 降序
     */
    @OpenApiDescription("降序")
    DESC(1, "DESC");

    /**
     * @param index 索引
     * @param value 值
     */
    OrderMethod(int index, String value) {
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
    public static OrderMethod toEnum(String value) throws IllegalArgumentException {
        return OrderMethod.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static OrderMethod toEnum(int index) throws IllegalArgumentException {
        for (OrderMethod value : OrderMethod.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}
