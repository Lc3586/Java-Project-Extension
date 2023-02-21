package project.extension.mybatis.edge.core.provider.oracle.curd;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.curd.Select;
import project.extension.mybatis.edge.core.provider.oracle.OracleSqlProvider;

/**
 * Dameng数据查询对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-05-27
 */
public class OracleSelect<T>
        extends Select<T> {
    public OracleSelect(DataSourceConfig config,
                        INaiveAdo ado,
                        Class<T> entityType) {
        super(config,
              new OracleSqlProvider(config),
              ado,
              entityType);
    }
}
