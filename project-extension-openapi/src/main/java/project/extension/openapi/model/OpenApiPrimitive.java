package project.extension.openapi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口架构原始类型数据对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public abstract class OpenApiPrimitive implements IOpenApiAny {
    public OpenApiPrimitive(Object value) {
        this.value = value;
    }

    public OpenApiPrimitive(Object value, String description) {
        this.value = value;
        this.description.add(description);
    }

    private final Object value;

    private AnyType anyType;

    private PrimitiveType primitiveType;

    private final List<String> description = new ArrayList<>();

    protected void setAnyType(AnyType anyType) {
        this.anyType = anyType;
    }

    @Override
    public AnyType getAnyType() {
        return this.anyType;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public void addDescription(String description) {
        this.description.add(description);
    }

    protected void setPrimitiveType(PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }

    public PrimitiveType getPrimitiveType() {
        return this.primitiveType;
    }

    public Object getValue() {
        return value;
    }
}
