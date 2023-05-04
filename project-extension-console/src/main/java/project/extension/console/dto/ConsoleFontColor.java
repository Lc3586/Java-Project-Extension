package project.extension.console.dto;

import java.util.Arrays;
import java.util.Optional;

/**
 * 控制台输出时的字体颜色
 *
 * @author LCTR
 * @date 2023-04-28
 */
public enum ConsoleFontColor {
    /**
     * 默认
     */
    默认(0,
       "默认"),
    /**
     * 红色
     */
    红色(31,
       "红色"),
    /**
     * 绿色
     */
    绿色(32,
       "绿色"),
    /**
     * 黄色
     */
    黄色(33,
       "黄色"),
    /**
     * 蓝色
     */
    蓝色(34,
       "蓝色"),
    /**
     * 紫色
     */
    紫色(35,
       "紫色"),
    /**
     * 青色
     */
    青色(36,
       "青色"),
    /**
     * 灰色
     */
    灰色(37,
       "灰色"),
    /**
     * 白色
     */
    白色(97,
       "白色");

    /**
     * @param index 索引
     * @param value 值
     */
    ConsoleFontColor(int index,
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
    public static ConsoleFontColor toEnum(String value) {
        Optional<ConsoleFontColor> find = Arrays.stream(ConsoleFontColor.values())
                                                .filter(x -> x.value.equals(value))
                                                .findFirst();
        return find.orElse(ConsoleFontColor.默认);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static ConsoleFontColor toEnum(int index) {
        for (ConsoleFontColor value : ConsoleFontColor.values()) {
            if (value.getIndex() == index)
                return value;
        }
        return ConsoleFontColor.默认;
    }
}