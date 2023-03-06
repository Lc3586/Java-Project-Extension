package project.extension.standard.api.request.datasort;

import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;
import project.extension.openapi.model.OpenApiSchemaType;

/**
 * 数据排序参数
 *
 * @author LCTR
 * @date 2022-12-08
 */
@OpenApiSchemaStrictMode
public class DataSortDTO<TKey> {
    @OpenApiDescription("主键")
    private TKey id;

    @OpenApiDescription("排序方法（默认值 UP）")
    @OpenApiSchema(value = "UP", type = OpenApiSchemaType.enum_)
    private SortMethod method = SortMethod.UP;

    @OpenApiDescription("跨度（表示移动几位，默认值 1）")
    @OpenApiSchema("1")
    private Integer span = 1;

    /**
     * 主键
     */
    public TKey getId() {
        return id;
    }

    public void setId(TKey id) {
        this.id = id;
    }

    /**
     * 排序方法
     * <p>默认 Up</p>
     */
    public SortMethod getMethod() {
        return method;
    }

    public void setMethod(SortMethod method) {
        this.method = method;
    }

    /**
     * 跨度
     * <p>表示移动几位</p>
     * <p>默认值 1</p>
     */
    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }
}
