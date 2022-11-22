package project.extension.office.excel;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.file.FileExtension;
import project.extension.file.PathExtension;
import project.extension.string.StringExtension;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Excel帮助类
 *
 * @author LCTR
 * @date 2022-05-09
 */
public class ExcelHelper {
    /**
     * 导出
     *
     * @param sheet    工作簿数据
     * @param filename 文件完整路径
     */
    public static void export(SheetInfo sheet,
                              String filename)
            throws
            Exception {
        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            export(Collections.singletonList(sheet),
                   ExcelVersion.valueOf(PathExtension.getExtension(filename)
                                                     .substring(1)),
                   outputStream);
        }
    }

    /**
     * 导出
     *
     * @param sheet        工作簿数据
     * @param version      版本
     * @param outputStream 输出流
     */
    public static void export(SheetInfo sheet,
                              ExcelVersion version,
                              OutputStream outputStream)
            throws
            Exception {
        export(Collections.singletonList(sheet),
               version,
               outputStream);
    }

    /**
     * 导出
     *
     * @param sheets       工作簿数据
     * @param version      版本
     * @param outputStream 输出流
     */
    public static void export(List<SheetInfo> sheets,
                              ExcelVersion version,
                              OutputStream outputStream)
            throws
            Exception {
        //创建工作簿
        Workbook workbook = version.equals(ExcelVersion.xls)
                            ? new HSSFWorkbook()
                            : new SXSSFWorkbook();

        for (SheetInfo sheetInfo : sheets) {
            //创建工作簿
            Sheet sheet = workbook.createSheet(sheetInfo.getName()
                                                        .replaceAll("/",
                                                                    " "));

            //当前处理的行索引
            int rowIndex = 0;

            if (StringUtils.hasText(sheetInfo.getTitle())) {
                //创建标题行
                Row titleRow = sheet.createRow(rowIndex++);
                titleRow.setHeightInPoints(30);
                Cell titleCell = titleRow.createCell(0);
                //单元格样式
                if (sheetInfo.getTitleStyle() != null) titleCell.setCellStyle(getCellStyle(workbook,
                                                                                           sheetInfo.getTitleStyle()));
                //单元格值
                titleCell.setCellValue(sheetInfo.getTitle());
                //合并单元格
                sheet.addMergedRegion(new CellRangeAddress(0,
                                                           0,
                                                           0,
                                                           sheetInfo.columnCount() - 1));
            }

            //数据单元格样式
            Map<Integer, CellStyle> dataStyles = new HashMap<>();

            //列头
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < sheetInfo.columnCount(); i++) {
                ColumnInfo columnInfo = sheetInfo.getColumn(i);
                Cell headerCell = headerRow.createCell(i);
                //单元格样式
                if (columnInfo.getHeaderStyle() != null) headerCell.setCellStyle(getCellStyle(workbook,
                                                                                              columnInfo.getHeaderStyle()));
                //单元格值
                headerCell.setCellValue(columnInfo.getName());

                //提示信息
                if (columnInfo.getPrompt() != null) setPrompt(sheet,
                                                              columnInfo.getPrompt(),
                                                              rowIndex - 1,
                                                              Integer.MAX_VALUE,
                                                              i,
                                                              i);

                //下拉选择框
                if (CollectionsExtension.anyPlus(columnInfo.getDropdownSelect())) setDropdownSelect(sheet,
                                                                                                    columnInfo.getDropdownSelect(),
                                                                                                    rowIndex - 1,
                                                                                                    Integer.MAX_VALUE,
                                                                                                    i,
                                                                                                    i);

                //计算列宽
                int width = columnInfo.getWidth() != null && columnInfo.getWidth() > 0
                            ? calcWidth(columnInfo.getWidth())
                            : calcWidth(columnInfo.getName());
                sheet.setColumnWidth(i,
                                     width);

                //数据单元格样式
                if (columnInfo.getDataStyle() != null) dataStyles.put(i,
                                                                      getCellStyle(workbook,
                                                                                   columnInfo.getDataStyle()));
            }

            //数据
            //遍历行
            for (int i = 0; i < sheetInfo.rowCount(); i++) {
                Row dataRow = sheet.createRow(rowIndex++);
                //遍历列
                for (int j = 0; j < sheetInfo.columnCount(); j++) {
                    Cell dataCell = dataRow.createCell(j);
                    if (dataStyles.containsKey(j)) dataCell.setCellStyle(dataStyles.get(j));
                    //单元格的值
                    setData(dataCell,
                            sheetInfo.getData(i,
                                              j));
                }
            }
        }

        //输出数据
        workbook.write(outputStream);
    }

    /**
     * 读取excel数据
     *
     * @param filename        文件完整路径
     * @param firstRowIsTitle 第一行为标题
     * @param hasColumnName   是否有列名
     * @return 工作表数据集合
     */
    public static List<SheetInfo> read(String filename,
                                       boolean firstRowIsTitle,
                                       boolean hasColumnName)
            throws
            Exception {
        try (InputStream inputStream = new FileInputStream(filename)) {
            return read(inputStream,
                        firstRowIsTitle,
                        hasColumnName);
        }
    }

    /**
     * 读取excel数据
     *
     * @param inputStream     输入流
     * @param firstRowIsTitle 第一行为标题
     * @param hasColumnName   是否有列名
     * @return 工作表数据集合
     */
    public static List<SheetInfo> read(InputStream inputStream,
                                       boolean firstRowIsTitle,
                                       boolean hasColumnName)
            throws
            Exception {
        List<SheetInfo> sheetInfoList = new ArrayList<>();
        //读取并创建工作簿
        Workbook workbook = WorkbookFactory.create(inputStream);
        //工作表迭代器
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            SheetInfo sheetInfo = new SheetInfo();
            //工作表
            Sheet sheet = sheetIterator.next();
            sheetInfo.setName(sheet.getSheetName());

            //数据行迭代器
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                //数据行
                Row row = rowIterator.next();

                if (firstRowIsTitle) {
                    //获取标题
                    sheetInfo.setTitle(getData(row.getCell(0)).toString());
                    firstRowIsTitle = false;
                } else if (hasColumnName) {
                    //获取列名
                    for (int i = 0; i < row.getPhysicalNumberOfCells(); i++)
                        sheetInfo.addColumn(new ColumnInfo().setName(getData(row.getCell(i)).toString()));
                    //后续的行都是数据
                    hasColumnName = false;
                } else {
                    //获取数据
                    Map<Integer, CellDataInfo> rowInfo = new HashMap<>();
                    for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                        Cell cell = row.getCell(i);
                        rowInfo.put(i,
                                    new CellDataInfo().setType(convertType(cell.getCellType()))
                                                      .setValue(getData(cell)));
                    }
                    sheetInfo.addRow(rowInfo);
                }
            }
            sheetInfoList.add(sheetInfo);
        }

        return sheetInfoList;
    }

    /**
     * 计算列宽
     *
     * @param value 内容
     * @return 列宽
     */
    private static int calcWidth(String value) {
        int length = 0;
        for (int i = 0; i < value.length(); i++)
            length += StringExtension.isChinese(value.charAt(i))
                      ? 2
                      : 1;
        return calcWidth(length);
    }

    /**
     * 计算列宽
     *
     * @param length 字符数
     * @return 列宽
     */
    private static int calcWidth(int length) {
        return Math.min(length + 1,
                        255) * 256;
    }

    /**
     * 获取单元格样式
     *
     * @param workbook  工作簿
     * @param styleInfo 样式信息
     */
    private static CellStyle getCellStyle(Workbook workbook,
                                          CellStyleInfo styleInfo)
            throws
            Exception {
        CellStyle style = workbook.createCellStyle();
        if (styleInfo.getStyle() != null) styleInfo.getStyle()
                                                   .invoke(style);

        if (styleInfo.getFontStyle() != null) {
            Font font = workbook.createFont();
            styleInfo.getFontStyle()
                     .invoke(font);
            style.setFont(font);
        }
        return style;
    }

    /**
     * 为指定的单元格添加提示信息
     *
     * @param sheet    工作表
     * @param data     提示信息
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    private static void setPrompt(Sheet sheet,
                                  PromptInfo data,
                                  int firstRow,
                                  int lastRow,
                                  int firstCol,
                                  int lastCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        // 设置数据有效性加载在哪个单元格上
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                                                                lastRow,
                                                                firstCol,
                                                                lastCol);
        DataValidation dataValidation = helper.createValidation(constraint,
                                                                regions);
        dataValidation.createPromptBox(data.getTitle(),
                                       data.getContent());
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 为指定的单元格添加下拉选择框
     * <p>即这些单元格的值只能从下拉框中选定，不可手动输入</p>
     *
     * @param sheet    工作表
     * @param data     下拉选择框的数据
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    private static void setDropdownSelect(Sheet sheet,
                                          String[] data,
                                          int firstRow,
                                          int lastRow,
                                          int firstCol,
                                          int lastCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(data);
        // 设置数据有效性加载在哪个单元格上
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                                                                lastRow,
                                                                firstCol,
                                                                lastCol);
        //数据有效性对象
        DataValidation dataValidation = helper.createValidation(constraint,
                                                                regions);
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置数据
     *
     * @param cell 单元格
     * @param data 数据
     */
    private static void setData(Cell cell,
                                CellDataInfo data)
            throws
            Exception {
        switch (data.getType()) {
            default:
            case STRING:
                //字符串内容
                cell.setCellValue(data.getValue() == null
                                  ? ""
                                  : String.valueOf(data.getValue()));
                int currentWidth = cell.getSheet()
                                       .getColumnWidth(cell.getColumnIndex());
                int newWidth = calcWidth(cell.getStringCellValue());
                cell.getSheet()
                    .setColumnWidth(cell.getColumnIndex(),
                                    Math.max(currentWidth,
                                             newWidth));
                break;
            case NUMERIC:
                //数值类型
                if (data.getValue() == null) cell.setCellValue(0);
                else {
                    String str = data.getValue()
                                     .toString();
                    if (str.indexOf(".") > 0) cell.setCellValue(new BigDecimal(str).doubleValue());
                    else cell.setCellValue(new Long(str));
                }
                break;
            case FORMULA:
                //公式
                cell.setCellFormula(String.valueOf(data.getValue()));
                break;
            case BOOLEAN:
                //布尔值
                cell.setCellValue((Boolean) data.getValue());
                break;
            case ERROR:
                //错误值
                cell.setCellErrorValue((Byte) data.getValue());
                break;
            case IMAGE:
                //图像
                ClientAnchor anchor = new XSSFClientAnchor(0,
                                                           0,
                                                           0,
                                                           0,
                                                           (short) cell.getColumnIndex(),
                                                           cell.getRow()
                                                               .getRowNum(),
                                                           (short) (cell.getColumnIndex() + 1),
                                                           cell.getRow()
                                                               .getRowNum() + 1);
                File image = new File(String.valueOf(data.getValue()));
                if (image.exists()) {
                    byte[] buffer = FileExtension.readAllByte(image);
                    (cell.getSheet()
                         .getDrawingPatriarch() == null
                     ? cell.getSheet()
                           .createDrawingPatriarch()
                     : cell.getSheet()
                           .getDrawingPatriarch()).createPicture(anchor,
                                                                 cell.getSheet()
                                                                     .getWorkbook()
                                                                     .addPicture(buffer,
                                                                                 getImageType(PathExtension.getExtension(image.getName()))));
                }
                break;
            case _NONE:
            case BLANK:
                //空值
                cell.setBlank();
                break;
        }
    }

    /**
     * 获取图片类型
     *
     * @param extension 文件拓展名
     * @return 图片类型
     */
    private static int getImageType(String extension) {
        if (extension.equalsIgnoreCase(".jpg")) return Workbook.PICTURE_TYPE_JPEG;
        else if (extension.equalsIgnoreCase(".png")) return Workbook.PICTURE_TYPE_PNG;
        else if (extension.equalsIgnoreCase(".emf")) return Workbook.PICTURE_TYPE_EMF;
        else if (extension.equalsIgnoreCase(".wmf")) return Workbook.PICTURE_TYPE_WMF;
        else if (extension.equalsIgnoreCase(".pict")) return Workbook.PICTURE_TYPE_PICT;
        else if (extension.equalsIgnoreCase(".dib")) return Workbook.PICTURE_TYPE_DIB;
        else return Workbook.PICTURE_TYPE_JPEG;
    }

    /**
     * 转行类型
     *
     * @param cellType 单元格类型
     * @return 单元格数据类型
     */
    private static CellDataType convertType(CellType cellType) {
        return CellDataType.valueOf(cellType.name());
    }

    /**
     * 获取单元格的值
     *
     * @param cell 单元格
     * @return 单元格的值
     */
    private static Object getData(Cell cell) {
        if (cell == null)
            return null;

        switch (cell.getCellType()) {
            default:
            case STRING:
                //字符串内容
                return cell.getStringCellValue();
            case NUMERIC:
                //数值类型
                return cell.getNumericCellValue();
            case FORMULA:
                //公式
                return cell.getCellFormula();
            case BOOLEAN:
                //布尔值
                return cell.getBooleanCellValue();
            case ERROR:
                //错误值
                return cell.getErrorCellValue();
            case _NONE:
            case BLANK:
                //空值
                return "";
        }
    }

    /**
     * Excel转PDF
     *
     * @param excelFilename Excel文件完整路径
     * @param pdfFilename   输出流
     */
    public static void convert2Pdf(String excelFilename,
                                   String pdfFilename)
            throws
            Exception {
        try (FileOutputStream pdfOutputStream = new FileOutputStream(pdfFilename)) {
            convert2Pdf(new FileInputStream(excelFilename),
                        pdfOutputStream,
                        excelFilename.toLowerCase(Locale.ROOT)
                                     .endsWith(".xlsx"));
        }
    }

    /**
     * Excel转PDF
     *
     * @param excelFilename Excel文件完整路径
     * @param outputStream  输出流
     */
    public static void convert2Pdf(String excelFilename,
                                   OutputStream outputStream)
            throws
            Exception {
        try (FileInputStream excelInputStream = new FileInputStream(excelFilename)) {
            convert2Pdf(excelInputStream,
                        outputStream,
                        excelFilename.toLowerCase(Locale.ROOT)
                                     .endsWith(".xlsx"));
        }
    }

    /**
     * Excel转PDF
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param xlsx         是否为xlsx文件
     */
    public static void convert2Pdf(InputStream inputStream,
                                   OutputStream outputStream,
                                   boolean xlsx)
            throws
            Exception {
        //读取并创建工作簿
        HSSFWorkbook workbook;
        //如果是xlsx文件，则需要先转换为xls
        if (xlsx) {
            try (PipedInputStream in = new PipedInputStream()) {
                try (PipedOutputStream xlsOutputStream = new PipedOutputStream(in)) {
                    CompletableFuture<Void> task
                            = CompletableFuture.runAsync(() -> convert2Xls(inputStream,
                                                                           xlsOutputStream));
                    workbook = new HSSFWorkbook(in);
                }
            }
        } else
            workbook = new HSSFWorkbook(inputStream);

        //工作表迭代器
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            //工作表
            Sheet sheet = sheetIterator.next();
        }

//        XWPFStyles styles = document.createStyles();
//
//        CTFonts fonts = CTFonts.Factory.newInstance();
//        fonts.setEastAsia("Times New Roman");
//        fonts.setHAnsi("等线");
//        styles.setDefaultFonts(fonts);
//
//        //转换为pdf
//        PdfOptions options = PdfOptions.create();
//        PdfConverter.getInstance()
//                    .convert(document,
//                             outputStream,
//                             options);
    }

    /**
     * xlsx转为xls
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     */
    public static void convert2Xls(InputStream inputStream,
                                   OutputStream outputStream) {
//        Transform transform = new Transform();
        Document document = new Document(inputStream);
        document.saveToFile(outputStream,
                            FileFormat.Docx_2013);
    }
}
