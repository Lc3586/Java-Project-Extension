package project.extension.system;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 操作系统平台
 */
public enum OSPlatform {
    Linux_32(1,
             "Linux_32"),
    Linux_64(2,
             "Linux_64"),
    OSX_32(3,
           "OSX_32"),
    OSX_64(4,
           "OSX_64"),
    Windows_32(5,
               "Windows_32"),
    Windows_64(6,
               "Windows_64");

    /**
     * @param index 索引
     * @param value 值
     */
    OSPlatform(int index,
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
     * @param arch  架构
     * @return 枚举
     */
    public static OSPlatform toEnum(String value,
                                    String arch)
            throws
            IllegalArgumentException {
        if (Pattern.compile("windows.*",
                            Pattern.CASE_INSENSITIVE)
                   .matcher(value)
                   .matches())
            return Objects.equals(arch,
                                  "32")
                   ? OSPlatform.Windows_32
                   : OSPlatform.Windows_64;
        else if ("Linux".equalsIgnoreCase(value))
            return Objects.equals(arch,
                                  "32")
                   ? OSPlatform.Linux_32
                   : OSPlatform.Linux_64;
        else if ("OSX".equalsIgnoreCase(value))
            return Objects.equals(arch,
                                  "32")
                   ? OSPlatform.OSX_32
                   : OSPlatform.OSX_64;

        throw new IllegalArgumentException(String.format("未找到符合%s_%s的OSPlatform枚举值",
                                                         value,
                                                         arch));
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static OSPlatform toEnum(int index)
            throws
            IllegalArgumentException {
        for (OSPlatform value : OSPlatform.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}
