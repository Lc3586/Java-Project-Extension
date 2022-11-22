package project.extension.file;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件下载信息
 *
 * @author LCTR
 * @date 2022-04-21
 */
public class FileDownloadInfo {
    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 字节数
     */
    private Long length;

    /**
     * 完整文件名
     */
    private String fullName;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件拓展名
     */
    private String extension;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 响应头信息
     */
    private Map<String, List<String>> HeaderFields;

    /**
     * 输入流
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 字节数
     */
    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    /**
     * 完整文件名
     */
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 文件名
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 文件拓展名
     */
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * 内容类型
     */
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 响应头信息
     */
    public Map<String, List<String>> getHeaderFields() {
        return HeaderFields;
    }

    public void setHeaderFields(Map<String, List<String>> headerFields) {
        HeaderFields = headerFields;
    }
}
