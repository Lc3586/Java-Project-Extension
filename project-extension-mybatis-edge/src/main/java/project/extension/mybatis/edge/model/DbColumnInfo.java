package project.extension.mybatis.edge.model;

import lombok.Data;

import java.sql.JDBCType;

/**
 * 数据库表的列信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
@Data
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
}
