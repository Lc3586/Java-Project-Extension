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
 * JqGrid数据结构方案
 *
 * @author LCTR
 * @date 2022-03-25
 */
@OpenApiSchemaStrictMode
@JSONType(ignores = {"total_", "records_", "costtime_"})
public class JqGridResult<T> implements IApiData<T> {
    @OpenApiIgnore
    @JsonIgnore
    private Long total_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("总页数")
    private String total;

    @OpenApiSchema
    @OpenApiDescription("当前页码")
    private Integer page;

    @OpenApiIgnore
    @JsonIgnore
    private Long records_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("总记录数")
    private String records;

    @OpenApiIgnore
    @JsonIgnore
    private Long costtime_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("耗时（毫秒）")
    private String costtime;

    @OpenApiSchema(OpenApiSchemaType.model)
    @OpenApiDescription("数据集合")
    private List<T> rows;

    /**
     * 总页数
     */
    public Long getTotal_() {
        return total_;
    }

    public void setTotal_(Long total_) {
        this.total_ = total_;
        this.total = null;
    }

    /**
     * 总页数
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
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * 总记录数
     */
    public Long getRecords_() {
        return records_;
    }

    public void setRecords_(Long records_) {
        this.records_ = records_;
        this.records = null;
    }

    /**
     * 总记录数
     */
    public String getRecords() {
        if (records == null)
            records = Long.toString(records_);
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
        this.records_ = Long.parseLong(records);
    }

    /**
     * 耗时（毫秒）
     */
    public Long getCosttime_() {
        return costtime_;
    }

    public void setCosttime_(Long costtime_) {
        this.costtime_ = costtime_;
        this.costtime = null;
    }

    /**
     * 耗时（毫秒）
     */
    public String getCosttime() {
        if (costtime == null)
            costtime = Long.toString(costtime_);
        return costtime;
    }

    public void setCosttime(String costtime) {
        this.costtime = costtime;
        this.costtime_ = Long.parseLong(costtime);
    }

    /**
     * 数据
     */
    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
