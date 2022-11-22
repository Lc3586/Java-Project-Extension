package project.extension.openapi.model;

/**
 * 接口架构属性的格式
 *
 * @author LCTR
 * @date 2022-03-18
 */
public class OpenApiSchemaFormat {
    /**
     * 字节
     */
    public static final String integer_byte = "byte";

    /**
     * 二进制数组
     */
    public static final String integer_binary = "binary";

    /**
     * 32位整型
     */
    public static final String integer_int32 = "int32";

    /**
     * 64位整型
     */
    public static final String integer_int64 = "int64";

    /**
     * 单精度浮点数
     */
    public static final String number_float = "float";

    /**
     * 双精度浮点数
     */
    public static final String number_double = "double";

    /**
     * 高精度浮点数
     */
    public static final String number_decimal = "decimal";

    /**
     * 日期
     */
    public static final String string_date = "date";

    /**
     * 日期和时间
     */
    public static final String string_datetime = "date-time";

    /**
     * 时间
     */
    public static final String string_time = "time";

    /**
     * 时间间隔
     */
    public static final String string_timespan = "timespan";

    /**
     * 原始时间
     */
    public static final String string_date_original = "date-original";

    /**
     * 密文
     */
    public static final String string_password = "password";

    /**
     * 只解析一次
     * <p>防止无限递归</p>
     */
    public static final String model_once = "once";

    /**
     * 泛型类型
     */
    public static final String model_generic_type = "generic_type";

    /**
     * 泛型类型 只解析一次
     * <p>防止无限递归</p>
     */
    public static final String model_generic_type_once = "generic_type_once";

    /**
     * 上传文件
     * <p>单文件</p>
     */
    public static final String file_single = "file-single";

    /**
     * 上传文件
     * <p>多文件</p>
     */
    public static final String file_multipart = "file-multipart";
}
