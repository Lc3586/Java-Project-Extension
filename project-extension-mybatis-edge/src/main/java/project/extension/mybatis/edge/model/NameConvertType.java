package project.extension.mybatis.edge.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 实体类表名/列名命名规则
 *
 * @author LCTR
 * @date 2022-05-27
 */
public enum NameConvertType {
    /**
     * 不进行任何处理
     */
    @OpenApiDescription("不进行任何处理")
    None(0,
         "None"),
    /**
     * 将帕斯卡命名字符串转换为骆驼命名字符串（BigApple -> bigApple）
     */
    @OpenApiDescription("将帕斯卡命名字符串转换为骆驼命名字符串（BigApple -> bigApple）")
    PascalCaseToCamelCase(1,
                          "PascalCaseToCamelCase"),
    /**
     * 将骆驼命名字符串转换为帕斯卡命名字符串（bigApple -> BigApple）
     */
    @OpenApiDescription("将骆驼命名字符串转换为帕斯卡命名字符串（bigApple -> BigApple）")
    CamelCaseToPascalCase(2,
                          "CamelCaseToPascalCase"),
    /**
     * 将帕斯卡命名字符串转换为下划线分隔字符串（BigApple -> Big_Apple）
     */
    @OpenApiDescription("将帕斯卡命名字符串转换为下划线分隔字符串（BigApple -> Big_Apple）")
    PascalCaseToUnderscore(3,
                           "PascalCaseToUnderscore"),
    /**
     * 将帕斯卡命名字符串转换为下划线分隔字符串，且转换为全大写（BigApple -> BIG_APPLE）
     */
    @OpenApiDescription("将帕斯卡命名字符串转换为下划线分隔字符串，且转换为全大写（BigApple -> BIG_APPLE）")
    PascalCaseToUnderscoreWithUpper(4,
                                    "PascalCaseToUnderscoreWithUpper"),
    /**
     * 将帕斯卡命名字符串转换为下划线分隔字符串，且转换为全小写（BigApple -> big_apple）
     */
    @OpenApiDescription("将帕斯卡命名字符串转换为下划线分隔字符串，且转换为全小写（BigApple -> big_apple）")
    PascalCaseToUnderscoreWithLower(5,
                                    "PascalCaseToUnderscoreWithLower"),
    /**
     * 将字符串转换为大写（BigApple -> BIGAPPLE）
     */
    @OpenApiDescription("将字符串转换为大写（BigApple -> BIGAPPLE）")
    ToUpper(6,
            "ToUpper"),
    /**
     * 将字符串转换为小写（BigApple -> bigapple）
     */
    @OpenApiDescription("将字符串转换为小写（BigApple -> bigapple）")
    ToLower(7,
            "ToLower");

    /**
     * @param index 索引
     * @param value 值
     */
    NameConvertType(int index,
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
    public static NameConvertType toEnum(String value)
            throws
            IllegalArgumentException {
        return NameConvertType.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static NameConvertType toEnum(int index)
            throws
            IllegalArgumentException {
        for (NameConvertType value : NameConvertType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}