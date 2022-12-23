package project.extension.mybatis.edge.core.provider.dameng;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.Update;
import project.extension.mybatis.edge.aop.INaiveAop;

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
                        INaiveAop aop,
                        Class<T> entityType,
                        boolean withTransactional) {
        super(config,
              new DamengSqlProvider(config),
              aop,
              entityType,
              withTransactional);
    }
}
