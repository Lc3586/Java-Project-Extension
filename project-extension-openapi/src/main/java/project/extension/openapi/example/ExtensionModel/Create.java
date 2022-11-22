package project.extension.openapi.example.ExtensionModel;

import project.extension.openapi.annotations.OpenApiMainTag;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.example.BaseModel;
import project.extension.openapi.model.OpenApiSchemaFormat;
import project.extension.openapi.model.OpenApiSchemaType;

/**
 * 示例代码 新增模型
 *
 * @author LCTR
 * @date 2022-03-18
 */
@OpenApiMainTag("Create")
public class Create extends BaseModel {
    @OpenApiSchema(type = OpenApiSchemaType.integer, format = OpenApiSchemaFormat.integer_int64)
    private String maxLong_;

    /**
     * long类型
     * <P>由于前端不支持64位整型数字，所以在和前端交互时需要转行为string类型。</P>
     */
    public String getMaxLong_() {
        return Long.toString(getMaxLong());
    }

    public void setMaxLong_(String maxLong_) {
        this.setMaxLong(Long.parseLong(maxLong_));
    }
}