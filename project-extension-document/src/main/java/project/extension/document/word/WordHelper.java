package project.extension.document.word;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * Word帮助类
 *
 * @author LCTR
 * @date 2022-07-12
 */
public class WordHelper {
    /**
     * 转换工具
     */
    public enum ConvertTool {
        /**
         * 使用Spire-Doc
         * <p>免费试用，去水印解锁功能需在官网购买 https://www.e-iceblue.cn/Buy/Spire-Doc-JAVA.html#</p>
         */
        SpireDoc,
        /**
         * 通过调用系统中安装的MS Office程序来转换文件
         * <p>只支持Windows系统</p>
         * <p>需在系统中安装MS Office 2007或以上版本</p>
         * <p>Win2008以下需要创建目录C:\Windows\System32\config\systemprofile\Desktop</p>
         * <p>Win2008及以上需要创建目录C:\Windows\SysWOW64\config\systemprofile\Desktop</p>
         */
        Jacob
    }

    /**
     * Word转PDF
     *
     * @param wordFilename word文件完整路径
     * @param pdfFilename  pdf输出文件完整路径
     * @param convertTool  转换工具
     */
    public static void convert2Pdf(String wordFilename,
                                   String pdfFilename,
                                   ConvertTool convertTool)
            throws
            Exception {
        switch (convertTool) {
            case Jacob:
                convert2AnyByJacob(wordFilename,
                                   pdfFilename);
                break;
            case SpireDoc:
                try (FileInputStream fileInputStream = new FileInputStream(wordFilename)) {
                    try (FileOutputStream pdfOutputStream = new FileOutputStream(pdfFilename)) {
                        convert2PdfBySpireDoc(fileInputStream,
                                              pdfOutputStream,
                                              wordFilename.toLowerCase(Locale.ROOT)
                                                          .endsWith(".doc"));
                    }
                }
                break;
        }
    }

    /**
     * Word转PDF
     *
     * @param wordFilename word文件完整路径
     * @param outputStream 输出流
     */
    public static void convert2PdfBySpireDoc(String wordFilename,
                                             OutputStream outputStream)
            throws
            Exception {
        try (FileInputStream is = new FileInputStream(wordFilename)) {
            convert2PdfBySpireDoc(is,
                                  outputStream,
                                  wordFilename.toLowerCase(Locale.ROOT)
                                              .endsWith(".doc"));
        }
    }

    /**
     * Word转PDF
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param doc          是否为doc文件
     */
    public static void convert2PdfBySpireDoc(InputStream inputStream,
                                             OutputStream outputStream,
                                             boolean doc)
            throws
            Exception {
        //读取word文档
        XWPFDocument document;
        //如果是doc文件，则需要先转换为docx
        if (doc) {
            try (PipedInputStream in = new PipedInputStream()) {
                try (PipedOutputStream docxOutputStream = new PipedOutputStream(in)) {
                    CompletableFuture<Void> task
                            = CompletableFuture.runAsync(() -> convert2DocxBySpireDoc(inputStream,
                                                                                      docxOutputStream));
                    document = new XWPFDocument(in);
                }
            }
        } else
            document = new XWPFDocument(inputStream);

        //设置默认字体
        CTFonts default_Fonts = CTFonts.Factory.newInstance();
        default_Fonts.setEastAsia("Times New Roman");
        default_Fonts.setHAnsi("等线");

        changeDocumentFonts(document,
                            default_Fonts);

        //转换为pdf
        PdfOptions options = PdfOptions.create();
        PdfConverter.getInstance()
                    .convert(document,
                             outputStream,
                             options);
    }

    /**
     * doc转为docx
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     */
    public static void convert2DocxBySpireDoc(InputStream inputStream,
                                              OutputStream outputStream) {
        Document document = new Document(inputStream);
        document.saveToFile(outputStream,
                            FileFormat.Docx_2013);
    }

    /**
     * Word转换为其他格式的文件
     *
     * @param wordFilename word文件完整路径
     * @param filename     输出文件完整路径
     */
    public static void convert2AnyByJacob(String wordFilename,
                                          String filename) {
        ActiveXComponent app = null;
        try {
            //调用office程序
            app = new ActiveXComponent("Word.Application");
            //隐藏运行窗口
            app.setProperty("Visible",
                            false);
            //获得所有打开的文档
            Dispatch docs = app.getProperty("Documents")
                               .toDispatch();
            Dispatch doc = Dispatch.call(docs,
                                         "Open",
                                         wordFilename)
                                   .toDispatch();
            //另存为，将文档保存为pdf，其中Word保存为pdf的格式宏的值是17
            Dispatch.call(doc,
                          "SaveAs",
                          filename,
                          17);
            Dispatch.call(doc,
                          "Close");
        } finally {
            if (app != null)
                app.invoke("Quit",
                           0);
        }
    }

    /**
     * 更改文档的字体
     *
     * @param document 文档
     * @param fonts    指定字体
     */
    private static void changeDocumentFonts(XWPFDocument document,
                                            CTFonts fonts) {
        XWPFStyles styles = document.createStyles();
        styles.setDefaultFonts(fonts);

        changeDocumentParagraphsFonts(document,
                                      fonts);
    }

    /**
     * 更改文档中所有段落的字体
     *
     * @param document 文档
     * @param fonts    指定字体
     */
    private static void changeDocumentParagraphsFonts(XWPFDocument document,
                                                      CTFonts fonts) {
        //更改所有页头中的段落的字体
        changeHeaderParagraphsFonts(document.getHeaderList(),
                                    fonts);

        //更改所有段落的字体
        changeParagraphsFonts(document.getParagraphs(),
                              fonts);

        //更改所有表格中的段落的字体
        changeTableParagraphsFonts(document.getTables(),
                                   fonts);

        //更改所有页尾中的段落的字体
        changeFooterParagraphsFonts(document.getFooterList(),
                                    fonts);
    }

    /**
     * 更改页头中段落的字体
     *
     * @param headerList 页头集合
     * @param fonts      指定字体
     */
    private static void changeHeaderParagraphsFonts(List<XWPFHeader> headerList,
                                                    CTFonts fonts) {
        if (headerList == null)
            return;

        for (XWPFHeader header : headerList) {
            changeParagraphsFonts(header.getParagraphs(),
                                  fonts);
        }
    }

    /**
     * 更改页尾中段落的字体
     *
     * @param footerList 页尾集合
     * @param fonts      指定字体
     */
    private static void changeFooterParagraphsFonts(List<XWPFFooter> footerList,
                                                    CTFonts fonts) {
        if (footerList == null)
            return;

        for (XWPFFooter footer : footerList) {
            changeParagraphsFonts(footer.getParagraphs(),
                                  fonts);
        }
    }

    /**
     * 更改表格中段落的字体
     *
     * @param tableList 表格集合
     * @param fonts     指定字体
     */
    private static void changeTableParagraphsFonts(List<XWPFTable> tableList,
                                                   CTFonts fonts) {
        if (tableList == null)
            return;
        for (XWPFTable table : tableList) {
            List<XWPFTableRow> rowList = table.getRows();
            if (rowList == null)
                continue;
            for (XWPFTableRow row : rowList) {
                List<XWPFTableCell> cellList = row.getTableCells();
                if (cellList == null)
                    continue;
                for (XWPFTableCell cell : cellList) {
                    changeParagraphsFonts(cell.getParagraphs(),
                                          fonts);

                    //单元格内部表格
                    changeTableParagraphsFonts(cell.getTables(),
                                               fonts);
                }
            }
        }
    }

    /**
     * 更改段落的字体
     *
     * @param paragraphList 段落集合
     * @param fonts         指定字体
     */
    private static void changeParagraphsFonts(List<XWPFParagraph> paragraphList,
                                              CTFonts fonts) {
        if (paragraphList == null)
            return;
        for (XWPFParagraph paragraph : paragraphList) {
            if (paragraph.getIndentationFirstLine() <= 0)
                paragraph.setIndentationFirstLine(1);

            if (paragraph.getIndentationLeft() <= 0)
                paragraph.setIndentationLeft(1);

            if (paragraph.getIndentationHanging() <= 0)
                paragraph.setIndentationHanging(1);

            if (paragraph.getSpacingBefore() <= 0)
                paragraph.setSpacingBefore(1);

            if (paragraph.getSpacingBeforeLines() <= 0)
                paragraph.setSpacingBeforeLines(1);

            if (paragraph.getSpacingAfter() <= 0)
                paragraph.setSpacingAfter(1);

            if (paragraph.getSpacingAfterLines() <= 0)
                paragraph.setSpacingAfterLines(1);

//            StringBuilder sb = new StringBuilder();

            List<XWPFRun> runs = paragraph.getRuns();
            if (runs == null)
                continue;
            for (XWPFRun run : runs) {
//                sb.append(run.getText(run.getTextPosition()));
                if (run.getCharacterSpacing() <= 0)
                    run.setCharacterSpacing(1);

                CTR ctr = run.getCTR();
                if (ctr == null)
                    continue;
                CTRPr ctrPr = ctr.getRPr();
                if (ctrPr == null)
                    continue;
                List<CTFonts> fontsList = ctrPr.getRFontsList();
                if (fontsList == null)
                    continue;
                for (CTFonts currentFonts : fontsList) {
                    //更改字体
                    currentFonts.set(fonts);
                }
            }

//            if (sb.length() > 0) {
//                for (int i = 1; i < paragraph.getRuns()
//                                             .size(); i++) {
//                    paragraph.removeRun(i);
//                }
//
//                paragraph.getRuns()
//                         .get(0)
//                         .setText(sb.toString());
//            }
        }
    }
}
