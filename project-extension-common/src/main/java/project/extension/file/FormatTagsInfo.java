package project.extension.file;

import javax.xml.crypto.Data;

/**
 * 格式标签信息
 *
 * @author LCTR
 * @date 2022-04-08
 */
public class FormatTagsInfo {
    /**
     * 主品牌
     */
    private String major_Brand;

    /**
     * 次要版本
     */
    private String minor_Version;

    /**
     * 兼容性品牌
     */
    private String compatible_Brands;

    /**
     * 编码器
     */
    private String encoder;

    /**
     * 创建时间
     */
    private Data creation_Time;

    /**
     * 主品牌
     */
    public String getMajor_Brand() {
        return major_Brand;
    }

    public void setMajor_Brand(String major_Brand) {
        this.major_Brand = major_Brand;
    }

    /**
     * 次要版本
     */
    public String getMinor_Version() {
        return minor_Version;
    }

    public void setMinor_Version(String minor_Version) {
        this.minor_Version = minor_Version;
    }

    /**
     * 兼容性品牌
     */
    public String getCompatible_Brands() {
        return compatible_Brands;
    }

    public void setCompatible_Brands(String compatible_Brands) {
        this.compatible_Brands = compatible_Brands;
    }

    /**
     * 编码器
     */
    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
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
