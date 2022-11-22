package project.extension.file;

/**
 * 媒体流信息
 *
 * @author LCTR
 * @date 2022-04-08
 */
public class StreamInfo {
    /**
     * 索引
     */
    private Integer index;

    /**
     * 编码器名
     */
    private String codec_Name;

    /**
     * 编码器名全称
     */
    private String codec_Long_Name;

    /**
     * 简介
     */
    private String profile;

    /**
     * 编码器类型
     */
    private String codec_Type;

    /**
     * 编码器每帧时长
     */
    private String codec_Time_Base;

    /**
     * 编码器标签名
     */
    private String codec_Tag_String;

    /**
     * 编码器标签
     */
    private String codec_Tag;

    /**
     * 采样点格式
     */
    private String sample_Fmt;

    /**
     * 采样率
     */
    private Long sample_Rate;

    /**
     * 音频通道数
     */
    private Integer channels;

    /**
     * 音频通道布局
     */
    private String channel_Layout;

    /**
     * 采样点bit数
     */
    private Integer bits_Per_Sample;

    /**
     * 帧宽度
     */
    private Integer width;

    /**
     * 帧高度
     */
    private Integer height;

    /**
     * 视频帧宽度
     */
    private Integer coded_Width;

    /**
     * 视频帧高度
     */
    private Integer coded_Height;

    private Integer closed_Captions;

    /**
     * 记录帧缓存大小
     * <p>视频的延迟帧数</p>
     */
    private Integer has_B_Frames;

    /**
     * 像素格式
     */
    private String pix_Fmt;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 色度样品的位置
     */
    private String chroma_Location;

    /**
     * 参考帧数量
     */
    private Integer refs;

    private Boolean is_Avc;

    /**
     * 表示用几个字节表示NALU的长度
     */
    private Long nal_Length_Size;

    /**
     * 真实基础帧率
     */
    private String r_Frame_Rate;

    /**
     * 平均帧率
     */
    private String avg_Frame_Rate;

    /**
     * 每帧时长
     */
    private String time_Base;

    /**
     * 流开始时间
     * <p>基于time_Base</p>
     */
    private Integer start_Pts;

    /**
     * 首帧时间
     */
    private Double start_Time;

    /**
     * 首帧时间
     */
    private String start_Time_Convert;

    /**
     * 流时长
     * <p>基于time_Base</p>
     */
    private Integer duration_Ts;

    /**
     * 时长(秒)
     * <p>转换（duration_ts * time_base）之后的时长，单位秒</p>
     */
    private Double duration;

    /**
     * 时长
     */
    private String duration_Convert;

    /**
     * 码率
     */
    private Long bit_Rate;

    /**
     * 最大码率
     */
    private Long max_Bit_Rate;

    /**
     * 原生采样的比特数/位深
     */
    private Integer bits_Per_Raw_Sample;

    /**
     * 视频流帧数
     */
    private Long nb_Frames;

    private String extradata;

    /**
     * 配置
     */
    private DispositionInfo disposition;

    /**
     * 标签
     */
    private StreamTagsInfo tags;

    /**
     * 索引
     */
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * 编码器名
     */
    public String getCodec_Name() {
        return codec_Name;
    }

    public void setCodec_Name(String codec_Name) {
        this.codec_Name = codec_Name;
    }

    /**
     * 编码器名全称
     */
    public String getCodec_Long_Name() {
        return codec_Long_Name;
    }

    public void setCodec_Long_Name(String codec_Long_Name) {
        this.codec_Long_Name = codec_Long_Name;
    }

    /**
     * 简介
     */
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 编码器类型
     */
    public String getCodec_Type() {
        return codec_Type;
    }

    public void setCodec_Type(String codec_Type) {
        this.codec_Type = codec_Type;
    }

    /**
     * 编码器每帧时长
     */
    public String getCodec_Time_Base() {
        return codec_Time_Base;
    }

    public void setCodec_Time_Base(String codec_Time_Base) {
        this.codec_Time_Base = codec_Time_Base;
    }

    /**
     * 编码器标签名
     */
    public String getCodec_Tag_String() {
        return codec_Tag_String;
    }

    public void setCodec_Tag_String(String codec_Tag_String) {
        this.codec_Tag_String = codec_Tag_String;
    }

    /**
     * 编码器标签
     */
    public String getCodec_Tag() {
        return codec_Tag;
    }

    public void setCodec_Tag(String codec_Tag) {
        this.codec_Tag = codec_Tag;
    }

    /**
     * 采样点格式
     */
    public String getSample_Fmt() {
        return sample_Fmt;
    }

    public void setSample_Fmt(String sample_Fmt) {
        this.sample_Fmt = sample_Fmt;
    }

    /**
     * 采样率
     */
    public Long getSample_Rate() {
        return sample_Rate;
    }

    public void setSample_Rate(Long sample_Rate) {
        this.sample_Rate = sample_Rate;
    }

    /**
     * 音频通道数
     */
    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    /**
     * 音频通道布局
     */
    public String getChannel_Layout() {
        return channel_Layout;
    }

    public void setChannel_Layout(String channel_Layout) {
        this.channel_Layout = channel_Layout;
    }

    /**
     * 采样点bit数
     */
    public Integer getBits_Per_Sample() {
        return bits_Per_Sample;
    }

    public void setBits_Per_Sample(Integer bits_Per_Sample) {
        this.bits_Per_Sample = bits_Per_Sample;
    }

    /**
     * 帧宽度
     */
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 帧高度
     */
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 视频帧宽度
     */
    public Integer getCoded_Width() {
        return coded_Width;
    }

    public void setCoded_Width(Integer coded_Width) {
        this.coded_Width = coded_Width;
    }

    /**
     * 视频帧高度
     */
    public Integer getCoded_Height() {
        return coded_Height;
    }

    public void setCoded_Height(Integer coded_Height) {
        this.coded_Height = coded_Height;
    }

    public Integer getClosed_Captions() {
        return closed_Captions;
    }

    public void setClosed_Captions(Integer closed_Captions) {
        this.closed_Captions = closed_Captions;
    }

    /**
     * 记录帧缓存大小
     * <p>视频的延迟帧数</p>
     */
    public Integer getHas_B_Frames() {
        return has_B_Frames;
    }

    public void setHas_B_Frames(Integer has_B_Frames) {
        this.has_B_Frames = has_B_Frames;
    }

    /**
     * 像素格式
     */
    public String getPix_Fmt() {
        return pix_Fmt;
    }

    public void setPix_Fmt(String pix_Fmt) {
        this.pix_Fmt = pix_Fmt;
    }

    /**
     * 级别
     */
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 色度样品的位置
     */
    public String getChroma_Location() {
        return chroma_Location;
    }

    public void setChroma_Location(String chroma_Location) {
        this.chroma_Location = chroma_Location;
    }

    /**
     * 参考帧数量
     */
    public Integer getRefs() {
        return refs;
    }

    public void setRefs(Integer refs) {
        this.refs = refs;
    }

    public Boolean getIs_Avc() {
        return is_Avc;
    }

    public void setIs_Avc(Boolean is_Avc) {
        this.is_Avc = is_Avc;
    }

    /**
     * 表示用几个字节表示NALU的长度
     */
    public Long getNal_Length_Size() {
        return nal_Length_Size;
    }

    public void setNal_Length_Size(Long nal_Length_Size) {
        this.nal_Length_Size = nal_Length_Size;
    }

    /**
     * 真实基础帧率
     */
    public String getR_Frame_Rate() {
        return r_Frame_Rate;
    }

    public void setR_Frame_Rate(String r_Frame_Rate) {
        this.r_Frame_Rate = r_Frame_Rate;
    }

    /**
     * 平均帧率
     */
    public String getAvg_Frame_Rate() {
        return avg_Frame_Rate;
    }

    public void setAvg_Frame_Rate(String avg_Frame_Rate) {
        this.avg_Frame_Rate = avg_Frame_Rate;
    }

    /**
     * 每帧时长
     */
    public String getTime_Base() {
        return time_Base;
    }

    public void setTime_Base(String time_Base) {
        this.time_Base = time_Base;
    }

    /**
     * 流开始时间
     * <p>基于time_Base</p>
     */
    public Integer getStart_Pts() {
        return start_Pts;
    }

    public void setStart_Pts(Integer start_Pts) {
        this.start_Pts = start_Pts;
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
     * 流时长
     * <p>基于time_Base</p>
     */
    public Integer getDuration_Ts() {
        return duration_Ts;
    }

    public void setDuration_Ts(Integer duration_Ts) {
        this.duration_Ts = duration_Ts;
    }

    /**
     * 时长(秒)
     * <p>转换（duration_ts * time_base）之后的时长，单位秒</p>
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
     * 码率
     */
    public Long getBit_Rate() {
        return bit_Rate;
    }

    public void setBit_Rate(Long bit_Rate) {
        this.bit_Rate = bit_Rate;
    }

    /**
     * 最大码率
     */
    public Long getMax_Bit_Rate() {
        return max_Bit_Rate;
    }

    public void setMax_Bit_Rate(Long max_Bit_Rate) {
        this.max_Bit_Rate = max_Bit_Rate;
    }

    /**
     * 原生采样的比特数/位深
     */
    public Integer getBits_Per_Raw_Sample() {
        return bits_Per_Raw_Sample;
    }

    public void setBits_Per_Raw_Sample(Integer bits_Per_Raw_Sample) {
        this.bits_Per_Raw_Sample = bits_Per_Raw_Sample;
    }

    /**
     * 视频流帧数
     */
    public Long getNb_Frames() {
        return nb_Frames;
    }

    public void setNb_Frames(Long nb_Frames) {
        this.nb_Frames = nb_Frames;
    }

    public String getExtradata() {
        return extradata;
    }

    public void setExtradata(String extradata) {
        this.extradata = extradata;
    }

    /**
     * 配置
     */
    public DispositionInfo getDisposition() {
        return disposition;
    }

    public void setDisposition(DispositionInfo disposition) {
        this.disposition = disposition;
    }

    /**
     * 标签
     */
    public StreamTagsInfo getTags() {
        return tags;
    }

    public void setTags(StreamTagsInfo tags) {
        this.tags = tags;
    }
}
