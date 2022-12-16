package project.extension.mybatis.edge.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import dm.jdbc.driver.DmDriver;
import oracle.jdbc.driver.OracleDriver;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.mybatis.edge.model.DbType;
import project.extension.standard.exception.ApplicationException;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据源配置
 *
 * @author LCTR
 * @date 2022-10-20
 */
public class DataSourceConfig {
    /**
     * 名称
     */
    private String name;

    /**
     * 数据库类型
     */
    private DbType dbType;

    /**
     * 驱动
     */
    private Driver driver;

    /**
     * 实体类表名/列名命名规则
     */
    private NameConvertType nameConvertType;

    /**
     * 默认的mybatis配置文件路径
     */
    private String configLocation;

    /**
     * 连接字符串
     */
    private String connectionString;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 启用
     *
     * @默认值 true
     */
    private boolean enable = true;

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 数据库类型
     */
    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    /**
     * 驱动
     */
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * 实体类表名/列名命名规则
     */
    public NameConvertType getNameConvertType() {
        return nameConvertType;
    }

    public void setNameConvertType(NameConvertType nameConvertType) {
        this.nameConvertType = nameConvertType;
    }

    /**
     * 默认的mybatis配置文件路径
     */
    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * 连接字符串
     */
    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * 用户名
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 密码
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 启用
     *
     * @默认值 true
     */
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 应用配置到数据源
     *
     * @param datasource 数据源
     * @return 数据源
     */
    public DruidDataSource applyConfig(DruidDataSource datasource) {
        datasource.setName(this.getName());
        datasource.setUrl(this.getConnectionString());
        datasource.setUsername(this.getUsername());
        datasource.setPassword(this.getPassword());
        datasource.setEnable(this.isEnable());
        datasource.setDbType(CommonUtils.convertToAlibabaDbType(this.getDbType()));
        if (this.getDriver() == null)
            this.setDriver(CommonUtils.getDriver(this.getDbType()));
        datasource.setDriver(this.getDriver());
        return datasource;
    }
}
