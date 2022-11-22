package project.extension.openapi.model;

/**
 * 接口架构32位整数类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiInteger extends OpenApiPrimitive {
    public OpenApiInteger(Integer value) {
        super(value);
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.Integer);
    }
}
