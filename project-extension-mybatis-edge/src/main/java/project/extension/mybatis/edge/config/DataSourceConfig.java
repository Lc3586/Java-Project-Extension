package project.extension.mybatis.edge.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.Data;
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
@Data
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
     * 需要扫描的存放Mapper配置文件的目录
     */
    private List<String> scanMapperXmlLocations;

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
}
