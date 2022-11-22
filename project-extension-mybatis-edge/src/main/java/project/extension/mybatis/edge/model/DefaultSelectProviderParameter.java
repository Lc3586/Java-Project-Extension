package project.extension.mybatis.edge.model;

import project.extension.openapi.annotations.OpenApiIgnore;

/**
 * 查询构造器默认参数
 */
public class DefaultSelectProviderParameter {
    public DefaultSelectProviderParameter(String sql) {
        this.sql = sql;
    }

    private String sql;

    /**
     * Sql语句
     *
     * @return
     */
    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * 字段名
     */
    @OpenApiIgnore
    public static String fieldName = "sql";
}
