package project.extension.standard.authentication;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Arrays;
import java.util.Optional;

/**
 * 身份验证方法
 *
 * @author LCTR
 * @date 2022-12-08
 */
public enum AuthMethod {
    /**
     * 跨域身份验证票据
     */
    @OpenApiDescription("json web token")
    JWT(0,
        "JWT"),
    /**
     * cookie
     */
    @OpenApiDescription("cookie")
    Cookie(1,
           "Cookie");

    /**
     * @param index 索引
     * @param value 值
     */
    AuthMethod(int index,
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
    public static AuthMethod toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<AuthMethod> find = Arrays.stream(AuthMethod.values())
                                          .filter(x -> x.value.equals(value))
                                          .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到值为%s的%s枚举",
                                                             value,
                                                             AuthMethod.class.getName()));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static AuthMethod toEnum(int index)
            throws
            IllegalArgumentException {
        for (AuthMethod value : AuthMethod.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}
