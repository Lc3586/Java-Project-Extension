package project.extension.openapi.model.ApiData;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 异常代码
 *
 * @author LCTR
 * @date 2022-03-25
 */
public enum ErrorCode {
    /**
     * 无
     */
    @OpenApiDescription("无")
    None(0, "None"),
    /**
     * 未认证
     */
    @OpenApiDescription("未认证")
    UnAuthorized(1, "UnAuthorized"),
    /**
     * 权限不足
     */
    @OpenApiDescription("权限不足")
    Forbidden(2, "Forbidden"),
    /**
     * 数据验证不通过
     */
    @OpenApiDescription("数据验证不通过")
    Validation(3, "Validation"),
    /**
     * 一般业务错误
     */
    @OpenApiDescription("一般业务错误")
    Business(4, "Business"),
    /**
     * 系统错误
     */
    @OpenApiDescription("系统错误")
    Error(5, "Error"),
    /**
     * 未知错误
     */
    @OpenApiDescription("未知错误")
    Unknown(6, "Unknown");

    /**
     * @param index 索引
     * @param value 值
     */
    ErrorCode(int index, String value) {
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
    public static ErrorCode toEnum(String value) throws IllegalArgumentException {
        return ErrorCode.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static ErrorCode toEnum(int index) throws IllegalArgumentException {
        for (ErrorCode value : ErrorCode.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}
