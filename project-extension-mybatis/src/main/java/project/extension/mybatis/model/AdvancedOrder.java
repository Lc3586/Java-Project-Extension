package project.extension.mybatis.model;

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

    @OpenApiSchema
    private String alias;

    @OpenApiSchema
    private String fieldName;

    @OpenApiSchema(value = "DESC", type = OpenApiSchemaType.enum_)
    private OrderMethod method;

    /**
     * 数据表别名
     */
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 排序列
     */
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 排序方法
     * <p>默认值 DES（降序）</p>
     */
    public OrderMethod getMethod() {
        return method;
    }

    public void setMethod(OrderMethod method) {
        this.method = method;
    }
}
