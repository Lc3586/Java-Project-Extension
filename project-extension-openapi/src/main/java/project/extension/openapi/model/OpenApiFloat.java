package project.extension.openapi.model;


/**
 * 接口架构单精度浮点数类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiFloat extends OpenApiPrimitive {
    public OpenApiFloat(Float value) {
        super(value);
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.Float);
    }
}
