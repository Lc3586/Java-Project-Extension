package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.Update;
import project.extension.mybatis.edge.aop.INaiveAop;

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
