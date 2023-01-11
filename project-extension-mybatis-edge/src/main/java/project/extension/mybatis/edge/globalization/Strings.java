package project.extension.mybatis.edge.globalization;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 字符串资源
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class Strings {
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
        return Strings.locale;
    }

    /**
     * 设置本地化信息
     * <p>设置中文：Locale.SIMPLIFIED_CHINESE</p>
     * <p>设置英文：Locale.US</p>
     *
     * @param locale 本土化信息
     */
    public static void setLocale(Locale locale) {
        Strings.locale = locale;
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
     * 事务早已开启，请检查是否同时使用了@Transactional和INaiveSql.transaction
     */
    public static String getTransactionAlreadyStarted() {
        return getString("TransactionAlreadyStarted");
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
     * 格式化日期失败
     *
     * @param value  日期值
     * @param format 格式
     */
    public static String getFormatDateFailed(String value,
                                             String format) {
        return getString("FormatDateFailed",
                         value,
                         format);
    }

    /**
     * 日期格式不规范
     *
     * @param object 指定对象
     * @param value  指定值
     * @param format 格式
     */
    public static String getDateFormatNonStandard(String object,
                                                  String value,
                                                  String format) {
        return getString("DateFormatNonStandard",
                         object,
                         value,
                         format);
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
     * 获取对象中指定字段的值失败
     *
     * @param typeName  类型名称
     * @param fieldName 字段名称
     */
    public static String getGetObjectFieldValueFailed(String typeName,
                                                      String fieldName) {
        return getString("GetObjectFieldValueFailed",
                         typeName,
                         fieldName);
    }

    /**
     * 无效的值
     *
     * @param name  值名称
     * @param value 值
     */
    public static String getUnknownValue(String name,
                                         String value) {
        return getString("UnknownValue",
                         name,
                         value);
    }

    /**
     * 指定类型的字段只支持此操作
     *
     * @param typeField 类型字段
     * @param operation 操作
     */
    public static String getTypeFieldOnlySupportOperation(String typeField,
                                                          String operation) {
        return getString("TypeFieldOnlySupportOperation",
                         typeField,
                         operation);
    }

    /**
     * 不支持此操作
     *
     * @param operation 操作
     */
    public static String getUnSupportOperation(String operation) {
        return getString("UnSupportOperation",
                         operation);
    }

    /**
     * 不支持此操作符
     *
     * @param operation 操作
     */
    public static String getUnSupportOperationSymbol(String operation) {
        return getString("UnSupportOperationSymbol",
                         operation);
    }

    /**
     * 不支持此目标操作类型
     *
     * @param type 类型
     */
    public static String getUnSupportTargetType(String type) {
        return getString("UnSupportTargetType",
                         type);
    }

    /**
     * 数据不存在或已被移除
     */
    public static String getDataUndefined() {
        return getString("DataUndefined");
    }

    /**
     * 请使用getMasterOrm方法获取主库ORM
     */
    public static String getUseMasterOrmMethod() {
        return getString("UseMasterOrmMethod");
    }

    /**
     * 数据库受影响行数有误
     *
     * @param rows 数据库受影响行数
     */
    public static String getRowsDataException(int rows) {
        return getString("RowsDataException",
                         rows);
    }

    /**
     * 查询数据失败
     */
    public static String getQueryDataFailed() {
        return getString("QueryDataFailed");
    }

    /**
     * 插入数据失败
     */
    public static String getInsertDataFailed() {
        return getString("InsertDataFailed");
    }

    /**
     * 更新数据失败
     */
    public static String getUpdateDataFailed() {
        return getString("UpdateDataFailed");
    }

    /**
     * 删除数据失败
     */
    public static String getDeleteDataFailed() {
        return getString("DeleteDataFailed");
    }

    /**
     * 删除数据时如果未设置数据源，那么则必须设置WHERE条件
     */
    public static String getDeleteOperationNeedDataOrCondition() {
        return getString("DeleteOperationNeedDataOrCondition");
    }

    /**
     * 无法从连接字符串中获取数据库名称
     *
     * @param pattern 使用的正则表达式
     */
    public static String getCanNotGetDbNameFromUrl(String pattern) {
        return getString("CanNotGetDbNameFromUrl",
                         pattern);
    }

    /**
     * 在SQL Server数据库种进行分页查询时必须指定ORDER BY语句
     */
    public static String getSqlServerRequireOrderBy4Paging() {
        return getString("SqlServerRequireOrderBy4Paging");
    }

}
