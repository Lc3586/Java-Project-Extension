package project.extension.mybatis.edge.globalization;

import org.springframework.context.MessageSource;
import project.extension.ioc.IOCExtension;

import java.util.Locale;

/**
 * DbContext功能模块部分所使用的字符串资源
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class DbContextStrings {
    /**
     * 信息源
     */
    private static final MessageSource messageSource = IOCExtension.applicationContext.getBean(MessageSource.class);

    /**
     * 本土化信息
     */
    private static Locale resourceLocale;

    /**
     * 获取当前的本地化信息
     *
     * @return 本土化信息
     */
    public static Locale getResourceLocale() {
        return resourceLocale;
    }

    /**
     * 设置本地化信息
     *
     * @param locale 本土化信息
     */
    public static void setResourceLocale(Locale locale) {
        resourceLocale = locale;
    }

    /**
     * 获取字符串
     *
     * @param code 编码
     * @param args 参数
     * @return 字符串
     */
    public static String getString(String code,
                                   Object... args) {
        return String.format("MybatisEdge: %s",
                             messageSource.getMessage(code,
                                                      args,
                                                      resourceLocale));
    }

    /**
     * 已开启事务，不能禁用工作单元
     */
    public static String transactionHasBeenStarted() {
        return getString("TransactionHasBeenStarted");
    }

    /**
     * 获取Sql会话工厂失败
     */
    public static String getSqlSessionFactoryFailed() {
        return getString("SqlSessionFactoryFailed");
    }

    /**
     * 在配置文件中没有为数据源配置名称
     */
    public static String getConfigDataSourceNameUndefined() {
        return getString("ConfigDataSourceNameUndefined");
    }

    /**
     * 找不到指定的数据源配置
     *
     * @param name 数据源名称
     */
    public static String getConfigDataSourceUndefined(String name) {
        return getString("ConfigDataSourceUndefined",
                         name);
    }

    /**
     * 数据源配置重复
     *
     * @param name 数据源名称
     */
    public static String getConfigDataSourceRepeat(String name) {
        return getString("ConfigDataSourceRepeat",
                         name);
    }

    /**
     * 数据源配置未启用
     *
     * @param name 数据源名称
     */
    public static String getConfigDataSourceNotActive(String name) {
        return getString("ConfigDataSourceNotActive",
                         name);
    }

    /**
     * 数据源配置项缺失
     *
     * @param name   数据源名称
     * @param option 配置项名称
     */
    public static String getConfigDataSourceOptionUndefined(String name,
                                                            String option) {
        return getString("ConfigDataSourceOptionUndefined",
                         name,
                         option);
    }

    /**
     * 创建指定的数据源失败
     *
     * @param name 数据源名称
     */
    public static String getCreateDataSourceFailed(String name) {
        return getString("CreateDataSourceFailed",
                         name);
    }

    /**
     * 不支持的数据库类型
     *
     * @param dbType 数据库类型
     */
    public static String getUnsupportedDbType(String dbType) {
        return getString("UnsupportedDbType",
                         dbType);
    }

    /**
     * 创建实例失败
     *
     * @param instanceName 实例名称
     */
    public static String getCreateInstanceFailed(String instanceName) {
        return getString("CreateInstanceFailed",
                         instanceName);
    }

    /**
     * 实例参数缺失
     *
     * @param instanceName 实例名称
     * @param params       参数名称
     */
    public static String getInstanceParamsUndefined(String instanceName,
                                                    String params) {
        return getString("InstanceParamsUndefined",
                         instanceName,
                         params);
    }

    /**
     * 不支持的日期数据类型
     *
     * @param typeName 类型名称
     */
    public static String getUnsupportedDateType(String typeName) {
        return getString("UnsupportedDateType",
                         typeName);
    }

    /**
     * 初始化实体数据失败
     */
    public static String getEntityInitializationFailed() {
        return getString("EntityInitializationFailed");
    }

    /**
     * 不支持的主键数据类型
     *
     * @param typeName 类型名称
     */
    public static String getUnsupportedDataType4PrimaryKey(String typeName) {
        return getString("UnsupportedDataType4PrimaryKey",
                         typeName);
    }

    /**
     * 设置操作时间失败
     */
    public static String getSetupOperatorTimeFailed() {
        return getString("SetupOperatorTimeFailed");
    }

    /**
     * SqlSessionFactory必须使用SpringManagedTransactionFactory才能使用Spring事务同步
     */
    public static String getNoneSpringManagedTransactionFactory() {
        return getString("NoneSpringManagedTransactionFactory");
    }

    /**
     * 获取事务失败
     */
    public static String getResolveTransactionFailed() {
        return getString("ResolveTransactionFailed");
    }
}
