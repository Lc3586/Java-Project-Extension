package project.extension.standard.api.response.select;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Arrays;
import java.util.Optional;

/**
 * 下拉选择框选项类型
 *
 * @author LCTR
 * @date 2022-07-04
 */
public enum SelectOptionType {
    /**
     * 文本
     */
    @OpenApiDescription("文本" )
    文本(1,
       "TEXT" ),
    /**
     * 图片
     */
    @OpenApiDescription("图片" )
    图片(2,
       "IMAGE" );

    /**
     * @param index 索引
     * @param value 值
     */
    SelectOptionType(int index,
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
    public static SelectOptionType toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<SelectOptionType> find = Arrays.stream(SelectOptionType.values())
                                                .filter(x -> x.value.equals(value))
                                                .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到符合%s此值的SelectOptionType枚举中" ,
                                                             value));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static SelectOptionType toEnum(int index)
            throws
            IllegalArgumentException {
        for (SelectOptionType value : SelectOptionType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效" ,
                                                         index));
    }
}
