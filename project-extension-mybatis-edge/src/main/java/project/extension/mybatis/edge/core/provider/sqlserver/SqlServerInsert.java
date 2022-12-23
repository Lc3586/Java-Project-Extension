package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.Insert;
import project.extension.mybatis.edge.aop.INaiveAop;

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
