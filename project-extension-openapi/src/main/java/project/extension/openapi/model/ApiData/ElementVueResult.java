package project.extension.openapi.model.ApiData;

import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.openapi.model.OpenApiSchemaType;

/**
 * ElementVue数据结构方案
 *
 * @author LCTR
 * @date 2022-03-25
 */
@OpenApiSchemaStrictMode
public class ElementVueResult<T> implements IApiData<T> {
    @OpenApiSchema
    @OpenApiDescription("成功与否")
    private Boolean success;

    @OpenApiSchema
    @OpenApiDescription("异常代码")
    private Integer errorCode;

    @OpenApiSchema
    @OpenApiDescription("消息")
    private String message;

    @OpenApiSchema(OpenApiSchemaType.model)
    @OpenApiDescription("数据")
    private ElementVueResultData<T> data;

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
     * 异常代码
     * <p></p>
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
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
     * 数据
     */
    public ElementVueResultData<T> getData() {
        return data;
    }

    public void setData(ElementVueResultData<T> data) {
        this.data = data;
    }
}
