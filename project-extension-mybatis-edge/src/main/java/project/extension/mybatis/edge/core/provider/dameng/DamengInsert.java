package project.extension.mybatis.edge.core.provider.dameng;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.normal.Insert;
import project.extension.mybatis.edge.aop.INaiveAop;

/**
 * Dameng数据插入对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-05-27
 */
public class DamengInsert<T>
        extends Insert<T> {
    public DamengInsert(DataSourceConfig config,
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
