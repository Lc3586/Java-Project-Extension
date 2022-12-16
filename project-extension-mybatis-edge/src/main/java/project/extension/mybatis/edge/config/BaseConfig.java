package project.extension.mybatis.edge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.model.DbType;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.standard.exception.ApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 基础配置
 *
 * @author LCTR
 * @date 2022-03-28
 */
@Component
@ConfigurationProperties("project.extension.mybatis")
public class BaseConfig {
    /**
     * 需要扫描的存放实体类的包
     */
    private List<String> scanEntitiesPackages;

    /**
     * 默认的数据源
     */
    private String dataSource;

    /**
     * 默认的数据库类型
     */
    private DbType dbType;

    /**
     * 默认的实体类表名/列名命名规则
     */
    private NameConvertType nameConvertType = NameConvertType.None;

    /**
     * mybatis配置文件路径
     */
    private String configLocation;

    /**
     * 默认的连接字符串
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
     * 多数据源配置
     */
    private List<DataSourceConfig> multiDataSource;

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
     * 默认的数据源名称
     */
    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 默认的数据库类型
     */
    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    /**
     * 默认的实体类表明/列名命名规则
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
     * 默认的连接字符串
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
     * 多库配置
     */
    public List<DataSourceConfig> getMultiDataSource() {
        return multiDataSource;
    }

    public void setMultiDataSource(List<DataSourceConfig> multi) {
        this.multiDataSource = multi;
    }

    /**
     * 是否为多数据源
     */
    public boolean isMultiDataSource() {
        return multiDataSource != null && multiDataSource.size() > 1;
    }

    public List<String> getAllDataSource() {
        this.getDataSourceConfig();
        return this.getMultiDataSource()
                   .stream()
                   .map(DataSourceConfig::getName)
                   .collect(Collectors.toList());
    }

    /**
     * 获取默认的数据源配置
     */
    public DataSourceConfig getDataSourceConfig() {
        if (!StringUtils.hasText(this.getDataSource())) {
            if (this.getMultiDataSource() == null || this.getMultiDataSource()
                                                         .size() == 0) {
                //设置默认数据源为master
                this.setDataSource("master");
            } else {
                //设置默认数据源为多库中的第一个
                this.setDataSource(this.getMultiDataSource()
                                       .get(0)
                                       .getName());

                if (!StringUtils.hasText(this.getDataSource())) {
                    if (this.getMultiDataSource()
                            .size() > 1)
                        throw new ApplicationException("在配置文件的project.extension.mybatis.multiDataSource中没有为数据源配置名称");

                    //设置默认数据源为master
                    this.setDataSource("master");
                    this.getMultiDataSource()
                        .get(0)
                        .setName(this.getDataSource());
                }

            }
        }

        return getDataSourceConfig(this.getDataSource());
    }

    /**
     * 获取指定的数据源配置
     *
     * @param dataSource 数据源名称
     */
    public DataSourceConfig getDataSourceConfig(String dataSource)
            throws
            ApplicationException {
        if (multiDataSource == null)
            multiDataSource = new ArrayList<>();

        if (multiDataSource.size() == 0) {
            DataSourceConfig masterConfig = new DataSourceConfig();
            masterConfig.setName(dataSource);
            masterConfig.setDbType(this.getDbType());
            masterConfig.setConnectionString(this.getConnectionString());
            masterConfig.setUsername(this.getUsername());
            masterConfig.setPassword(this.getPassword());
            masterConfig.setNameConvertType(this.getNameConvertType());
            masterConfig.setConfigLocation(this.getConfigLocation());
            masterConfig.setEnable(true);
            multiDataSource.add(masterConfig);
            return masterConfig;
        }

        AtomicInteger count = new AtomicInteger();
        Optional<DataSourceConfig> matchConfig = multiDataSource.stream()
                                                                .filter(x -> {
                                                                    if (dataSource.equals(x.getName())) {
                                                                        count.getAndIncrement();
                                                                        return true;
                                                                    } else
                                                                        return false;
                                                                })
                                                                .findAny();
        if (!matchConfig.isPresent())
            throw new ApplicationException(String.format("配置文件project.extension.mybatis.multiDataSource中未找到名称为%s的数据源配置",
                                                         dataSource));

        if (count.get() > 1)
            throw new ApplicationException(String.format("配置文件project.extension.mybatis.multiDataSource中存在多个名称为%s的数据源",
                                                         dataSource));

        DataSourceConfig config = matchConfig.get();

//        if (!config.isEnable())
//            throw new ApplicationException(String.format("未启用配置文件的project.extension.mybatis.multiDataSource.%s数据源",
//                                                         dataSource));

        if (config.getDbType() == null)
            config.setDbType(this.getDbType());

        if (config.getDbType() == null)
            throw new ApplicationException(String.format("配置文件project.extension.mybatis.multiDataSource - name为%s的数据源缺失dbType设置，并且也没有设置默认的dbType",
                                                         dataSource));

        if (config.getNameConvertType() == null)
            config.setNameConvertType(this.getNameConvertType());

        if (config.getNameConvertType() == null)
            throw new ApplicationException(String.format("配置文件project.extension.mybatis.multiDataSource - name为%s的数据源缺失nameConvertType设置，并且也没有设置默认的nameConvertType",
                                                         dataSource));

        if (!StringUtils.hasText(config.getConfigLocation()))
            config.setConfigLocation(this.getConfigLocation());

        if (!StringUtils.hasText(config.getConfigLocation()))
            throw new ApplicationException(String.format("配置文件project.extension.mybatis.multiDataSource - name为%s的数据源缺失configLocation设置，并且也没有设置默认的configLocation",
                                                         dataSource));

        if (!StringUtils.hasText(config.getUsername()))
            config.setUsername(this.getUsername());

        if (!StringUtils.hasText(config.getUsername()))
            throw new ApplicationException(String.format("配置文件project.extension.mybatis.multiDataSource - name为%s的数据源缺失username设置，并且也没有设置默认的username",
                                                         dataSource));

        if (!StringUtils.hasText(config.getPassword()))
            config.setPassword(this.getPassword());

        if (!StringUtils.hasText(config.getPassword()))
            throw new ApplicationException(String.format("配置文件project.extension.mybatis.multiDataSource - name为%s的数据源缺失password设置，并且也没有设置默认的password",
                                                         dataSource));

        if (!StringUtils.hasText(config.getConnectionString()))
            config.setConnectionString(this.getConnectionString());

        if (!StringUtils.hasText(config.getConnectionString()))
            throw new ApplicationException(String.format("配置文件project.extension.mybatis.multiDataSource - name为%s的数据源缺失connectionString设置，并且也没有设置默认的connectionString",
                                                         dataSource));

        return config;
    }
}
