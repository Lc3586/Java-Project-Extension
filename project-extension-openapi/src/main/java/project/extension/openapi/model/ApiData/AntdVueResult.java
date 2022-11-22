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
 * AntdVue数据结构方案
 *
 * @author LCTR
 * @date 2022-03-25
 */
@OpenApiSchemaStrictMode
@JSONType(ignores = {"total_"})
public class AntdVueResult<T> implements IApiData<T> {
    @OpenApiSchema
    @OpenApiDescription("成功与否")
    private Boolean success;

    @OpenApiSchema
    @OpenApiDescription("消息")
    private String message;

    @OpenApiIgnore
    @JsonIgnore
    private Long total_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("总记录数")
    private String total;

    @OpenApiSchema(OpenApiSchemaType.model)
    @OpenApiDescription("数据集合")
    private List<T> data;

    /**
     * 成功与否
     */
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 消息
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
     * 数据
     */
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
