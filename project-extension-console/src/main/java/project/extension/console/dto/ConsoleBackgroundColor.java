package project.extension.console.dto;

import java.util.Arrays;
import java.util.Optional;

/**
 * 控制台输出时的背景颜色
 *
 * @author LCTR
 * @date 2023-04-28
 */
public enum ConsoleBackgroundColor {
    /**
     * 红色
     */
    红色(41,
       "红色"),
    /**
     * 绿色
     */
    绿色(42,
       "绿色"),
    /**
     * 黄色
     */
    黄色(43,
       "黄色"),
    /**
     * 蓝色
     */
    蓝色(44,
       "蓝色"),
    /**
     * 紫色
     */
    紫色(45,
       "紫色"),
    /**
     * 青色
     */
    青色(46,
       "青色"),
    /**
     * 灰色
     */
    灰色(47,
       "灰色"),
    /**
     * 黑色
     */
    黑色(40,
       "黑色");

    /**
     * @param index 索引
     * @param value 值
     */
    ConsoleBackgroundColor(int index,
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
    public static ConsoleBackgroundColor toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<ConsoleBackgroundColor> find = Arrays.stream(ConsoleBackgroundColor.values())
                                                      .filter(x -> x.value.equals(value))
                                                      .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到符合%s此值的ConsoleBackgroundColor枚举",
                                                             value));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static ConsoleBackgroundColor toEnum(int index)
            throws
            IllegalArgumentException {
        for (ConsoleBackgroundColor value : ConsoleBackgroundColor.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}