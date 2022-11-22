package project.extension.office.excel;

/**
 * 列信息
 *
 * @author LCTR
 * @date 2022-05-09
 */
public class ColumnInfo {
    /**
     * 名称
     */
    private String name;

    /**
     * 指定列宽
     * <p>单位：字符数量</p>
     */
    private Integer width;

    /**
     * 提示信息
     */
    private PromptInfo prompt;

    /**
     * 下拉选择框数据
     */
    private String[] dropdownSelect;

    /**
     * 列的列头单元格样式
     */
    private CellStyleInfo headerStyles;

    /**
     * 列的数据单元格样式
     */
    private CellStyleInfo dataStyles;

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     */
    public ColumnInfo setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 指定列宽
     * <p>单位：字符数量</p>
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * 指定列宽
     * <p>单位：字符数量</p>
     */
    public ColumnInfo setWidth(Integer width) {
        this.width = width;
        return this;
    }

    /**
     * 提示信息
     */
    public PromptInfo getPrompt() {
        return prompt;
    }

    /**
     * 提示信息
     */
    public ColumnInfo setPrompt(PromptInfo prompt) {
        this.prompt = prompt;
        return this;
    }

    /**
     * 下拉选择框数据
     */
    public String[] getDropdownSelect() {
        return dropdownSelect;
    }

    /**
     * 下拉选择框数据
     */
    public ColumnInfo setDropdownSelect(String[] dropdownSelect) {
        this.dropdownSelect = dropdownSelect;
        return this;
    }

    /**
     * 列头单元格样式
     */
    public CellStyleInfo getHeaderStyle() {
        return headerStyles;
    }

    /**
     * 列头单元格样式
     *
     * @param style 样式
     */
    public ColumnInfo setHeaderStyle(CellStyleInfo style) {
        headerStyles = style;
        return this;
    }

    /**
     * 数据单元格样式
     */
    public CellStyleInfo getDataStyle() {
        return dataStyles;
    }

    /**
     * 数据单元格样式
     *
     * @param style 样式
     */
    public ColumnInfo setDataStyle(CellStyleInfo style) {
        dataStyles = style;
        return this;
    }
}
