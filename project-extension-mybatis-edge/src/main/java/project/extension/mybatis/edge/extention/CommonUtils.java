package project.extension.mybatis.edge.extention;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import oracle.jdbc.OracleDriver;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.DbType;
import project.extension.standard.exception.ModuleException;

import java.sql.Driver;

/**
 * 通用工具类
 *
 * @author LCTR
 * @date 2022-12-15
 */
public class CommonUtils {
    private static BaseConfig config;

    /**
     * 配置
     */
    public static BaseConfig getConfig() {
        if (config == null)
            config = IOCExtension.applicationContext.getBean(BaseConfig.class);
        return config;
    }

    /**
     * 转换为阿里巴巴数据库类型
     *
     * @param dbType 数据库类型
     * @return 阿里巴巴数据库类型
     */
    public static com.alibaba.druid.DbType convertToAlibabaDbType(DbType dbType)
            throws
            ModuleException {
        switch (dbType) {
            case JdbcMySql8:
                return com.alibaba.druid.DbType.mysql;
            case JdbcMariaDB10:
                return com.alibaba.druid.DbType.mariadb;
            case JdbcSqlServer:
            case JdbcSqlServer_2012_plus:
                return com.alibaba.druid.DbType.sqlserver;
            case JdbcOracle19c:
                return com.alibaba.druid.DbType.oracle;
            case JdbcDameng6:
            case JdbcDameng7:
            case JdbcDameng8:
                return com.alibaba.druid.DbType.dm;
            case JdbcPostgreSQL15:
                return com.alibaba.druid.DbType.postgresql;
            default:
                return com.alibaba.druid.DbType.other;
        }
    }

    /**
     * 获取数据库驱动
     *
     * @param dbType 数据库类型
     * @return 数据库驱动
     */
    public static Driver getDriver(DbType dbType)
            throws
            ModuleException {
        switch (dbType) {
            case JdbcMySql8:
                try {
                    return new com.mysql.jdbc.Driver();
                } catch (Exception ex) {
                    throw new ModuleException(Strings.getCreateInstanceFailed("com.mysql.jdbc.Driver"),
                                              ex);
                }
            case JdbcMariaDB10:
                try {
                    return new org.mariadb.jdbc.Driver();
                } catch (Exception ex) {
                    throw new ModuleException(Strings.getCreateInstanceFailed("org.mariadb.jdbc.Driver"),
                                              ex);
                }
            case JdbcSqlServer:
            case JdbcSqlServer_2012_plus:
                return new SQLServerDriver();
            case JdbcOracle19c:
                return new OracleDriver();
            case JdbcDameng6:
                return new dm6.jdbc.driver.DmDriver();
            case JdbcDameng7:
            case JdbcDameng8:
                return new dm.jdbc.driver.DmDriver();
            case JdbcPostgreSQL15:
                return new org.postgresql.Driver();
            default:
                throw new ModuleException(Strings.getUnsupportedDbType(dbType.toString()));
        }
    }
}
