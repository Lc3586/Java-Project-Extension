package project.extension.mybatis.edge.core.provider.mysql;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.Update;
import project.extension.mybatis.edge.aop.INaiveAop;

/**
 * MySql数据更新对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public class MySqlUpdate<T>
        extends Update<T> {
    public MySqlUpdate(DataSourceConfig config,
                       INaiveAop aop,
                       Class<T> entityType,
                       boolean withTransactional) {
        super(config,
              new MySqlSqlProvider(config),
              aop,
              entityType,
              withTransactional);
    }
}
