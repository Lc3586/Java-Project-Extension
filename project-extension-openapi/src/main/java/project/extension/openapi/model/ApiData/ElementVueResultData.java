package project.extension.openapi.model.ApiData;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiIgnore;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.openapi.model.OpenApiSchemaFormat;
import project.extension.openapi.model.OpenApiSchemaType;

import java.util.List;

/**
 * ElementVue数据结构方案
 *
 * @author LCTR
 * @date 2022-03-25
 */
@OpenApiSchemaStrictMode
@JSONType(ignores = {"pageTotal_", "total_"})
public class ElementVueResultData<T> implements IApiData<T> {
    @OpenApiIgnore
    @JsonIgnore
    private Long pageTotal_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("总页数")
    private String pageTotal;

    @OpenApiIgnore
    @JsonIgnore
    private Long total_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("总记录数")
    private String total;

    @OpenApiSchema
    @OpenApiDescription("当前页码")
    private Integer pageIndex;

    @OpenApiSchema
    @OpenApiDescription("每页数据量")
    private Integer pageSize;

    @OpenApiSchema(type = OpenApiSchemaType.model)
    @OpenApiDescription("数据集合")
    private List<T> list;

    /**
     * 总页数
     */
    public Long getPageTotal_() {
        return pageTotal_;
    }

    public void setPageTotal_(Long pageTotal_) {
        this.pageTotal_ = pageTotal_;
        this.pageTotal = null;
    }

    /**
     * 总页数
     */
    public String getPageTotal() {
        if (pageTotal == null)
            pageTotal = Long.toString(pageTotal_);
        return pageTotal;
    }

    public void setPageTotal(String pageTotal) {
        this.pageTotal = pageTotal;
        this.pageTotal_ = Long.parseLong(pageTotal);
    }

    /**
     * 总记录数
     */
    public Long getTotal_() {
        return total_;
    }

    public void setTotal_(Long total_) {
        this.total_ = total_;
        this.total = null;
    }

    /**
     * 总记录数
     */
    public String getTotal() {
        if (total == null)
            total = Long.toString(total_);
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
        this.total_ = Long.parseLong(total);
    }

    /**
     * 当前页码
     */
    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 每页数据量
     */
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 数据
     */
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
