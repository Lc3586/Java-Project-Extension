package project.extension.mybatis.edge.core.provider.dameng.curd;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.dameng.DamengSqlProvider;
import project.extension.mybatis.edge.core.provider.normal.curd.Update;

/**
 * Dameng数据更新对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-05-27
 */
public class DamengUpdate<T>
        extends Update<T> {
    public DamengUpdate(DataSourceConfig config,
                        INaiveAdo ado,
                        Class<T> entityType) {
        super(config,
              new DamengSqlProvider(config),
              ado,
              entityType);
    }
}
