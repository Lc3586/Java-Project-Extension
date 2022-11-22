package project.extension.system;

import java.util.regex.Pattern;

/**
 * 操作系统平台
 */
public enum OSPlatform {
    Linux(1, "Linux"),
    OSX(2, "OSX"),
    Windows(3, "Windows");

    /**
     * @param index 索引
     * @param value 值
     */
    OSPlatform(int index, String value) {
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
    public static OSPlatform toEnum(String value) throws IllegalArgumentException {
        if (Pattern.compile("windows.*", Pattern.CASE_INSENSITIVE).matcher(value).matches())
            return OSPlatform.Windows;
        return OSPlatform.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static OSPlatform toEnum(int index) throws IllegalArgumentException {
        for (OSPlatform value : OSPlatform.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}
