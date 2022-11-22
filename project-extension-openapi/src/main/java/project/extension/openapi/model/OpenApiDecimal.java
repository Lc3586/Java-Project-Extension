package project.extension.openapi.model;


import java.math.BigDecimal;

/**
 * 接口架构高精度浮点数类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiDecimal extends OpenApiPrimitive {
    public OpenApiDecimal(BigDecimal value) {
        super(value);
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.Decimal);
    }
}
