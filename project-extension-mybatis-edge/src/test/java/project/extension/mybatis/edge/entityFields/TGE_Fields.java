package project.extension.mybatis.edge.entityFields;

/**
 * 测试读写常规数据实体字段
 *
 * @author LCTR
 * @date 2022-01-09
 * @see project.extension.mybatis.edge.entity.TestGeneralEntity
 */
public class TGE_Fields {
    /**
     * 主键
     */
    public static final String id = "id";

    /**
     * 字符
     */
    public static final String char_ = "char_";

    /**
     * 字符串
     */
    public static final String string = "string";

    /**
     * 8位整数
     */
    public static final String byte_ = "byte_";

    /**
     * 16位整数
     */
    public static final String short_ = "short_";

    /**
     * 32位整数
     */
    public static final String integer = "integer";

    /**
     * 64位整数
     */
    public static final String long_ = "long_";

    /**
     * 单精度浮点数
     */
    public static final String float_ = "float_";

    /**
     * 双精度浮点数
     */
    public static final String double_ = "double_";

    /**
     * 高精度浮点数
     */
    public static final String decimal = "decimal";

    /**
     * 布尔
     */
    public static final String boolean_ = "boolean_";

    /**
     * 日期
     */
    public static final String date = "date";

    /**
     * 时间
     */
    public static final String time = "time";

    /**
     * 日期时间
     */
    public static final String datetime = "datetime";

    /**
     * 全部字段
     */
    public static final String[] allFields
            = new String[]{
            id,
            char_,
            string,
            byte_,
            short_,
            integer,
            long_,
            float_,
            double_,
            decimal,
            boolean_,
            date,
//            time,
            datetime
    };
}
