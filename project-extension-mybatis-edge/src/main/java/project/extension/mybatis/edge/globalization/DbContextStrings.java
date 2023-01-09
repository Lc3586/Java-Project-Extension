package project.extension.mybatis.edge.globalization;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * DbContext功能模块部分所使用的字符串资源
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class DbContextStrings {
    /**
     * 本土化信息
     */
    private static Locale locale = LocaleContextHolder.getLocale();

    /**
     * 获取当前的本地化信息
     *
     * @return 本土化信息
     */
    public static Locale getLocale() {
        return DbContextStrings.locale;
    }

    /**
     * 设置本地化信息
     * <p>设置中文：Locale.SIMPLIFIED_CHINESE</p>
     * <p>设置英文：Locale.US</p>
     *
     * @param locale 本土化信息
     */
    public static void setLocale(Locale locale) {
        DbContextStrings.locale = locale;
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
        String message;
        if (Locale.CHINESE.equals(locale)
                || Locale.SIMPLIFIED_CHINESE.equals(locale)
                || Locale.TRADITIONAL_CHINESE.equals(locale))
            message = String.format(Strings_zh_CN.getValue(code),
                                    args);
        else
            message = String.format(Strings_en_US.getValue(code),
                                    args);

        return String.format("MybatisEdge: %s",
                             message);
    }

    /**
     * 方法尚未实现
     */
    public static String getFunctionNotImplemented() {
        return getString("FunctionNotImplemented");
    }

    /**
     * 已开启事务，不能禁用工作单元
     */
    public static String getTransactionHasBeenStarted() {
        return getString("TransactionHasBeenStarted");
    }

    /**
     * 开启事务失败
     */
    public static String getTransactionBeginFailed() {
        return getString("TransactionBeginFailed");
    }

    /**
     * 提交事务失败
     */
    public static String getTransactionCommitFailed() {
        return getString("TransactionCommitFailed");
    }

    /**
     * 回滚事务失败
     */
    public static String getTransactionRollbackFailed() {
        return getString("TransactionRollbackFailed");
    }

    /**
     * 检查Sql会话是否已关闭失败
     */
    public static String getSqlSessionCheckClosedFailed() {
        return getString("SqlSessionCheckClosedFailed");
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

    /**
     * 未找到指定的实体
     *
     * @param entityName 实体名称
     */
    public static String getEntityUndefined(String entityName) {
        return getString("EntityUndefined",
                         entityName);
    }

    /**
     * 初始化实体数据失败
     */
    public static String getEntityInitializationFailed() {
        return getString("EntityInitializationFailed");
    }

    /**
     * 实体中未定义主键
     */
    public static String getEntityPrimaryKeyUndefined() {
        return getString("EntityPrimaryKeyUndefined");
    }

    /**
     * 实体存在联合主键时，主键类型必须为Tuple元组类型
     */
    public static String getEntityCompositePrimaryKeyMustBeTupleType() {
        return getString("EntityCompositePrimaryKeyMustBeTupleType");
    }

    /**
     * 未在实体中找到指定列对应的字段
     *
     * @param entityName 实体名称
     * @param columnName 列名称
     */
    public static String getEntityField4ColumnUndefined(String entityName,
                                                        String columnName) {
        return getString("EntityField4ColumnUndefined",
                         entityName,
                         columnName);
    }

    /**
     * 未在实体中找到指定的字段
     *
     * @param entityName 实体名称
     * @param fieldName  字段名称
     */
    public static String getEntityFieldUndefined(String entityName,
                                                 String fieldName) {
        return getString("EntityFieldUndefined",
                         entityName,
                         fieldName);
    }


    /**
     * 数据不存在或已被移除
     */
    public static String getDataUndefined() {
        return getString("DataUndefined");
    }
}
