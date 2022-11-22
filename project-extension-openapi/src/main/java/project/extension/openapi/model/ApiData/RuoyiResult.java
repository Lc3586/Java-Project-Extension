package project.extension.openapi.model.ApiData;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import project.extension.openapi.annotations.*;
import project.extension.openapi.model.OpenApiSchemaFormat;
import project.extension.openapi.model.OpenApiSchemaType;

import java.util.List;

/**
 * Ruoyi框架数据结构方案
 *
 * @author LCTR
 * @date 2022-03-25
 */
@OpenApiSchemaStrictMode
@JSONType(ignores = {"total_"})
public class RuoyiResult<T> implements IApiData<T> {
    @OpenApiIgnore
    @JsonIgnore
    private Long total_;

    @OpenApiSchema(value = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    @OpenApiDescription("总记录数")
    private String total;

    @OpenApiSchema
    @OpenApiDescription("状态码")
    private Integer code;

    @OpenApiSchema
    @OpenApiDescription("消息")
    private String msg;

    @OpenApiSchema(OpenApiSchemaType.model)
    @OpenApiDescription("数据集合")
    private List<T> rows;

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
     * 消息
     */
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
