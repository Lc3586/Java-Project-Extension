package project.extension.mybatis.edge.core.provider.mysql;

import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.provider.normal.Select;
import project.extension.mybatis.edge.core.provider.standard.IAop;

/**
 * MySql数据查询对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public class MySqlSelect<T>
        extends Select<T> {
    public MySqlSelect(BaseConfig config,
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