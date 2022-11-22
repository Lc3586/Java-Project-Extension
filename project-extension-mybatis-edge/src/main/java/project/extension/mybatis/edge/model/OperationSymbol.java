package project.extension.mybatis.edge.model;

/**
 * 操作符
 *
 * @author LCTR
 * @date 2022-04-11
 */
public enum OperationSymbol {
    /**
     * 相加
     */
    Plus(0, "Plus"),
    /**
     * 相减
     */
    Reduce(1, "Reduce"),
    /**
     * 相乘
     */
    Become(2, "Become"),
    /**
     * 相除
     */
    Except(3, "Except"),
    /**
     * 取余
     */
    Remainder(4, "Remainder");

    /**
     * @param index 索引
     * @param value 值
     */
    OperationSymbol(int index, String value) {
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
    public static OperationSymbol toEnum(String value) throws IllegalArgumentException {
        return OperationSymbol.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static OperationSymbol toEnum(int index) throws IllegalArgumentException {
        for (OperationSymbol value : OperationSymbol.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}
