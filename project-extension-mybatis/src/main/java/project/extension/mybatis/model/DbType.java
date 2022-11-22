package project.extension.mybatis.model;

import project.extension.openapi.annotations.OpenApiDescription;

/**
 * 数据库类型
 *
 * @author LCTR
 * @date 2022-03-24
 */
public enum DbType {
    /**
     * Jdbc MySql
     */
    @OpenApiDescription("Jdbc MySql")
    JdbcMySql(0, "JdbcMySql"),
    /**
     * Jdbc SqlServer
     */
    @OpenApiDescription("Jdbc SqlServer")
    JdbcSqlServer(1, "JdbcSqlServer"),
    /**
     * Jdbc SqlServer 2012以上版本
     */
    @OpenApiDescription("Jdbc SqlServer 2012以上版本")
    JdbcSqlServer_2012_plus(2, "JdbcSqlServer_2012_plus"),
    /**
     * Jdbc PostgreSQL
     */
    @OpenApiDescription("Jdbc PostgreSQL")
    JdbcPostgreSQL(3, "JdbcPostgreSQL"),
    /**
     * Jdbc Oracle
     */
    @OpenApiDescription("Jdbc Oracle")
    JdbcOracle(4, "JdbcOracle"),
    /**
     * Jdbc Dameng
     */
    @OpenApiDescription("Jdbc Dameng")
    JdbcDameng(5, "JdbcDameng");

    /**
     * @param index 索引
     * @param value 值
     */
    DbType(int index, String value) {
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
    public static DbType toEnum(String value) throws IllegalArgumentException {
        return DbType.valueOf(value);
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static DbType toEnum(int index) throws IllegalArgumentException {
        for (DbType value : DbType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效", index));
    }
}