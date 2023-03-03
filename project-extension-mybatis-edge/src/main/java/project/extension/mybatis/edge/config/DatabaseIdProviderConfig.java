package project.extension.mybatis.edge.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 数据源配置
 *
 * @author LCTR
 * @date 2023-03-03
 */
@Configuration
public class DatabaseIdProviderConfig {
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MySQL",
                               "mysql");
        properties.setProperty("MariaDB",
                               "mariadb");
        properties.setProperty("SQL Server",
                               "sqlserver");
        properties.setProperty("DM DBMS",
                               "dm");
        properties.setProperty("Oracle",
                               "oracle");
        properties.setProperty("PostgreSQL",
                               "postgresql");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
