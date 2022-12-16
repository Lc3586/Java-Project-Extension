package project.extension.mybatis.edge.extention.datasearch;

import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.mybatis.edge.model.FilterGroupRelation;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.openapi.model.OpenApiSchemaFormat;
import project.extension.openapi.model.OpenApiSchemaType;

import java.util.List;

/**
 * 数据搜索条件
 *
 * @author LCTR
 * @date 2022-12-08
 */
@OpenApiSchemaStrictMode
public class DataSearchFilter {
    @OpenApiSchema
    @OpenApiDescription("数据表标识")
    private String tableKey;

    @OpenApiSchema
    @OpenApiDescription("要比较的字段")
    private String field;

    @OpenApiSchema(OpenApiSchemaType.string)
    @OpenApiDescription("用于比较的值")
    private Object value;

    @OpenApiSchema("false")
    @OpenApiDescription("Value值是用来比较的字段")
    private Boolean valueIsField = false;

    @OpenApiSchema(value = "Eq",
                   type = OpenApiSchemaType.enum_)
    @OpenApiDescription("比较类型")
    private FilterCompare compare = FilterCompare.Eq;

    @OpenApiSchema(value = "AND",
                   type = OpenApiSchemaType.enum_)
    @OpenApiDescription("组内关系")
    private FilterGroupRelation relation = FilterGroupRelation.AND;

    @OpenApiSchema(type = OpenApiSchemaType.model,
                   format = OpenApiSchemaFormat.model_once)
    @OpenApiDescription("子条件")
    private List<DataSearchFilter> filters;

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
     * 要比较的字段
     */
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    /**
     * 用于比较的值
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Value值是用来比较的字段
     */
    public Boolean getValueIsField() {
        return valueIsField;
    }

    public void setValueIsField(Boolean valueIsField) {
        this.valueIsField = valueIsField;
    }

    /**
     * 比较类型
     * <p>默认值 Eq</p>
     */
    public FilterCompare getCompare() {
        return compare;
    }

    public void setCompare(FilterCompare compare) {
        this.compare = compare;
    }

    /**
     * 组内关系
     * <p>默认值 AND</p>
     */
    public FilterGroupRelation getRelation() {
        return relation;
    }

    public void setRelation(FilterGroupRelation relation) {
        this.relation = relation;
    }

    /**
     * 子条件
     */
    public List<DataSearchFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<DataSearchFilter> filters) {
        this.filters = filters;
    }
}