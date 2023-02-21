package project.extension.mybatis.edge.core.provider.dameng.curd;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.dameng.DamengSqlProvider;
import project.extension.mybatis.edge.core.provider.normal.curd.Delete;

/**
 * Dameng数据删除对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-05-27
 */
public class DamengDelete<T>
        extends Delete<T> {
    public DamengDelete(DataSourceConfig config,
                        INaiveAdo ado,
                        Class<T> entityType) {
        super(config,
              new DamengSqlProvider(config),
              ado,
              entityType);
    }
}
