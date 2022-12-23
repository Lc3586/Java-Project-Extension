package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.Delete;
import project.extension.mybatis.edge.aop.INaiveAop;

/**
 * SqlServer数据删除对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerDelete<T>
        extends Delete<T> {
    public SqlServerDelete(DataSourceConfig config,
                           INaiveAop aop,
                           Class<T> entityType,
                           boolean withTransactional) {
        super(config,
              new SqlServerSqlProvider(config),
              aop,
              entityType,
              withTransactional);
    }
}
