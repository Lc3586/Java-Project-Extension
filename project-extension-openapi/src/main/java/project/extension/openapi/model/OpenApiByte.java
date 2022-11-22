package project.extension.openapi.model;

/**
 * 接口架构字节类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiByte extends OpenApiPrimitive {
    public OpenApiByte(Byte value) {
        super(new Byte[]{value});
        super.setAnyType(AnyType.Primitive);
        super.setPrimitiveType(PrimitiveType.Byte);
    }
}
