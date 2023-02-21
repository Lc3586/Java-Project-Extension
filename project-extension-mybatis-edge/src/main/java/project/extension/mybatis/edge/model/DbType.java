package project.extension.mybatis.edge.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 数据库类型
 *
 * @author LCTR
 * @date 2022-03-24
 */
public enum DbType {
    /**
     * Jdbc MySQL 8.0
     */
    @OpenApiDescription("Jdbc MySQL 8.0")
    JdbcMySQL8(0,
               "JdbcMySQL8"),
    /**
     * Jdbc MariaDB 10.0
     */
    @OpenApiDescription("Jdbc MariaDB 10.0")
    JdbcMariaDB10(0,
                  "JdbcMariaDB10"),
    /**
     * Jdbc SqlServer
     */
    @OpenApiDescription("Jdbc SqlServer")
    JdbcSqlServer(20,
                  "JdbcSqlServer"),
    /**
     * Jdbc SqlServer 2012及以上版本
     */
    @OpenApiDescription("Jdbc SqlServer 2012以上版本")
    JdbcSqlServer_2012_plus(21,
                            "JdbcSqlServer_2012_plus"),
    /**
     * Jdbc PostgreSQL 15.0
     */
    @OpenApiDescription("Jdbc PostgreSQL 15.0")
    JdbcPostgreSQL15(30,
                     "JdbcPostgreSQL15"),
    /**
     * Jdbc Oracle 12c
     */
    @OpenApiDescription("Jdbc Oracle 12c")
    JdbcOracle12c(41,
                  "JdbcOracle12c"),
    /**
     * Jdbc Oracle 18c
     */
    @OpenApiDescription("Jdbc Oracle 18c")
    JdbcOracle18c(42,
                  "JdbcOracle18c"),
    /**
     * Jdbc Oracle 19c
     */
    @OpenApiDescription("Jdbc Oracle 19c")
    JdbcOracle19c(43,
                  "JdbcOracle19c"),
    /**
     * Jdbc Oracle 21c
     */
    @OpenApiDescription("Jdbc Oracle 21c")
    JdbcOracle21c(44,
                  "JdbcOracle21c"),
    /**
     * Jdbc Dameng 6
     */
    @OpenApiDescription("Jdbc Dameng 6")
    JdbcDameng6(60,
                "JdbcDameng6"),
    /**
     * Jdbc Dameng 7
     */
    @OpenApiDescription("Jdbc Dameng 7")
    JdbcDameng7(61,
                "JdbcDameng7"),
    /**
     * Jdbc Dameng 8
     */
    @OpenApiDescription("Jdbc Dameng 8")
    JdbcDameng8(62,
                "JdbcDameng8");

    /**
     * @param index 索引
     * @param value 值
     */
    DbType(int index,
           String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * 索引
     */
    final int index;

    /**
     * 值
     */
    final String value;

    /**
     * 获取索引
     *
     * @return 索引
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 获取字符串
     *
     * @return 值
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * 获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static DbType toEnum(String value)
            throws
            IllegalArgumentException {
        return DbType.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static DbType toEnum(int index)
            throws
            IllegalArgumentException {
        for (DbType value : DbType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}