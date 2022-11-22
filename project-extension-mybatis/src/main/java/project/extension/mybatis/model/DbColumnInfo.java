package project.extension.mybatis.model;

import java.sql.JDBCType;

/**
 * 数据库表的列信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
public class DbColumnInfo {
    public DbColumnInfo() {

    }

    public DbColumnInfo(DbTableInfo table,
                        String name,
                        boolean isPrimary,
                        boolean isIdentity,
                        boolean isNullable,
                        int position,
                        String dbTypeText,
                        String dbTypeTextFull,
                        int maxLength,
                        int precision,
                        int scale,
                        String defaultValue,
                        String comment) {
        this.table = table;
        this.name = name;
        this.isPrimary = isPrimary;
        this.isIdentity = isIdentity;
        this.isNullable = isNullable;
        this.position = position;
        this.dbTypeText = dbTypeText;
        this.dbTypeTextFull = dbTypeTextFull;
        this.maxLength = maxLength;
        this.precision = precision;
        this.scale = scale;
        this.defaultValue = defaultValue;
        this.comment = comment;
    }

    /**
     * 所属数据库表信息
     */
    private DbTableInfo table;

    /**
     * 列名
     */
    private String name;

    /**
     * 映射到java类型
     */
    private Class<?> javaType;

    /**
     * 映射到jdbc类型
     */
    private JDBCType jdbcType;

    /**
     * 数据库类型字符串
     * <p>示例值：varchar</p>
     */
    private String dbTypeText;

    /**
     * 数据库类型完整字符串
     * <p>示例值：varchar(50)</p>
     */
    private String dbTypeTextFull;

    /**
     * 最大长度
     */
    private int maxLength;

    /**
     * 主键
     */
    private boolean isPrimary;

    /**
     * 是否自增
     */
    private boolean isIdentity;

    /**
     * 是否可为空
     */
    private boolean isNullable;

    /**
     * 数据库默认值
     */
    private String defaultValue;

    /**
     * 字段位置
     */
    private int position;

    /**
     * 精度
     * <p>decimal、numeric等数据类型的长度</p>
     */
    private int precision;

    /**
     * 标度
     * <p>decimal、numeric等数据类型的小数位长度</p>
     */
    private int scale;

    /**
     * 备注
     */
    private String comment;

    /**
     * 所属数据库表信息
     */
    public DbTableInfo getTable() {
        return table;
    }

    /**
     * 所属数据库表信息
     */
    public void setTable(DbTableInfo table) {
        this.table = table;
    }

    /**
     * 列名
     */
    public String getName() {
        return name;
    }

    /**
     * 列名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 映射到java类型
     */
    public Class<?> getJavaType() {
        return javaType;
    }

    /**
     * 映射到java类型
     */
    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    /**
     * 映射到jdbc类型
     */
    public JDBCType getJdbcType() {
        return jdbcType;
    }

    /**
     * 映射到jdbc类型
     */
    public void setJdbcType(JDBCType jdbcType) {
        this.jdbcType = jdbcType;
    }

    /**
     * 数据库类型字符串
     * <p>示例值：varchar</p>
     */
    public String getDbTypeText() {
        return dbTypeText;
    }

    /**
     * 数据库类型字符串
     * <p>示例值：varchar</p>
     */
    public void setDbTypeText(String dbTypeText) {
        this.dbTypeText = dbTypeText;
    }

    /**
     * 数据库类型完整字符串
     * <p>示例值：varchar(50)</p>
     */
    public String getDbTypeTextFull() {
        return dbTypeTextFull;
    }

    /**
     * 数据库类型完整字符串
     * <p>示例值：varchar(50)</p>
     */
    public void setDbTypeTextFull(String dbTypeTextFull) {
        this.dbTypeTextFull = dbTypeTextFull;
    }

    /**
     * 最大长度
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * 最大长度
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * 主键
     */
    public boolean getIsPrimary() {
        return isPrimary;
    }

    /**
     * 主键
     */
    public void setIsPrimary(boolean primary) {
        isPrimary = primary;
    }

    /**
     * 是否自增
     */
    public boolean getIsIdentity() {
        return isIdentity;
    }

    /**
     * 是否自增
     */
    public void setIsIdentity(boolean identity) {
        isIdentity = identity;
    }

    /**
     * 是否可为空
     */
    public boolean getIsNullable() {
        return isNullable;
    }

    /**
     * 是否可为空
     */
    public void setIsNullable(boolean nullable) {
        isNullable = nullable;
    }

    /**
     * 数据库默认值
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 数据库默认值
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * 字段位置
     */
    public int getPosition() {
        return position;
    }

    /**
     * 字段位置
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * 精度
     * <p>decimal、numeric等数据类型的长度</p>
     */
    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * 标度
     * <p>decimal、numeric等数据类型的小数位长度</p>
     */
    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * 备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 备注
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
