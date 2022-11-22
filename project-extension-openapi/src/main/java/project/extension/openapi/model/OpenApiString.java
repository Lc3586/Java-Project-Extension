package project.extension.openapi.model;

/**
 * 接口架构字符串类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiString extends OpenApiPrimitive {
    public OpenApiString(String value) {
        super(value);
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.String);
    }
}
