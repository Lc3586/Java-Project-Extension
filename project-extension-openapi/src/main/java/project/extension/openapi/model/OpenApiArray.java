package project.extension.openapi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口架构集合对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiArray extends ArrayList<IOpenApiAny> implements IOpenApiAny {
    public OpenApiArray(IOpenApiAny any) {
        this.any = any;
    }

    public OpenApiArray(IOpenApiAny any, String description) {
        this.any = any;
        this.description.add(description);
    }

    private final IOpenApiAny any;

    private final AnyType anyType = AnyType.Array;

    private final List<String> description = new ArrayList<>();

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

    public IOpenApiAny getAny() {
        return any;
    }
}