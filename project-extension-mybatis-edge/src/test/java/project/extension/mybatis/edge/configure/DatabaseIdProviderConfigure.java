package project.extension.mybatis.edge.configure;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis多数据源配置
 *
 * @author LCTR
 * @date 2023-02-20
 */
@Configuration
public class DatabaseIdProviderConfigure {
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
