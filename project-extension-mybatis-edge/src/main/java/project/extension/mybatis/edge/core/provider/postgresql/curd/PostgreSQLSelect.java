package project.extension.mybatis.edge.core.provider.postgresql.curd;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.curd.Select;
import project.extension.mybatis.edge.core.provider.postgresql.PostgreSQLSqlProvider;

/**
 * PostgreSQL数据查询对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2023-02-21
 */
public class PostgreSQLSelect<T>
        extends Select<T> {
    public PostgreSQLSelect(DataSourceConfig config,
                            INaiveAdo ado,
                            Class<T> entityType) {
        super(config,
              new PostgreSQLSqlProvider(config),
              ado,
              entityType);
    }
}
