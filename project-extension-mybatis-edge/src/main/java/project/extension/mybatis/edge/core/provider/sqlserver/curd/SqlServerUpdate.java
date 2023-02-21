package project.extension.mybatis.edge.core.provider.sqlserver.curd;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.curd.Update;
import project.extension.mybatis.edge.core.provider.sqlserver.SqlServerSqlProvider;

/**
 * SqlServer数据更新对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public class SqlServerUpdate<T>
        extends Update<T> {
    public SqlServerUpdate(DataSourceConfig config,
                           INaiveAdo ado,
                           Class<T> entityType) {
        super(config,
              new SqlServerSqlProvider(config),
              ado,
              entityType);
    }
}
