package project.extension.mybatis.edge.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 动态过滤条件比较类型
 *
 * @author LCTR
 * @date 2022-03-24
 */
public enum FilterCompare {
    /**
     * 相等
     * <P>column = value</P>
     */
    @OpenApiDescription("相等")
    Eq(0,
       "Eq"),
    /**
     * 相等（忽略大小写）
     * <P>lower(column) = lower(value)</P>
     */
    @OpenApiDescription("相等（忽略大小写）")
    EqIgnoreCase(1,
                 "EqIgnoreCase"),
    /**
     * 不相等
     * <P>column != value</P>
     */
    @OpenApiDescription("不相等")
    NotEq(2,
          "NotEq"),
    /**
     * 不相等（忽略大小写）
     * <P>lower(column) != lower(value)</P>
     */
    @OpenApiDescription("不相等（忽略大小写）")
    NotEqIgnoreCase(3,
                    "NotEq"),
    /**
     * 小于等于
     * <P>column <= value</P>
     */
    @OpenApiDescription("小于等于")
    Le(4,
       "Le"),
    /**
     * 小于
     * <P>column < value</P>
     */
    @OpenApiDescription("小于")
    Lt(5,
       "Lt"),
    /**
     * 大于等于
     * <P>column >= value</P>
     */
    @OpenApiDescription("大于等于")
    Ge(6,
       "Ge"),
    /**
     * 大于
     * <P>column > value</P>
     */
    @OpenApiDescription("大于")
    Gt(7,
       "Gt"),
    /**
     * 包含
     * <P>column like '%value%'</P>
     */
    @OpenApiDescription("包含")
    In(8,
       "In"),
    /**
     * 包含（忽略大小写）
     * <P>lower(column) like lower('%value%')</P>
     */
    @OpenApiDescription("包含（忽略大小写）")
    InIgnoreCase(9,
                 "InIgnoreCase"),
    /**
     * 不包含
     * <P>column not like '%value%'</P>
     */
    @OpenApiDescription("不包含")
    NotIn(10,
          "NotIn"),
    /**
     * 不包含（忽略大小写）
     * <P>lower(column) not like lower('%value%')</P>
     */
    @OpenApiDescription("不包含（忽略大小写）")
    NotInIgnoreCase(11,
                    "NotInIgnoreCase"),
    /**
     * 前段包含
     * <P>column like 'value%'</P>
     */
    @OpenApiDescription("前段包含")
    InStart(12,
            "InStart"),
    /**
     * 前段包含（忽略大小写）
     * <P>lower(column) like lower('value%')</P>
     */
    @OpenApiDescription("前段包含（忽略大小写）")
    InStartIgnoreCase(13,
                      "InStartIgnoreCase"),
    /**
     * 前段不包含
     * <P>column not like 'value%'</P>
     */
    @OpenApiDescription("前段不包含")
    NotInStart(14,
               "NotInStart"),
    /**
     * 前段不包含（忽略大小写）
     * <P>lower(column) not like lower('value%')</P>
     */
    @OpenApiDescription("前段不包含（忽略大小写）")
    NotInStartIgnoreCase(15,
                         "NotInStartIgnoreCase"),
    /**
     * 后段包含
     * <P>column like '%value'</P>
     */
    @OpenApiDescription("后段包含")
    InEnd(16,
          "InEnd"),
    /**
     * 后段包含（忽略大小写）
     * <P>lower(column) like lower('%value')</P>
     */
    @OpenApiDescription("后段包含（忽略大小写）")
    InEndIgnoreCase(15,
                    "InEndIgnoreCase"),
    /**
     * 后段不包含
     * <P>column not like '%value'</P>
     */
    @OpenApiDescription("后段不包含")
    NotInEnd(16,
             "NotInEnd"),
    /**
     * 后段不包含（忽略大小写）
     * <P>lower(column) not like lower('%value')</P>
     */
    @OpenApiDescription("后段不包含（忽略大小写）")
    NotInEndIgnoreCase(17,
                       "NotInEndIgnoreCase"),
    /**
     * 包含于
     * <P>value like concat('%', column, '%')</P>
     */
    @OpenApiDescription("包含于")
    IncludedIn(18,
               "IncludedIn"),
    /**
     * 包含于（忽略大小写）
     * <P>lower(value) like concat('%', lower(column), '%')</P>
     */
    @OpenApiDescription("包含于（忽略大小写）")
    IncludedInIgnoreCase(19,
                         "IncludedInIgnoreCase"),
    /**
     * 不包含于
     * <P>value not like concat('%', column, '%')</P>
     */
    @OpenApiDescription("不包含于")
    NotIncludedIn(20,
                  "NotIncludedIn"),
    /**
     * 不包含于（忽略大小写）
     * <P>lower(value) not like concat('%', lower(column), '%')</P>
     */
    @OpenApiDescription("不包含于（忽略大小写）")
    NotIncludedInIgnoreCase(21,
                            "NotIncludedInIgnoreCase"),
    /**
     * 在集合中
     * <P>column in (value1, value2, value3)</P>
     */
    @OpenApiDescription("在集合中")
    InSet(22,
          "InSet"),
    /**
     * 在集合中（忽略大小写）
     * <P>lower(column) in (lower(value1), lower(value2), lower(value3))</P>
     */
    @OpenApiDescription("在集合中（忽略大小写）")
    InSetIgnoreCase(23,
                    "InSetIgnoreCase"),
    /**
     * 不在集合中
     * <P>column not in (value1, value2, value3)</P>
     */
    @OpenApiDescription("不在集合中")
    NotInSet(24,
             "NotInSet"),
    /**
     * 不在集合中（忽略大小写）
     * <P>lower(column) not in (lower(value1), lower(value2), lower(value3))</P>
     */
    @OpenApiDescription("不在集合中（忽略大小写）")
    NotInSetIgnoreCase(25,
                       "NotInSetIgnoreCase"),
    /**
     * 范围
     * <P>column >= value1 and column < value2</P>
     */
    @OpenApiDescription("范围")
    Range(26,
          "Range"),
    /**
     * 日期范围
     * <p>这是专门为日期范围查询定制的操作符，它会处理 date2 + 1，比如：</p>
     * <p>当 date2 选择的是 2020-05-30，那查询的时候是 小于 2020-05-31</p>
     * <p>当 date2 选择的是 2020-05，那查询的时候是 小于 2020-06</p>
     * <p>当 date2 选择的是 2020，那查询的时候是 小于 2021</p>
     * <p>当 date2 选择的是 2020-05-30 12，那查询的时候是 小于 2020-05-30 13</p>
     * <p>当 date2 选择的是 2020-05-30 12:30，那查询的时候是 小于 2020-05-30 12:31</p>
     * <p>当 date2 选择的是 2020-05-30 12:30:30，那查询的时候是 小于 2020-05-30 12:30:31</p>
     * <p>并且 date2 只支持以上 6 种格式 (date1 没有限制)</p>
     * <P>column >= value1 and column < value2</P>
     */
    @OpenApiDescription("日期范围")
    DateRange(27,
              "DateRange"),
    /**
     * 严格模式的日期范围
     * <p>传的什么值，就会使用什么值进行判断</p>
     */
    @OpenApiDescription("严格模式的日期范围")
    DateRangeStrict(28,
                    "DateRangeStrict");

    /**
     * @param index 索引
     * @param value 值
     */
    FilterCompare(int index,
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
    public static FilterCompare toEnum(String value)
            throws
            IllegalArgumentException {
        return FilterCompare.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static FilterCompare toEnum(int index)
            throws
            IllegalArgumentException {
        for (FilterCompare value : FilterCompare.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}
