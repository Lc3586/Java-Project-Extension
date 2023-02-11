package project.extension.document.excel;

import org.apache.poi.ss.usermodel.*;
import project.extension.action.IAction1;

/**
 * 单元格样式信息
 *
 * @author LCTR
 * @date 2022-05-09
 */
public class CellStyleInfo {
    /**
     * 单元格样式
     */
    private IAction1<CellStyle> style;

    /**
     * 字体样式
     */
    private IAction1<Font> fontStyle;

    /**
     * 单元格样式
     */
    public IAction1<CellStyle> getStyle() {
        return style;
    }

    /**
     * 单元格样式
     */
    public CellStyleInfo setStyle(IAction1<CellStyle> style) {
        this.style = style;
        return this;
    }

    /**
     * 字体样式
     */
    public IAction1<Font> getFontStyle() {
        return fontStyle;
    }

    /**
     * 字体样式
     */
    public CellStyleInfo setFontStyle(IAction1<Font> fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }
}
