package project.extension.mybatis.edge.core.provider.postgresql;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.SqlProvider;

/**
 * PostgreSQL数据库Sql语句构建器
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class PostgreSQLSqlProvider
        extends SqlProvider {
    public PostgreSQLSqlProvider(DataSourceConfig config) {
        super(config,
              new char[]{'"',
                         '"'});
    }

    /**
     * 原始Sql语句转为获取首条记录的Sql语句
     *
     * @param originalSql 原始Sql语句
     * @param offset      偏移量
     * @param count       数据量
     * @return Sql语句
     */
    @Override
    public String originalSql2LimitSql(String originalSql,
                                       int offset,
                                       int count) {
        return String.format("%s \r\nLIMIT %s OFFSET %s ",
                             originalSql,
                             count,
                             offset);
    }
}
