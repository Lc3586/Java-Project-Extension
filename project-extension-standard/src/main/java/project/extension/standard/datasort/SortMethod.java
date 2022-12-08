package project.extension.standard.datasort;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Arrays;
import java.util.Optional;

/**
 * 排序方法
 *
 * @author LCTR
 * @date 2022-12-08
 */
public enum SortMethod {
    /**
     * 置顶
     */
    @OpenApiDescription("置顶")
    TOP(0,
        "TOP"),
    /**
     * 上移
     */
    @OpenApiDescription("上移")
    UP(1,
       "UP"),
    /**
     * 下移
     */
    @OpenApiDescription("下移")
    DOWN(2,
         "DOWN"),
    /**
     * 置底
     */
    @OpenApiDescription("置底")
    LOW(3,
        "LOW");

    /**
     * @param index 索引
     * @param value 值
     */
    SortMethod(int index,
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
    public static SortMethod toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<SortMethod> find = Arrays.stream(SortMethod.values())
                                          .filter(x -> x.value.equals(value))
                                          .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到值为%s的%s枚举",
                                                             value,
                                                             SortMethod.class.getName()));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static SortMethod toEnum(int index)
            throws
            IllegalArgumentException {
        for (SortMethod value : SortMethod.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}