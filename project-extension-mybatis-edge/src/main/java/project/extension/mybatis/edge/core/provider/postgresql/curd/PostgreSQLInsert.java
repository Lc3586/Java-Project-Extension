package project.extension.mybatis.edge.core.provider.postgresql.curd;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.curd.Insert;
import project.extension.mybatis.edge.core.provider.postgresql.PostgreSQLSqlProvider;

/**
 * PostgreSQL数据插入对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2023-02-21
 */
public class PostgreSQLInsert<T>
        extends Insert<T> {
    public PostgreSQLInsert(DataSourceConfig config,
                            INaiveAdo ado,
                            Class<T> entityType) {
        super(config,
              new PostgreSQLSqlProvider(config),
              ado,
              entityType);
    }
}
