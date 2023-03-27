package project.extension.wechat.model;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Arrays;
import java.util.Optional;

/**
 * 微信加密类型
 *
 * @author LCTR
 * @date 2023-03-22
 */
public enum WeChatEncryptType {
    /**
     * 明文
     */
    @OpenApiDescription("明文")
    RAW(0,
        "raw"),
    /**
     * AES加密
     */
    @OpenApiDescription("AES加密")
    AES(1,
        "aes");

    /**
     * @param index 索引
     * @param value 值
     */
    WeChatEncryptType(int index,
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
    public static WeChatEncryptType toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<WeChatEncryptType> find = Arrays.stream(WeChatEncryptType.values())
                                                 .filter(x -> x.value.equals(value))
                                                 .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到值为%s的%s枚举",
                                                             value,
                                                             WeChatEncryptType.class.getName()));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static WeChatEncryptType toEnum(int index)
            throws
            IllegalArgumentException {
        for (WeChatEncryptType value : WeChatEncryptType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}
