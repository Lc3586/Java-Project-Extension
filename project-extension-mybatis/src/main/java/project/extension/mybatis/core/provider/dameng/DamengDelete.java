package project.extension.mybatis.core.provider.dameng;

import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.provider.normal.Delete;
import project.extension.mybatis.core.provider.standard.IAop;

/**
 * Dameng数据删除对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-05-27
 */
public class DamengDelete<T>
        extends Delete<T> {
    public DamengDelete(BaseConfig config,
                        IAop aop,
                        Class<T> entityType,
                        boolean withTransactional) {
        super(config,
              new DamengSqlProvider(config),
              aop,
              entityType,
              withTransactional);
    }
}
