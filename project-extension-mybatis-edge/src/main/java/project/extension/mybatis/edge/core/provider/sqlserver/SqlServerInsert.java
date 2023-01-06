package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.Insert;

/**
 * SqlServer数据插入对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerInsert<T>
        extends Insert<T> {
    public SqlServerInsert(DataSourceConfig config,
                           INaiveAdo ado,
                           Class<T> entityType) {
        super(config,
              new SqlServerSqlProvider(config),
              ado,
              entityType);
    }
}
