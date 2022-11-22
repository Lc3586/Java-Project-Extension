package project.extension.openapi.model;

import java.util.Date;

/**
 * 接口架构日期时间类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiDate extends OpenApiPrimitive {
    public OpenApiDate(Date value) {
        super(value);
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.Date);
    }
}
