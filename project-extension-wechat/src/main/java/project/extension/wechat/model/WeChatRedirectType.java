package project.extension.wechat.model;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Arrays;
import java.util.Optional;

/**
 * 微信重定向类型
 *
 * @author LCTR
 * @date 2023-03-17
 */
public enum WeChatRedirectType {
    /**
     * 基础授权接口
     */
    @OpenApiDescription("基础授权接口")
    OAUTH2_BASE_API(0,
                    "OAUTH2_BASE_API"),
    /**
     * 基础授权回调
     */
    @OpenApiDescription("基础授权回调")
    OAUTH2_BASE_RETURN(1,
                       "OAUTH2_BASE_RETURN"),
    /**
     * 身份信息授权接口
     */
    @OpenApiDescription("身份信息授权接口")
    OAUTH2_USER_INFO_API(2,
                         "OAUTH2_USER_INFO_API"),
    /**
     * 身份信息授权回调
     */
    @OpenApiDescription("身份信息授权回调")
    OAUTH2_USER_INFO_RETURN(3,
                            "OAUTH2_USER_INFO_RETURN");

    /**
     * @param index 索引
     * @param value 值
     */
    WeChatRedirectType(int index,
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
    public static WeChatRedirectType toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<WeChatRedirectType> find = Arrays.stream(WeChatRedirectType.values())
                                                  .filter(x -> x.value.equals(value))
                                                  .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到值为%s的%s枚举",
                                                             value,
                                                             WeChatRedirectType.class.getName()));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static WeChatRedirectType toEnum(int index)
            throws
            IllegalArgumentException {
        for (WeChatRedirectType value : WeChatRedirectType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}
