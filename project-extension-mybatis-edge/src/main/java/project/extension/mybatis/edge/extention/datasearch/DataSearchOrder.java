package project.extension.mybatis.edge.extention.datasearch;

import project.extension.mybatis.edge.model.OrderMethod;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.openapi.model.OpenApiSchemaType;

import java.util.List;

/**
 * 数据排序条件
 *
 * @author LCTR
 * @date 2022-12-08
 */
@OpenApiSchemaStrictMode
public class DataSearchOrder {
    @OpenApiSchema
    @OpenApiDescription("数据表标识")
    private String tableKey;

    @OpenApiSchema("create_time")
    @OpenApiDescription("排序字段")
    private String field;

    @OpenApiSchema(value = "DESC",
                   type = OpenApiSchemaType.enum_)
    @OpenApiDescription("排序方法")
    private OrderMethod method = OrderMethod.DESC;

    @OpenApiSchema(type = OpenApiSchemaType.model)
    @OpenApiDescription("高级排序")
    private List<DataSearchAdvancedOrder> advancedOrder;

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
     * <p>默认值 createTime</p>
     */
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    /**
     * 排序方法
     * <p>默认值 DESC（降序）</p>
     */
    public OrderMethod getMethod() {
        return method;
    }

    public void setMethod(OrderMethod method) {
        this.method = method;
    }

    /**
     * 高级排序
     */
    public List<DataSearchAdvancedOrder> getAdvancedOrder() {
        return advancedOrder;
    }

    public void setAdvancedOrder(List<DataSearchAdvancedOrder> advancedOrder) {
        this.advancedOrder = advancedOrder;
    }
}