package project.extension.mybatis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.extension.mybatis.model.DbType;
import project.extension.mybatis.model.NameConvertType;

import java.util.List;

/**
 * 基础配置
 *
 * @author LCTR
 * @date 2022-03-28
 */
@Component("MybatisBaseConfig")
@ConfigurationProperties(prefix = "project.extension.mybatis")
public class BaseConfig {
    /**
     * 数据源名称
     */
    private String dataSource = "master";

    /**
     * 数据库类型
     */
    private DbType dbType;

    /**
     * 实体类表名/列名命名规则
     */
    private NameConvertType nameConvertType = NameConvertType.None;

    /**
     * mybatis配置文件路径
     */
    private String configLocation;

    /**
     * 连接字符串
     */
    private String connectionString;

    /**
     * 多数据源配置
     */
    private List<DataSourceConfig> multiDataSource;

    /**
     * 数据源名称
     */
    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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
     * 实体类表明/列名命名规则
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

    /**
     * 多库配置
     */
    public List<DataSourceConfig> getMultiDataSource() {
        return multiDataSource;
    }

    public void setMultiDataSource(List<DataSourceConfig> multi) {
        this.multiDataSource = multi;
    }

    /**
     * 多库配置
     *
     * @param dataSource 数据源名称
     */
    public BaseConfig getMultiConfig(String dataSource)
            throws
            Exception {
        if (multiDataSource != null)
            for (DataSourceConfig multiConfig : multiDataSource) {
                if (dataSource.equals(multiConfig.getName())) {
                    BaseConfig config = new BaseConfig();
                    config.setDataSource(dataSource);

                    if (multiConfig.getDbType() == null)
                        config.setDbType(this.getDbType());
                    else
                        config.setDbType(multiConfig.getDbType());

                    if (multiConfig.getNameConvertType() == null)
                        config.setNameConvertType(this.getNameConvertType());
                    else
                        config.setNameConvertType(multiConfig.getNameConvertType());

                    if (!StringUtils.hasText(multiConfig.getConfigLocation()))
                        config.setConfigLocation(this.getConfigLocation());
                    else
                        config.setConfigLocation(multiConfig.getConfigLocation());

                    if (!StringUtils.hasText(multiConfig.getConnectionString()))
                        config.setConnectionString(this.getConnectionString());
                    else
                        config.setConnectionString(multiConfig.getConnectionString());

                    return config;
                }
            }

        throw new Exception(String.format("在配置文件的project.extension.mybatis.multi中未找到名称为%s的数据源配置",
                                          dataSource));
    }
}
