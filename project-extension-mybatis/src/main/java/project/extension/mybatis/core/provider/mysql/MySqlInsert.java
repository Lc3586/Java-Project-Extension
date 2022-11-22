package project.extension.mybatis.core.provider.mysql;

import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.provider.normal.Insert;
import project.extension.mybatis.core.provider.standard.IAop;

/**
 * MySql数据插入对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public class MySqlInsert<T>
        extends Insert<T> {
    public MySqlInsert(BaseConfig config,
                       IAop aop,
                       Class<T> entityType,
                       boolean withTransactional) {
        super(config,
              new MySqlSqlProvider(config),
              aop,
              entityType,
              withTransactional);
    }
}
