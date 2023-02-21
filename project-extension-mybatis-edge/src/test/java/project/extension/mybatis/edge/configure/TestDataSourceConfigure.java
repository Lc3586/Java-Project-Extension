package project.extension.mybatis.edge.configure;

import java.util.LinkedHashMap;

/**
 * 测试数据源配置
 *
 * @author LCTR
 * @date 2023-02-17
 */
public class TestDataSourceConfigure {
    /**
     * 全部的测试数据源
     */
    private static final LinkedHashMap<String, String> multiDataSourceTestMap = new LinkedHashMap<>();

    static {
        multiDataSourceTestMap.put("MySQL 8.0",
                                   "mysql");
        multiDataSourceTestMap.put("MariaDB 10.10",
                                   "mariadb");
        multiDataSourceTestMap.put("SqlServer 2012 降级处理",
                                   "sqlserver");
        multiDataSourceTestMap.put("SqlServer 2012",
                                   "sqlserver2012");
        multiDataSourceTestMap.put("达梦 8",
                                   "dameng");
        multiDataSourceTestMap.put("Oracle 19c",
                                   "oracle");
        multiDataSourceTestMap.put("PostgreSQL 15",
                                   "postgresql");
    }

    /**
     * 获取全部用于测试的数据源名称集合
     */
    public static String[] getMultiTestDataSourceName() {
        return multiDataSourceTestMap.keySet()
                                     .toArray(new String[0]);
    }

    /**
     * 获取用于测试的数据源
     *
     * @param name 名称
     */
    public static String getTestDataSource(String name) {
        return multiDataSourceTestMap.get(name);
    }
}
