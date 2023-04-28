package project.extension.console.dto;

import java.util.Arrays;
import java.util.Optional;

/**
 * 控制台输出时的字体样式
 *
 * @author LCTR
 * @date 2023-04-28
 */
public enum ConsoleFontStyle {
    /**
     * 重置
     */
    重置(0,
       "重置"),
    /**
     * 加粗
     */
    加粗(1,
       "加粗"),
    /**
     * 减弱
     */
    减弱(2,
       "减弱"),
    /**
     * 斜体
     */
    斜体(3,
       "斜体"),
    /**
     * 下划线
     */
    下划线(4,
        "下划线"),
    /**
     * 慢速闪烁
     */
    慢速闪烁(5,
         "慢速闪烁"),
    /**
     * 快速闪烁
     */
    快速闪烁(6,
         "快速闪烁");

    /**
     * @param index 索引
     * @param value 值
     */
    ConsoleFontStyle(int index,
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
    public static ConsoleFontStyle toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<ConsoleFontStyle> find = Arrays.stream(ConsoleFontStyle.values())
                                                .filter(x -> x.value.equals(value))
                                                .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到符合%s此值的ConsoleFontStyle枚举",
                                                             value));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static ConsoleFontStyle toEnum(int index)
            throws
            IllegalArgumentException {
        for (ConsoleFontStyle value : ConsoleFontStyle.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}