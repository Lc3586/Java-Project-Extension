package project.extension.mybatis.core.provider.sqlserver;

import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.provider.normal.Insert;
import project.extension.mybatis.core.provider.standard.IAop;

/**
 * SqlServer数据插入对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerInsert<T>
        extends Insert<T> {
    public SqlServerInsert(BaseConfig config,
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
