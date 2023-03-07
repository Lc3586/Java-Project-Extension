package project.extension.standard.api.response.select;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 下拉选择框选项业务模型
 *
 * @author LCTR
 * @date 2022-07-04
 */
public class SelectOptionDTO<TDetail> {
    public SelectOptionDTO() {
        this(null,
             null,
             SelectOptionType.文本,
             null);
    }

    public SelectOptionDTO(String text,
                           Object value) {
        this(text,
             value,
             SelectOptionType.文本,
             null);
    }

    public SelectOptionDTO(String text,
                           Object value,
                           SelectOptionType type,
                           TDetail detail) {
        this.text = text;
        this.value = value;
        this.type = type;
        this.detail = detail;
    }

    /**
     * 显示文本
     */
    @OpenApiDescription("显示文本")
    private String text;

    /**
     * 值
     */
    @OpenApiDescription("值")
    private Object value;

    /**
     * 选项类型
     */
    @OpenApiDescription("选项类型")
    private SelectOptionType type;

    /**
     * 详情数据
     */
    @OpenApiDescription("详情数据")
    private TDetail detail;

    /**
     * 显示文本
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * 值
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 选项类型
     */
    public SelectOptionType getType() {
        return type;
    }

    public void setType(SelectOptionType type) {
        this.type = type;
    }

    /**
     * 详情数据
     */
    public TDetail getDetail() {
        return detail;
    }

    public void setDetail(TDetail detail) {
        this.detail = detail;
    }
}
