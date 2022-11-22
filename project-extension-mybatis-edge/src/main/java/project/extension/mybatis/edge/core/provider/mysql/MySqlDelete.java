package project.extension.mybatis.edge.core.provider.mysql;

import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.provider.normal.Delete;
import project.extension.mybatis.edge.core.provider.standard.IAop;

/**
 * MySql数据删除对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public class MySqlDelete<T>
        extends Delete<T> {
    public MySqlDelete(BaseConfig config,
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
