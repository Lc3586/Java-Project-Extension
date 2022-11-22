package project.extension.openapi.model;


/**
 * 接口架构双精度浮点数类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiDouble extends OpenApiPrimitive {
    public OpenApiDouble(Double value) {
        super(value);
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.Double);
    }
}
