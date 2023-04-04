package project.extension.mybatis.edge.model;

import lombok.Data;
import project.extension.openapi.annotations.OpenApiMainTag;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.openapi.model.OpenApiSchemaType;

/**
 * 高级排序
 *
 * @author LCTR
 * @date 2022-03-24
 */
@OpenApiSchemaStrictMode
@OpenApiMainTag("AdvancedOrder")
@Data
public class AdvancedOrder {
    /**
     * @param fieldName 字段名
     * @param method    排序方法
     */
    public AdvancedOrder(String fieldName, OrderMethod method) {
        this(fieldName, method, null);
    }

    /**
     * @param fieldName 字段名
     * @param method    排序方法
     * @param alias     别名
     */
    public AdvancedOrder(String fieldName, OrderMethod method, String alias) {
        this.alias = alias;
        this.fieldName = fieldName;
        this.method = method;
    }

    /**
     * 数据表别名
     */
    @OpenApiSchema
    private String alias;

    /**
     * 排序列
     */
    @OpenApiSchema
    private String fieldName;

    /**
     * 排序方法
     * <p>默认值 DES（降序）</p>
     */
    @OpenApiSchema(value = "DESC", type = OpenApiSchemaType.enum_)
    private OrderMethod method;
}
