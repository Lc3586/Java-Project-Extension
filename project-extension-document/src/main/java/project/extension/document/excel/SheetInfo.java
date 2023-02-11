package project.extension.document.excel;

import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工作表信息
 *
 * @author LCTR
 * @date 2022-05-09
 */
public class SheetInfo {
    /**
     * 工作表名称
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 全部列
     */
    private final List<ColumnInfo> columns = new ArrayList<>();

    /**
     * 全部数据行
     */
    private final List<Map<Integer, CellDataInfo>> rows = new ArrayList<>();

    /**
     * 标题单元格样式
     */
    private CellStyleInfo titleStyles;

    /**
     * 默认的标题单元格样式
     * <p>对齐方式：水平居中，垂直居中</p>
     * <p>字体：Arial 16 加粗</p>
     */
    private static CellStyleInfo defaultTitleStyle() {
        return new CellStyleInfo()
                .setStyle(style -> {
                    style.setAlignment(HorizontalAlignment.CENTER);
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                })
                .setFontStyle(font -> {
                    font.setFontName("Arial");
                    font.setFontHeightInPoints((short) 16);
                    font.setBold(true);
                });
    }

    /**
     * 默认的列头单元格样式
     * <p>对齐方式：水平居中，垂直居中，半透明灰色完全填充背景</p>
     * <p>字体：Arial 10 白色 加粗</p>
     */
    private static CellStyleInfo defaultHeaderStyle() {
        return new CellStyleInfo()
                .setStyle(style -> {
                    style.setAlignment(HorizontalAlignment.CENTER);
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                    style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                })
                .setFontStyle(font -> {
                    font.setFontName("Arial");
                    font.setFontHeightInPoints((short) 10);
                    font.setBold(true);
                    font.setColor(IndexedColors.WHITE.index);
                });
    }

    /**
     * 默认的数据单元格样式
     * <p>对齐方式：水平居中，垂直居中</p>
     * <p>边框：上、右、下、左 皆为半透明灰色细线</p>
     * <p>字体：Arial 10</p>
     */
    private static CellStyleInfo defaultDataStyle() {
        return new CellStyleInfo()
                .setStyle(style -> {
                    style.setAlignment(HorizontalAlignment.LEFT);
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                    style.setBorderTop(BorderStyle.THIN);
                    style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);

                    style.setBorderRight(BorderStyle.THIN);
                    style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);

                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);

                    style.setBorderLeft(BorderStyle.THIN);
                    style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
                })
                .setFontStyle(font -> {
                    font.setFontName("Arial");
                    font.setFontHeightInPoints((short) 10);
                });
    }

    /**
     * 工作表名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置工作表名称
     *
     * @param name 名称
     */
    public SheetInfo setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 工作表标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置工作表标题
     *
     * @param title 标题
     */
    public SheetInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 总列数
     */
    public int columnCount() {
        return columns.size();
    }

    /**
     * 新增列
     *
     * @param column 列
     */
    public SheetInfo addColumn(ColumnInfo column) {
        columns.add(column);
        return this;
    }

    /**
     * 移除列
     *
     * @param column 列
     */
    public SheetInfo removeColumn(ColumnInfo column) {
        columns.remove(column);
        return this;
    }

    /**
     * 获取列名
     *
     * @param index 索引
     */
    public ColumnInfo getColumn(int index) {
        return columns.get(index);
    }

    /**
     * 总行数
     */
    public int rowCount() {
        return rows.size();
    }

    /**
     * 新增一行数据
     *
     * @param row 数据
     */
    public SheetInfo addRow(Map<Integer, CellDataInfo> row) {
        rows.add(row);
        return this;
    }

    /**
     * 获取一行数据
     *
     * @param index 索引
     */
    public Map<Integer, CellDataInfo> getRow(int index) {
        return rows.get(index);
    }

    /**
     * 设置单元格数据
     *
     * @param index  索引
     * @param column 列
     * @param data   数据
     */
    public SheetInfo setData(int index,
                             int column,
                             CellDataInfo data) {
        rows.get(index)
            .put(column,
                 data);
        return this;
    }

    /**
     * 获取单元格数据
     *
     * @param index  索引
     * @param column 列
     */
    public CellDataInfo getData(int index,
                                int column) {
        return rows.get(index)
                   .get(column);
    }

    /**
     * 设置标题单元格样式
     *
     * @param style 样式
     */
    public SheetInfo setTitleStyle(CellStyleInfo style) {
        titleStyles = style;
        return this;
    }

    /**
     * 设置为默认的标题单元格样式
     */
    public SheetInfo setDefaultTitleStyle() {
        titleStyles = defaultTitleStyle();
        return this;
    }

    /**
     * 获取标题单元格样式
     */
    public CellStyleInfo getTitleStyle() {
        return titleStyles;
    }

    /**
     * 设置所有列的列头单元格样式
     *
     * @param style 样式
     */
    public SheetInfo setHeaderStyle(CellStyleInfo style) {
        for (ColumnInfo column : columns)
            column.setHeaderStyle(style);
        return this;
    }

    /**
     * 所有列设置为默认的列头单元格样式
     */
    public SheetInfo setDefaultHeaderStyle() {
        for (ColumnInfo column : columns)
            column.setHeaderStyle(defaultHeaderStyle());
        return this;
    }

    /**
     * 设置所有列的数据单元格样式
     *
     * @param style 样式
     */
    public SheetInfo setDataStyle(CellStyleInfo style) {
        for (ColumnInfo column : columns)
            column.setDataStyle(style);
        return this;
    }

    /**
     * 所有列设置为默认的数据单元格样式
     */
    public SheetInfo setDefaultDataStyle() {
        for (ColumnInfo column : columns)
            column.setDataStyle(defaultDataStyle());
        return this;
    }
}
