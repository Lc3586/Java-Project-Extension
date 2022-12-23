package project.extension.mybatis.edge.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.mybatis.edge.model.DbType;

import java.util.Properties;

/**
 * 数据源配置
 *
 * @author LCTR
 * @date 2022-10-20
 */
public class DataSourceConfig {
    /**
     * 数据库类型
     */
    private DbType dbType;

    /**
     * 实体类表名/列名命名规则
     */
    private NameConvertType nameConvertType;

    /**
     * 默认的mybatis配置文件路径
     */
    private String configLocation;

    /**
     * 启用
     *
     * @默认值 true
     */
    private boolean enable = true;

    /**
     * 配置
     */
    public Properties properties;

    /**
     * 名称
     */
    public String getName() {
        return properties == null
               ? null
               : (String) properties.get(DruidDataSourceFactory.PROP_NAME);
    }

    public void setName(String name) {
        if (properties == null) properties = new Properties();
        properties.put(DruidDataSourceFactory.PROP_NAME,
                       name);
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
     * 默认的mybatis配置文件路径
     */
    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
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

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
