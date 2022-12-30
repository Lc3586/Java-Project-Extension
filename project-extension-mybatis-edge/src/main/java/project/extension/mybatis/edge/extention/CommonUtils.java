package project.extension.mybatis.edge.extention;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import dm.jdbc.driver.DmDriver;
import oracle.jdbc.driver.OracleDriver;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.mybatis.edge.model.DbType;
import project.extension.standard.exception.ApplicationException;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            ApplicationException {
        switch (dbType) {
            case JdbcMySql:
                return com.alibaba.druid.DbType.mysql;
            case JdbcSqlServer:
            case JdbcSqlServer_2012_plus:
                return com.alibaba.druid.DbType.sqlserver;
            case JdbcOracle:
                return com.alibaba.druid.DbType.oracle;
            case JdbcDameng:
                return com.alibaba.druid.DbType.dm;
            case JdbcPostgreSQL:
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
            ApplicationException {
        switch (dbType) {
            case JdbcMySql:
                try {
                    return new com.mysql.cj.jdbc.Driver();
                } catch (Exception ex) {
                    throw new ApplicationException(DbContextStrings.getCreateInstanceFailed("com.mysql.cj.jdbc.Driver"),
                                                   ex);
                }
            case JdbcSqlServer:
            case JdbcSqlServer_2012_plus:
                return new SQLServerDriver();
            case JdbcOracle:
                return new OracleDriver();
            case JdbcDameng:
                Driver driver = DmDriver.driver;
                try {
                    DriverManager.registerDriver(driver);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return driver;
            case JdbcPostgreSQL:
                return new org.postgresql.Driver();
            default:
                throw new ApplicationException(DbContextStrings.getUnsupportedDbType(dbType.toString()));
        }
    }
}
