package project.extension.standard.datasearch;

import project.extension.mybatis.edge.model.OrderMethod;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiMainTag;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.openapi.model.OpenApiSchemaType;

/**
 * 高级排序
 *
 * @author LCTR
 * @date 2022-12-08
 */
@OpenApiSchemaStrictMode
@OpenApiMainTag("DataSearchAdvancedOrder")
public class DataSearchAdvancedOrder {
    @OpenApiSchema
    @OpenApiDescription("数据表标识")
    private String tableKey;

    @OpenApiSchema
    private String field;

    @OpenApiSchema(value = "DESC", type = OpenApiSchemaType.enum_)
    private OrderMethod method;

    /**
     * 数据表标识
     */
    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    /**
     * 排序字段
     */
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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