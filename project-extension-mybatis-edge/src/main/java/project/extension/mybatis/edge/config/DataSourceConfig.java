package project.extension.mybatis.edge.config;

import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.mybatis.edge.model.DbType;

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
     * 实体类表名/列名命名规则
     */
    private NameConvertType nameConvertType;

    /**
     * mybatis配置文件路径
     */
    private String configLocation;

    /**
     * 连接字符串
     */
    private String connectionString;

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
     * 实体类表名/列名命名规则
     */
    public NameConvertType getNameConvertType() {
        return nameConvertType;
    }

    public void setNameConvertType(NameConvertType nameConvertType) {
        this.nameConvertType = nameConvertType;
    }

    /**
     * mybatis配置文件路径
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
}
