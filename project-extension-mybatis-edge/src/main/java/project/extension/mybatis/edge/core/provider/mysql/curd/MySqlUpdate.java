package project.extension.mybatis.edge.core.provider.mysql.curd;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.mysql.MySqlSqlProvider;
import project.extension.mybatis.edge.core.provider.normal.curd.Update;

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
                       INaiveAdo ado,
                       Class<T> entityType) {
        super(config,
              new MySqlSqlProvider(config),
              ado,
              entityType);
    }
}
