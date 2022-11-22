package project.extension.file;

/**
 * 视频文件信息
 *
 * @author LCTR
 * @date 2022-04-08
 */
public class VideoInfo {
    /**
     * 媒体流
     */
    public java.util.List<StreamInfo> Streams;

    /**
     * 格式
     */
    public FormatInfo Format;

    /**
     * 章节
     */
    public java.util.List<ChapterInfo> Chapters;

    /**
     * 程序
     */
    public java.util.List<ProgramInfo> Programs;

    /**
     * 程序版本
     */
    public ProgramVersionInfo Program_Version;

    /**
     * 库版本
     */
    public java.util.List<LibraryVersionInfo> Library_Versions;
}
