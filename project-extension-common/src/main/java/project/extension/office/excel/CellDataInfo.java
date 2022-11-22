package project.extension.office.excel;

/**
 * 单元格数据信息
 *
 * @author LCTR
 * @date 2022-05-09
 */
public class CellDataInfo {
    /**
     * 值
     */
    private Object value;

    /**
     * 类型
     */
    private CellDataType type;

    /**
     * 值
     */
    public Object getValue() {
        return value;
    }

    /**
     * 值
     */
    public CellDataInfo setValue(Object value) {
        this.value = value;
        return this;
    }

    /**
     * 类型
     */
    public CellDataType getType() {
        return type;
    }

    /**
     * 类型
     */
    public CellDataInfo setType(CellDataType type) {
        this.type = type;
        return this;
    }
}
