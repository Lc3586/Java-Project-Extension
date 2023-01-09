package project.extension.mybatis.edge.globalization;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串资源en_US
 *
 * @author LCTR
 * @date 2022-01-09
 */
public class Strings_en_US {
    private static Map<String, String> values = null;

    /**
     * 获取值
     *
     * @param code 编码
     * @return 值
     */
    public static String getValue(String code) {
        if (values == null) {
            values = new HashMap<>();
            values.put("FunctionNotImplemented",
                       "方法尚未实现");
            values.put("TransactionHasBeenStarted",
                       "Transaction opened, unit of work cannot be disabled");
            values.put("TransactionBeginFailed",
                       "开启事务失败");
            values.put("TransactionCommitFailed",
                       "提交事务失败");
            values.put("TransactionRollbackFailed",
                       "回滚事务失败");
            values.put("NoneSpringManagedTransactionFactory",
                       "SqlSessionFactory must be using a SpringManagedTransactionFactory in order to use Spring transaction synchronization");
            values.put("SqlSessionCheckClosedFailed",
                       "检查Sql会话是否已关闭失败");
            values.put("SqlSessionFactoryFailed",
                       "获取Sql会话工厂失败");
            values.put("ConfigDataSourceNameUndefined",
                       "在配置文件的project.extension.mybatis.multiDataSource中没有为数据源配置名称");
            values.put("ConfigDataSourceUndefined",
                       "配置文件project.extension.mybatis.multiDataSource中未找到名称为{0}的数据源配置");
            values.put("ConfigDataSourceRepeat",
                       "配置文件project.extension.mybatis.multiDataSource中存在多个名称为{0}的数据源");
            values.put("ConfigDataSourceNotActive",
                       "未启用配置文件中的project.extension.mybatis.multiDataSource.{0}数据源");
            values.put("ConfigDataSourceOptionUndefined",
                       "配置文件project.extension.mybatis.multiDataSource - name为{0}的数据源缺失{1}设置，并且也没有设置默认的{1}");
            values.put("CreateDataSourceFailed",
                       "创建数据源失败，配置来源于project.extension.mybatis.multiDataSource中名称为{0}的数据源配置");
            values.put("UnsupportedDbType",
                       "暂不支持{0}数据库");
            values.put("CreateInstanceFailed",
                       "创建{0}实例失败");
            values.put("InstanceParamsUndefined",
                       "{0}构造函数{1}参数不可为空");
            values.put("UnsupportedDateType",
                       "暂不支持{0}类型的日期");
            values.put("UnsupportedDataType4PrimaryKey",
                       "暂不支持设置{0}数据类型的主键");
            values.put("SetupOperatorTimeFailed",
                       "设置操作时间失败");
            values.put("ResolveTransactionFailed",
                       "获取事务失败");
            values.put("EntityUndefined",
                       "未找到{0}实体");
            values.put("EntityInitializationFailed",
                       "初始化实体数据失败");
            values.put("EntityPrimaryKeyUndefined",
                       "请在实体里设置主键@ColumnSetting(primaryKey = true)");
            values.put("EntityCompositePrimaryKeyMustBeTupleType",
                       "实体存在联合主键时，主键类型必须为Tuple元组类型");
            values.put("EntityField4ColumnUndefined",
                       "未在{0}实体中找到{1}列对应的字段");
            values.put("EntityFieldUndefined",
                       "未在{0}实体中找到{1}字段");
            values.put("DataUndefined",
                       "数据不存在或已被移除");
        }
        return values.get(code);
    }

    public static String FunctionNotImplemented = "方法尚未实现";
    public static String TransactionHasBeenStarted = "Transaction opened, unit of work cannot be disabled";
    public static String TransactionBeginFailed = "开启事务失败";
    public static String TransactionCommitFailed = "提交事务失败";
    public static String TransactionRollbackFailed = "回滚事务失败";
    public static String SqlSessionCheckClosedFailed = "检查Sql会话是否已关闭失败";
    public static String SqlSessionFactoryFailed = "获取Sql会话工厂失败";
    public static String ConfigDataSourceNameUndefined = "在配置文件的project.extension.mybatis.multiDataSource中没有为数据源配置名称";
    public static String ConfigDataSourceUndefined = "配置文件project.extension.mybatis.multiDataSource中未找到名称为{0}的数据源配置";
    public static String ConfigDataSourceRepeat = "配置文件project.extension.mybatis.multiDataSource中存在多个名称为{0}的数据源";
    public static String ConfigDataSourceNotActive = "未启用配置文件中的project.extension.mybatis.multiDataSource.{0}数据源";
    public static String ConfigDataSourceOptionUndefined = "配置文件project.extension.mybatis.multiDataSource - name为{0}的数据源缺失{1}设置，并且也没有设置默认的{1}";
    public static String CreateDataSourceFailed = "创建数据源失败，配置来源于project.extension.mybatis.multiDataSource中名称为{0}的数据源配置";
    public static String UnsupportedDbType = "暂不支持{0}数据库";
    public static String CreateInstanceFailed = "创建{0}实例失败";
    public static String InstanceParamsUndefined = "{0}构造函数{1}参数不可为空";
    public static String UnsupportedDateType = "暂不支持{0}类型的日期";
    public static String UnsupportedDataType4PrimaryKey = "暂不支持设置{0}数据类型的主键";
    public static String SetupOperatorTimeFailed = "设置操作时间失败";
    public static String NoneSpringManagedTransactionFactory = "SqlSessionFactory must be using a SpringManagedTransactionFactory in order to use Spring transaction synchronization";
    public static String ResolveTransactionFailed = "获取事务失败";
    public static String EntityUndefined = "未找到{0}实体";
    public static String EntityInitializationFailed = "初始化实体数据失败";
    public static String EntityPrimaryKeyUndefined = "请在实体里设置主键@ColumnSetting(primaryKey = true)";
    public static String EntityCompositePrimaryKeyMustBeTupleType = "实体存在联合主键时，主键类型必须为Tuple元组类型";
    public static String EntityField4ColumnUndefined = "未在{0}实体中找到{1}列对应的字段";
    public static String EntityFieldUndefined = "未在{0}实体中找到{1}字段";
    public static String DataUndefined = "数据不存在或已被移除";
}
