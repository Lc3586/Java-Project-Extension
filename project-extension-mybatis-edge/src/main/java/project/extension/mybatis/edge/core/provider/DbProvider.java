package project.extension.mybatis.edge.core.provider;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.provider.dameng.DamengProvider;
import project.extension.mybatis.edge.core.provider.mysql.MySqlProvider;
import project.extension.mybatis.edge.core.provider.sqlserver.SqlServerProvider;
import project.extension.mybatis.edge.core.provider.standard.IBaseDbProvider;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.standard.exception.ModuleException;

/**
 * 数据库对象构造器
 *
 * @author LCTR
 * @date 2022-03-30
 */
public class DbProvider {
    public static IBaseDbProvider getDbProvider(DataSourceConfig config)
            throws
            ModuleException {
        switch (config.getDbType()) {
            case JdbcMySql8:
            case JdbcMariaDB10:
                return new MySqlProvider(config);
            case JdbcDameng6:
            case JdbcDameng7:
            case JdbcDameng8:
                return new DamengProvider(config);
            case JdbcSqlServer:
            case JdbcSqlServer_2012_plus:
                return new SqlServerProvider(config);
            default:
                throw new ModuleException(Strings.getUnsupportedDbType(config.getDbType()
                                                                             .toString()));
        }
    }
}
