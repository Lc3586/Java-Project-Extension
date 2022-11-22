package project.extension.file;

import javax.xml.crypto.Data;

/**
 * 流标签信息
 *
 * @author LCTR
 * @date 2022-04-08
 */
public class StreamTagsInfo {
    /**
     * 语言
     */
    private String language;

    /**
     * 处理器名字
     */
    private String handler_Name;

    /**
     * 创建时间
     */
    private Data creation_Time;

    /**
     * 语言
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 处理器名字
     */
    public String getHandler_Name() {
        return handler_Name;
    }

    public void setHandler_Name(String handler_Name) {
        this.handler_Name = handler_Name;
    }

    /**
     * 创建时间
     */
    public Data getCreation_Time() {
        return creation_Time;
    }

    public void setCreation_Time(Data creation_Time) {
        this.creation_Time = creation_Time;
    }
}
