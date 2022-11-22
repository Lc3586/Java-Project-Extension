package project.extension.mybatis.core.provider.sqlserver;

import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.provider.normal.Delete;
import project.extension.mybatis.core.provider.standard.IAop;

/**
 * SqlServer数据删除对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerDelete<T>
        extends Delete<T> {
    public SqlServerDelete(BaseConfig config,
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
