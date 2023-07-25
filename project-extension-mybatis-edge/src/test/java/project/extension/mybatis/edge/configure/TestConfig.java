package project.extension.mybatis.edge.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.model.NameConvertType;

import java.util.*;

/**
 * 基础配置
 *
 * @author LCTR
 * @date 2022-03-28
 */
@Component
@ConfigurationProperties("project.extension.mybatis")
@Data
public class TestConfig {
    /**
     * 默认的数据源
     */
    private String dataSource;

    /**
     * 默认的mybatis配置文件路径
     */
    private String configLocation;

    /**
     * 默认的需要扫描的存放实体类的包（包括TypeAliasesPackage）
     */
    private List<String> scanEntitiesPackages;

    /**
     * 需要扫描的存放Mapper接口类的包（可选）
     */
    private List<String> scanMapperPackages;

    /**
     * 需要扫描的存放Mapper配置文件的目录
     */
    private List<String> scanMapperXmlLocations;

    /**
     * 默认的实体类表名/列名命名规则
     */
    private NameConvertType nameConvertType = NameConvertType.CamelCaseToPascalCase;

    /**
     * 多数据源配置
     */
    private Map<String, DataSourceConfig> multiDataSource;

    /**
     * 启用分页插件
     */
    private boolean enablePageHelper;

    /**
     * 分页插件配置
     */
    public Properties pageHelperProperties;
}
