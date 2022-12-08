package project.extension.standard.datasearch;

import project.extension.mybatis.edge.model.Pagination;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.openapi.model.ApiData.ApiDataSchema;
import project.extension.openapi.model.OpenApiSchemaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据搜索参数
 *
 * @author LCTR
 * @date 2022-12-08
 */
@OpenApiSchemaStrictMode
public class DataSearchDTO {
    @OpenApiDescription("数据结构方案（默认值 Default，也就是偌依框架的数据结果）")
    @OpenApiSchema(value = "Default", type = OpenApiSchemaType.enum_)
    private ApiDataSchema schema = ApiDataSchema.Default;

    @OpenApiDescription("分页设置（默认设置 进行分页，每页50条数据）")
    private Pagination pagination = new Pagination();

    @OpenApiDescription("搜索条件")
    private List<DataSearchFilter> filters = new ArrayList<>();

    @OpenApiDescription("自定义参数")
    private Map<String,Object> customParameter = new HashMap<>();

    @OpenApiDescription("排序条件")
    private DataSearchOrder order;

    /**
     * 数据结构方案
     * <p>默认值 Default</p>
     */
    public ApiDataSchema getSchema() {
        return schema;
    }

    public void setSchema(ApiDataSchema schema) {
        this.schema = schema;
    }

    /**
     * 分页设置
     */
    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * 搜索条件
     */
    public List<DataSearchFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<DataSearchFilter> filters) {
        this.filters = filters;
    }

    /**
     * 自定义参数
     */
    public Map<String, Object> getCustomParameter() {
        return customParameter;
    }

    public void setCustomParameter(Map<String, Object> customParameter) {
        this.customParameter = customParameter;
    }

    /**
     * 排序条件
     */
    public DataSearchOrder getOrder() {
        return order;
    }

    public void setOrder(DataSearchOrder order) {
        this.order = order;
    }
}
