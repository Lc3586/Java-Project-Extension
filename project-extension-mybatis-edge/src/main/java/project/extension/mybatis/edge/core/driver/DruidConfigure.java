package project.extension.mybatis.edge.core.driver;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.config.DruidConfig;

/**
 * 配置连接池
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Configuration
public class DruidConfigure {
    public DruidConfigure(BaseConfig baseConfig,
                          DruidConfig druidConfig,
                          ApplicationContext applicationContext) {
        this.baseConfig = baseConfig;
        this.druidConfig = druidConfig;
        this.applicationContext = applicationContext;
    }

    /**
     * 基础配置
     */
    private final BaseConfig baseConfig;

    /**
     * 连接池配置
     */
    private final DruidConfig druidConfig;

    /**
     * 应用上下文
     */
    private final ApplicationContext applicationContext;

    /**
     * 注册数据源
     */
    @Autowired
    public void registerDataSource() {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) context.getBeanFactory();

        for (String dataSource : baseConfig.getAllDataSource()) {
            DataSourceConfig dataSourceConfig = baseConfig.getDataSourceConfig(dataSource);
            DruidDataSource druidDataSource = DruidDataSourceBuilder.create()
                                                                    .build();
            dataSourceConfig.applyConfig(druidDataSource);
            druidConfig.applyConfig(druidDataSource,
                                    dataSourceConfig.getDbType());

            //注入为单例
            defaultListableBeanFactory.registerSingleton(
                    String.format("MybatisDataSource-%s",
                                  dataSourceConfig.getName()),
                    druidDataSource);
        }
    }
}
