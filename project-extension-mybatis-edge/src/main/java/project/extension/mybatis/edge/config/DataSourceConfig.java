package project.extension.mybatis.edge.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.mybatis.edge.model.DbType;

import java.util.List;
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
     * mybatis配置文件路径
     */
    private String configLocation;

    /**
     * 需要扫描的存放实体类的包（包括TypeAliasesPackage）
     */
    private List<String> scanEntitiesPackages;

    /**
     * 需要扫描的存放Mapper配置类的包
     */
    private List<String> scanMapperXmlPackages;

    /**
     * 实体类表名/列名命名规则
     */
    private NameConvertType nameConvertType;

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
     * mybatis配置文件路径
     */
    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * 需要扫描的存放实体类的包
     */
    public List<String> getScanEntitiesPackages() {
        return scanEntitiesPackages;
    }

    public void setScanEntitiesPackages(List<String> scanEntitiesPackages) {
        this.scanEntitiesPackages = scanEntitiesPackages;
    }

    /**
     * 需要扫描的存放Mapper配置类的包
     */
    public List<String> getScanMapperXmlPackages() {
        return scanMapperXmlPackages;
    }

    public void setScanMapperXmlPackages(List<String> scanMapperXmlPackages) {
        this.scanMapperXmlPackages = scanMapperXmlPackages;
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
