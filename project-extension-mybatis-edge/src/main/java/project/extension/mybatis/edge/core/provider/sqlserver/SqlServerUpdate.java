package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.provider.normal.Update;
import project.extension.mybatis.edge.core.provider.standard.IAop;

/**
 * SqlServer数据更新对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public class SqlServerUpdate<T>
        extends Update<T> {
    public SqlServerUpdate(BaseConfig config,
                           IAop aop,
                           Class<T> entityType,
                           boolean withTransactional) {
        super(config,
              new SqlServerSqlProvider(config),
              aop,
              entityType,
              withTransactional);
    }
}
