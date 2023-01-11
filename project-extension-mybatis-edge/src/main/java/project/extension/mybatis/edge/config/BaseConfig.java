package project.extension.mybatis.edge.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.core.ado.INaiveDataSourceProvider;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.standard.exception.ModuleException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基础配置
 *
 * @author LCTR
 * @date 2022-03-28
 */
@Primary
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
     * 默认的实体类表名/列名命名规则
     */
    private NameConvertType nameConvertType = NameConvertType.None;

    /**
     * mybatis配置文件路径
     */
    private String configLocation;

    /**
     * 多数据源配置
     */
    private Map<String, DataSourceConfig> multiDataSource;

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
     * 多库配置
     */
    public Map<String, DataSourceConfig> getMultiDataSource() {
        return multiDataSource;
    }

    public void setMultiDataSource(Map<String, DataSourceConfig> multi) {
        this.multiDataSource = multi;
    }

    /**
     * 是否为多数据源
     */
    public boolean isMultiDataSource() {
        return multiDataSource != null && multiDataSource.size() > 1;
    }

    /**
     * 获取所有数据源名称
     */
    public List<String> getAllDataSource() {
        this.getDataSourceConfig();
        return new ArrayList<>(this.getMultiDataSource()
                                   .keySet());
    }

    /**
     * 获取默认的数据源配置
     */
    public DataSourceConfig getDataSourceConfig() {
        if (!StringUtils.hasText(this.getDataSource())) {
            if (this.getMultiDataSource() == null || this.getMultiDataSource()
                                                         .size() == 0) {
                //设置默认数据源为master
                this.setDataSource(INaiveDataSourceProvider.DEFAULT_DATASOURCE);
            } else {
                //设置默认数据源为多库中的第一个
                DataSourceConfig firstDataSource = CollectionsExtension.firstValue(this.getMultiDataSource());
                assert firstDataSource != null;
                this.setDataSource(firstDataSource.getName());

                if (!StringUtils.hasText(this.getDataSource())) {
                    if (this.getMultiDataSource()
                            .size() > 1)
                        throw new ModuleException(Strings.getConfigDataSourceNameUndefined());

                    //设置默认数据源为master
                    this.setDataSource(INaiveDataSourceProvider.DEFAULT_DATASOURCE);
                    firstDataSource.setName(this.getDataSource());
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
            ModuleException {
        if (multiDataSource == null || multiDataSource.size() == 0)
            throw new ModuleException(Strings.getConfigDataSourceUndefined(dataSource));

//        if (multiDataSource.size() == 0) {

//            DataSourceConfig defaultConfig = new DataSourceConfig();
//            defaultConfig.setName(dataSource);
//            defaultConfig.setDbType(this.getDbType());
//            defaultConfig.getProperties()
//                         .put(DruidDataSourceFactory.PROP_URL,
//                              this.getUrl());
//            defaultConfig.getProperties()
//                         .put(DruidDataSourceFactory.PROP_USERNAME,
//                              this.getUsername());
//            defaultConfig.getProperties()
//                         .put(DruidDataSourceFactory.PROP_PASSWORD,
//                              this.getPassword());
//            defaultConfig.setNameConvertType(this.getNameConvertType());
//            defaultConfig.setConfigLocation(this.getConfigLocation());
//            defaultConfig.setEnable(true);
//            multiDataSource.put(dataSource,
//                                defaultConfig);
//            return defaultConfig;
//        }

        AtomicInteger count = new AtomicInteger();
        Optional<String> matchConfig = multiDataSource.keySet()
                                                      .stream()
                                                      .filter(x -> {
                                                          if (dataSource.equals(x)) {
                                                              count.getAndIncrement();
                                                              return true;
                                                          } else
                                                              return false;
                                                      })
                                                      .findAny();
        if (!matchConfig.isPresent())
            throw new ModuleException(Strings.getConfigDataSourceUndefined(dataSource));

        if (count.get() > 1)
            throw new ModuleException(Strings.getConfigDataSourceRepeat(dataSource));

        DataSourceConfig config = multiDataSource.get(matchConfig.get());

        config.setName(matchConfig.get());

//        if (!config.isEnable())
//            throw new ApplicationException(DbContextStrings.getConfigDataSourceNotActive(dataSource));

//        if (config.getDbType() == null)
//            config.setDbType(this.getDbType());

        if (config.getDbType() == null)
            throw new ModuleException(Strings.getConfigDataSourceOptionUndefined(dataSource,
                                                                                 "dbType"));

        if (config.getNameConvertType() == null)
            config.setNameConvertType(this.getNameConvertType());

        if (!StringUtils.hasText(config.getConfigLocation()))
            config.setConfigLocation(this.getConfigLocation());

        if (!StringUtils.hasText(config.getConfigLocation()))
            throw new ModuleException(Strings.getConfigDataSourceOptionUndefined(dataSource,
                                                                                 "configLocation"));
//
//        config.getProperties()
//              .computeIfAbsent(DruidDataSourceFactory.PROP_URL,
//                               k -> this.getUrl());
//
//        config.getProperties()
//              .computeIfAbsent(DruidDataSourceFactory.PROP_USERNAME,
//                               k -> this.getUsername());
//
//        config.getProperties()
//              .computeIfAbsent(DruidDataSourceFactory.PROP_PASSWORD,
//                               k -> this.getPassword());


        //检查必须要有的的属性
        List<String> requiredProperties = Arrays.asList(DruidDataSourceFactory.PROP_URL,
                                                        DruidDataSourceFactory.PROP_USERNAME,
                                                        DruidDataSourceFactory.PROP_PASSWORD);
        for (String property : requiredProperties) {
            if (config.getProperties()
                      .get(property) == null
                    || !StringUtils.hasText((String) config.getProperties()
                                                           .get(property)))
                throw new ModuleException(Strings.getConfigDataSourceOptionUndefined(dataSource,
                                                                                     property));
        }

        return config;
    }
}
