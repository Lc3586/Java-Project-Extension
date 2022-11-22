package project.extension.mybatis.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 动态过滤条件分组关系类型
 *
 * @author LCTR
 * @date 2022-03-24
 */
public enum FilterGroupRelation {
    /**
     * 并且
     */
    @OpenApiDescription("并且")
    AND(0, "AND"),
    /**
     * 或
     */
    @OpenApiDescription("或")
    OR(1, "OR");

    /**
     * @param index 索引
     * @param value 值
     */
    FilterGroupRelation(int index, String value) {
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
    public static FilterGroupRelation toEnum(String value) throws IllegalArgumentException {
        return FilterGroupRelation.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static FilterGroupRelation toEnum(int index) throws IllegalArgumentException {
        for (FilterGroupRelation value : FilterGroupRelation.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}
