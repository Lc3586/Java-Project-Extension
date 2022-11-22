package project.extension.openapi.model;

/**
 * 接口架构64位整数类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiLong extends OpenApiPrimitive {
    public OpenApiLong(Long value) {
        super(value);
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.Long);
    }
}
