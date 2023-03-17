package project.extension.wechat.model;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Arrays;
import java.util.Optional;

/**
 * 微信支付通通知类型
 *
 * @author LCTR
 * @date 2023-03-16
 */
public enum WeChatPayNotifyType {
    /**
     * 付款
     */
    @OpenApiDescription("付款")
    Pay(0,
        "Pay"),
    /**
     * 支付分
     */
    @OpenApiDescription("支付分")
    PayScore(1,
             "PayScore"),
    /**
     * 退款
     */
    @OpenApiDescription("退款")
    Refund(2,
           "Refund");

    /**
     * @param index 索引
     * @param value 值
     */
    WeChatPayNotifyType(int index,
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
    public static WeChatPayNotifyType toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<WeChatPayNotifyType> find = Arrays.stream(WeChatPayNotifyType.values())
                                                   .filter(x -> x.value.equals(value))
                                                   .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到值为%s的%s枚举",
                                                             value,
                                                             WeChatPayNotifyType.class.getName()));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static WeChatPayNotifyType toEnum(int index)
            throws
            IllegalArgumentException {
        for (WeChatPayNotifyType value : WeChatPayNotifyType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}
