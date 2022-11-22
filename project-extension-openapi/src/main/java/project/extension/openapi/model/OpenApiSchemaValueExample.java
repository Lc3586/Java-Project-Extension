package project.extension.openapi.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 接口架构属性的示例值
 *
 * @author LCTR
 * @date 2022-03-18
 */
public class OpenApiSchemaValueExample {
    public static final Boolean boolean_ = true;

    public static final Byte byte_ = Byte.MAX_VALUE;

    public static final Byte[] byte_array = {Byte.MAX_VALUE, (Byte.MAX_VALUE - 1), (Byte.MAX_VALUE - 2)};

    public static final Integer int_ = Integer.MAX_VALUE;

    public static final Long long_ = Long.MAX_VALUE;

    public static final Float float_ = Float.MAX_VALUE;

    public static final Double double_ = Double.MAX_VALUE;

    public static final BigDecimal decimal = new BigDecimal(OpenApiSchemaValueExample.double_);

    public static final String string = "字符串内容";

    public static final Date date_original = new Date();

    public static final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public static final String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

    public static final String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public static final String timespan = "15:59:59";
}
