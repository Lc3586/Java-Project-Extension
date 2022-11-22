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
 * Layui数据结构方案
 *
 * @author LCTR
 * @date 2022-03-25
 */
@OpenApiSchemaStrictMode
@JSONType(ignores = {"count_", "costtime_"})
public class LayuiResult<T> implements IApiData<T> {
    @OpenApiSchema
    @OpenApiDescription("状态码（成功 0，失败 -1）")
    private Integer code;

    @OpenApiSchema
    @OpenApiDescription("信息")
    private String msg;

    @OpenApiIgnore
    @JsonIgnore
    private Long count_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("总记录数")
    private String count;

    @OpenApiIgnore
    @JsonIgnore
    private Long costtime_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("耗时（毫秒）")
    private String costtime;

    @OpenApiSchema(OpenApiSchemaType.model)
    @OpenApiDescription("数据集合")
    private List<T> data;

    /**
     * 总记录数
     */
    public Long getCount_() {
        return count_;
    }

    public void setCount_(Long count_) {
        this.count_ = count_;
        this.count = null;
    }

    /**
     * 总记录数
     */
    public String getCount() {
        if (count == null)
            count = Long.toString(count_);
        return count;
    }

    public void setCount(String count) {
        this.count = count;
        this.count_ = Long.parseLong(count);
    }

    /**
     * 状态码
     * <p>成功 0，失败 -1</p>
     */
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 信息
     */
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
