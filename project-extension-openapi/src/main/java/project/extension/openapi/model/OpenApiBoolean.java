package project.extension.openapi.model;

/**
 * 接口架构布尔类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiBoolean extends OpenApiPrimitive {
    public OpenApiBoolean(Boolean value) {
        super(value);
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.Boolean);
    }
}
