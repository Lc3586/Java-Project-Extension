package project.extension.mybatis.model;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Arrays;
import java.util.Optional;

/**
 * 增删改查操作类型
 *
 * @author LCTR
 * @date 2022-07-14
 */
public enum CurdType {
    /**
     * 查询
     */
    @OpenApiDescription("查询")
    查询(1,
       "Select"),
    /**
     * 插入
     */
    @OpenApiDescription("插入")
    插入(2,
       "Insert"),
    /**
     * 更新
     */
    @OpenApiDescription("更新")
    更新(3,
       "Update"),
    /**
     * 删除
     */
    @OpenApiDescription("删除")
    删除(4,
       "Delete");

    /**
     * @param index 索引
     * @param value 值
     */
    CurdType(int index,
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
    public static CurdType toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<CurdType> find = Arrays.stream(CurdType.values())
                                        .filter(x -> x.value.equals(value))
                                        .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到符合%s此值的CurdType枚举中",
                                                             value));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static CurdType toEnum(int index)
            throws
            IllegalArgumentException {
        for (CurdType value : CurdType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}
