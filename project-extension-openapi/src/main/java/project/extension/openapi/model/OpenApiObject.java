package project.extension.openapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 接口架构对象
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiObject extends HashMap<String, IOpenApiAny> implements IOpenApiAny {
    public OpenApiObject() {

    }

    public OpenApiObject(String description) {
        this.description.add(description);
    }

    private final AnyType anyType = AnyType.Object;

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
}
