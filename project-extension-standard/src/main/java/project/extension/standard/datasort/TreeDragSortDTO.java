package project.extension.standard.datasort;

import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.annotations.OpenApiSchemaStrictMode;

/**
 * 树状数据拖动排序参数
 *
 * @author LCTR
 * @date 2022-12-08
 */
@OpenApiSchemaStrictMode
public class TreeDragSortDTO<TKey> extends DragSortDTO<TKey> {
    @OpenApiDescription("是否移至目标内部（默认值 false）")
    @OpenApiSchema("false")
    private Boolean inside = false;

    /**
     * 是否移至目标内部
     * <p>默认值 false</p>
     */
    public Boolean getInside() {
        return inside;
    }

    public void setInside(Boolean inside) {
        this.inside = inside;
    }
}
