package project.extension.standard.api.request.datasort;

import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;

/**
 * 数据拖动排序参数
 *
 * @author LCTR
 * @date 2022-12-08
 */
@OpenApiSchemaStrictMode
public class DragSortDTO<TKey> {
    @OpenApiDescription("主键")
    private TKey id;

    @OpenApiDescription("目标主键")
    private TKey targetId;

    @OpenApiDescription("是否排在目标之后（默认值 true）")
    @OpenApiSchema("true")
    private Boolean append = true;

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
     * 目标主键
     */
    public TKey getTargetId() {
        return targetId;
    }

    public void setTargetId(TKey targetId) {
        this.targetId = targetId;
    }

    /**
     * 是否排在目标之后
     * <p>默认值 true</p>
     */
    public Boolean getAppend() {
        return append;
    }

    public void setAppend(Boolean append) {
        this.append = append;
    }
}
