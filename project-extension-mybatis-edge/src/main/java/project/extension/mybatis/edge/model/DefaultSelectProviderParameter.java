package project.extension.mybatis.edge.model;

import lombok.Data;
import project.extension.openapi.annotations.OpenApiIgnore;

/**
 * 查询构造器默认参数
 */
@Data
public class DefaultSelectProviderParameter {
    public DefaultSelectProviderParameter(String sql) {
        this.sql = sql;
    }

    private String sql;

    /**
     * 字段名
     */
    @OpenApiIgnore
    public static String fieldName = "sql";
}
