package project.extension.file;

import java.math.BigDecimal;

/**
 * 格式信息
 *
 * @author LCTR
 * @date 2022-04-08
 */
public class FormatInfo {
    /**
     * 文件绝对路径
     */
    private String filename;

    /**
     * 输入视频的AVStream个数
     */
    private Integer nb_Streams;

    private Integer nb_Programs;

    /**
     * 格式名
     * <p>半角逗号[,]分隔</p>
     */
    private String format_Name;

    /**
     * 格式名全称
     */
    private String format_Long_Name;

    /**
     * 首帧时间
     */
    private Double start_Time;

    /**
     * 首帧时间
     */
    private String start_Time_Convert;

    /**
     * 时长(秒)
     */
    private Double duration;

    /**
     * 时长
     */
    private String duration_Convert;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 码率
     */
    private Long bit_Rate;

    /**
     * 文件内容与文件拓展名匹配程度
     * <p>100为最高分, 低于25分时文件拓展名可能被串改</p>
     */
    private Integer probe_Score;

    /**
     * 标签
     */
    private FormatTagsInfo tags;

    /**
     * 文件绝对路径
     */
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 输入视频的AVStream个数
     */
    public Integer getNb_Streams() {
        return nb_Streams;
    }

    public void setNb_Streams(Integer nb_Streams) {
        this.nb_Streams = nb_Streams;
    }

    public Integer getNb_Programs() {
        return nb_Programs;
    }

    public void setNb_Programs(Integer nb_Programs) {
        this.nb_Programs = nb_Programs;
    }

    /**
     * 格式名
     * <p>半角逗号[,]分隔</p>
     */
    public String getFormat_Name() {
        return format_Name;
    }

    public void setFormat_Name(String format_Name) {
        this.format_Name = format_Name;
    }

    /**
     * 格式名全称
     */
    public String getFormat_Long_Name() {
        return format_Long_Name;
    }

    public void setFormat_Long_Name(String format_Long_Name) {
        this.format_Long_Name = format_Long_Name;
    }

    /**
     * 首帧时间
     */
    public Double getStart_Time() {
        return start_Time;
    }

    public void setStart_Time(Double start_Time) {
        this.start_Time = start_Time;
        if (start_Time == null) start_Time_Convert = null;
        else
            start_Time_Convert = String.format("%s:%s:%s", (int) Math.floor(start_Time / 60 / 60), (int) Math.floor(start_Time / 60), Double.toString(start_Time).substring(2));
    }

    /**
     * 首帧时间
     */
    public String getStart_Time_Convert() {
        return start_Time_Convert;
    }

    public void setStart_Time_Convert(String start_Time_Convert) {
        this.start_Time_Convert = start_Time_Convert;
        if (start_Time_Convert == null) start_Time = null;
        else {
            String[] times = start_Time_Convert.split(":");
            if (times.length != 3) start_Time = null;
            else
                start_Time = (Double.parseDouble(times[0]) * 60 * 60) + (Double.parseDouble(times[1]) * 60) + Double.parseDouble(String.format("0.%s", times[2]));
        }
    }

    /**
     * 时长(秒)
     */
    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
        if (duration == null) duration_Convert = null;
        else
            duration_Convert = String.format("%s:%s:%s", (int) Math.floor(duration / 60 / 60), (int) Math.floor(duration / 60), Double.toString(duration).substring(2));
    }

    /**
     * 时长
     */
    public String getDuration_Convert() {
        return duration_Convert;
    }

    public void setDuration_Convert(String duration_Convert) {
        this.duration_Convert = duration_Convert;
        if (duration_Convert == null) duration = null;
        else {
            String[] times = duration_Convert.split(":");
            if (times.length != 3) duration = null;
            else
                duration = (Double.parseDouble(times[0]) * 60 * 60) + (Double.parseDouble(times[1]) * 60) + Double.parseDouble(String.format("0.%s", times[2]));
        }
    }

    /**
     * 文件大小
     */
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * 码率
     */
    public Long getBit_Rate() {
        return bit_Rate;
    }

    public void setBit_Rate(Long bit_Rate) {
        this.bit_Rate = bit_Rate;
    }

    /**
     * 文件内容与文件拓展名匹配程度
     * <p>100为最高分, 低于25分时文件拓展名可能被串改</p>
     */
    public Integer getProbe_Score() {
        return probe_Score;
    }

    public void setProbe_Score(Integer probe_Score) {
        this.probe_Score = probe_Score;
    }

    /**
     * 标签
     */
    public FormatTagsInfo getTags() {
        return tags;
    }

    public void setTags(FormatTagsInfo tags) {
        this.tags = tags;
    }
}
