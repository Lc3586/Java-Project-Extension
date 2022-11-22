package project.extension.mybatis.edge.core.provider;

import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.provider.dameng.DamengProvider;
import project.extension.mybatis.edge.core.provider.mysql.MySqlProvider;
import project.extension.mybatis.edge.core.provider.sqlserver.SqlServerProvider;
import project.extension.mybatis.edge.core.provider.standard.IAop;
import project.extension.mybatis.edge.core.provider.standard.IBaseDbProvider;

import javax.naming.OperationNotSupportedException;

/**
 * 数据库对象构造器
 *
 * @author LCTR
 * @date 2022-03-30
 */
public class DbProvider {
    public static <T> IBaseDbProvider<T> getDbProvider(BaseConfig config,
                                                       IAop aop)
            throws
            OperationNotSupportedException {
        switch (config.getDbType()) {
            case JdbcMySql:
                return new MySqlProvider<>(config,
                                           aop);
            case JdbcDameng:
                return new DamengProvider<>(config,
                                            aop);
            case JdbcSqlServer:
            case JdbcSqlServer_2012_plus:
                return new SqlServerProvider<>(config,
                                               aop);
            default:
                throw new OperationNotSupportedException(String.format("暂不支持%s数据库",
                                                                       config.getDbType()));
        }
    }
}
