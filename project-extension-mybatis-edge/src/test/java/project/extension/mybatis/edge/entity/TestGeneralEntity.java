package project.extension.mybatis.edge.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;
import project.extension.mybatis.edge.annotations.ColumnSetting;
import project.extension.mybatis.edge.annotations.TableSetting;
import project.extension.openapi.annotations.OpenApiDescription;
import project.extension.openapi.annotations.OpenApiSubTag;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 测试读写常规数据
 *
 * @author LCTR
 * @date 2022-01-09
 */
@TableSetting
//新增此属性防止mapper的xml文件报错
@Alias("TestGeneralEntity")
public class TestGeneralEntity {
    /**
     * 主键
     */
    @OpenApiDescription("主键")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(isPrimaryKey = true,
                   length = 36)
    private String id;

    /**
     * 字符
     * <p>在达梦数据库中最好将char的精度乘以2</p>
     */
    @OpenApiDescription("字符")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(alias = "char")
    private Character char_;

    /**
     * 字符串
     * <p>在达梦数据库中最好将varchar2的精度乘以2</p>
     */
    @OpenApiDescription("字符串")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(length = 200)
    private String string;

    /**
     * 8位整数
     */
    @OpenApiDescription("8位整数")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(alias = "byte")
    private Byte byte_;

    /**
     * 16位整数
     */
    @OpenApiDescription("16位整数")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(alias = "short")
    private Short short_;

    /**
     * 32位整数
     */
    @OpenApiDescription("32位整数")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(alias = "integer")
    private Integer integer;

    /**
     * 64位整数
     */
    @OpenApiDescription("64位整数")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(alias = "long")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long long_;

    /**
     * 单精度浮点数
     * <p>在mysql和mariadb数据库中需要指定为float(70, 30)</p>
     */
    @OpenApiDescription("单精度浮点数")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(alias = "float",
                   precision = 38,
                   scale = 30)
    private Float float_;

    /**
     * 双精度浮点数
     */
    @OpenApiDescription("双精度浮点数")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(alias = "double",
                   precision = 38,
                   scale = 30)
    private Double double_;

    /**
     * 高精度浮点数
     */
    @OpenApiDescription("高精度浮点数")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(precision = 38,
                   scale = 30)
    private BigDecimal decimal;

    /**
     * 布尔
     */
    @OpenApiDescription("布尔")
    @OpenApiSubTag({"List",
                    "Create"})
    @ColumnSetting(alias = "boolean")
    private Boolean boolean_;

    /**
     * 日期
     */
    @OpenApiDescription("日期")
    @OpenApiSubTag({"List",
                    "Create"})
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private java.sql.Date date;

    /**
     * 时间
     */
    @OpenApiDescription("时间")
    @OpenApiSubTag({"List",
                    "Create"})
    @JsonFormat(pattern = "HH:mm:ss.SSS")
    @JSONField(format = "HH:mm:ss.SSS")
    @ColumnSetting(isIgnore = true)
    //TODO mybatis 默认类型处理类暂不支持
    private java.sql.Time time;

    /**
     * 日期时间
     * <p>在mysql和mariadb数据库中需要指定为datetime(6)</p>
     */
    @OpenApiDescription("日期时间")
    @OpenApiSubTag({"List",
                    "Create"})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date datetime;

    /**
     * 主键
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 字符
     */
    public Character getChar_() {
        return char_;
    }

    public void setChar_(Character char_) {
        this.char_ = char_;
    }

    /**
     * 字符串
     */
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    /**
     * 16位整数
     */
    public Byte getByte_() {
        return byte_;
    }

    public void setByte_(Byte byte_) {
        this.byte_ = byte_;
    }

    /**
     * 16位整数
     */
    public Short getShort_() {
        return short_;
    }

    public void setShort_(Short short_) {
        this.short_ = short_;
    }

    /**
     * 32位整数
     */
    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    /**
     * 64位整数
     */
    public Long getLong_() {
        return long_;
    }

    public void setLong_(Long long_) {
        this.long_ = long_;
    }

    /**
     * 单精度浮点数
     */
    public Float getFloat_() {
        return float_;
    }

    public void setFloat_(Float float_) {
        this.float_ = float_;
    }

    /**
     * 双精度浮点数
     */
    public Double getDouble_() {
        return double_;
    }

    public void setDouble_(Double double_) {
        this.double_ = double_;
    }

    /**
     * 高精度浮点数
     */
    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    /**
     * 布尔
     */
    public Boolean getBoolean_() {
        return boolean_;
    }

    public void setBoolean_(Boolean boolean_) {
        this.boolean_ = boolean_;
    }

    /**
     * 日期
     */
    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    /**
     * 时间
     */
    public java.sql.Time getTime() {
        return time;
    }

    public void setTime(java.sql.Time time) {
        this.time = time;
    }

    /**
     * 日期时间
     */
    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,
                                   ToStringStyle.MULTI_LINE_STYLE)
                .append("id",
                        getId())
                .append("char_",
                        getChar_())
                .append("string",
                        getString())
                .append("byte_",
                        getByte_())
                .append("short_",
                        getShort_())
                .append("integer",
                        getInteger())
                .append("long_",
                        getLong_())
                .append("float_",
                        getFloat_())
                .append("double_",
                        getDouble_())
                .append("decimal",
                        getDecimal())
                .append("boolean_",
                        getBoolean_())
                .append("date",
                        getDate())
                .append("time",
                        getTime())
                .append("datetime",
                        getDatetime())
                .toString();
    }
}
