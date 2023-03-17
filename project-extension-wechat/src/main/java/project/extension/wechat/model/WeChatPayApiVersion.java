package project.extension.wechat.model;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Arrays;
import java.util.Optional;

/**
 * 微信支付接口版本
 *
 * @author LCTR
 * @date 2023-03-14
 */
public enum WeChatPayApiVersion {
    /**
     * 普通
     * <p>https://pay.weixin.qq.com/wiki/doc/api/index.html</p>
     */
    @OpenApiDescription("普通")
    NORMAL(0,
           "NORMAL"),
    /**
     * V2
     * <p>https://pay.weixin.qq.com/wiki/doc/api/index.html</p>
     */
    @OpenApiDescription("V2")
    V2(2,
       "V2"),
    /**
     * V3
     * <p>https://pay.weixin.qq.com/wiki/doc/apiv3/index.shtml</p>
     */
    @OpenApiDescription("V3")
    V3(3,
       "V3");

    /**
     * @param index 索引
     * @param value 值
     */
    WeChatPayApiVersion(int index,
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
    public static WeChatPayApiVersion toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<WeChatPayApiVersion> find = Arrays.stream(WeChatPayApiVersion.values())
                                                   .filter(x -> x.value.equals(value))
                                                   .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到值为%s的%s枚举",
                                                             value,
                                                             WeChatPayApiVersion.class.getName()));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static WeChatPayApiVersion toEnum(int index)
            throws
            IllegalArgumentException {
        for (WeChatPayApiVersion value : WeChatPayApiVersion.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}
