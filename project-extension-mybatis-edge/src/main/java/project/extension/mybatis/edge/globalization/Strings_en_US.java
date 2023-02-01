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
            values.put("TransactionAlreadyStarted",
                       "事务早已开启，请检查是否同时使用了@Transactional和INaiveSql.transaction，或是嵌套了多层事务");
            values.put("BeginTransaction",
                       "开启事务");
            values.put("TransactionBeginFailed",
                       "开启事务失败");
            values.put("TransactionCommit",
                       "提交事务");
            values.put("TransactionCommitFailed",
                       "提交事务失败");
            values.put("TransactionRollback",
                       "回滚事务");
            values.put("TransactionRollbackFailed",
                       "回滚事务失败");
            values.put("NoneSpringManagedTransactionFactory",
                       "SqlSessionFactory must be using a SpringManagedTransactionFactory in order to use Spring transaction synchronization");
            values.put("CloseSqlSessionFailed",
                       "关闭Sql会话失败");
            values.put("SqlSessionCheckClosedFailed",
                       "检查Sql会话是否已关闭失败");
            values.put("SqlSessionFactoryFailed",
                       "获取Sql会话工厂失败");
            values.put("ConfigDataSourceNameUndefined",
                       "在配置文件的project.extension.mybatis.multiDataSource中没有为数据源配置名称");
            values.put("ConfigDataSourceUndefined",
                       "配置文件project.extension.mybatis.multiDataSource中未找到名称为%1$2s的数据源配置");
            values.put("ConfigDataSourceRepeat",
                       "配置文件project.extension.mybatis.multiDataSource中存在多个名称为%1$2s的数据源");
            values.put("ConfigDataSourceNotActive",
                       "未启用配置文件中的project.extension.mybatis.multiDataSource.%1$2s数据源");
            values.put("ConfigDataSourceOptionUndefined",
                       "配置文件project.extension.mybatis.multiDataSource - name为%1$2s的数据源缺失%2$2s设置，并且也没有设置默认的%2$2s");
            values.put("CreateDataSourceFailed",
                       "创建数据源失败，配置来源于project.extension.mybatis.multiDataSource中名称为%1$2s的数据源配置");
            values.put("UnsupportedDbType",
                       "暂不支持%1$2s数据库");
            values.put("CreateInstanceFailed",
                       "创建%1$2s实例失败");
            values.put("InstanceParamsUndefined",
                       "%1$2s构造函数%2$2s参数不可为空");
            values.put("UnsupportedDateType",
                       "暂不支持%1$2s类型的日期");
            values.put("FormatDateFailed",
                       "格式化日期%1$2s到%2$2s格式失败");
            values.put("DateFormatNonStandard",
                       "%1$2s 要求 %2$2s 格式必须为：%3$2s其中一种");
            values.put("UnsupportedDataType4PrimaryKey",
                       "暂不支持设置%1$2s数据类型的主键");
            values.put("SetupOperatorTimeFailed",
                       "设置操作时间失败");
            values.put("ResolveTransactionFailed",
                       "获取事务失败");
            values.put("EntityUndefined",
                       "未找到%1$2s实体");
            values.put("EntityInitializationFailed",
                       "初始化实体数据失败");
            values.put("EntityPrimaryKeyUndefined",
                       "请在实体里设置主键@ColumnSetting(primaryKey = true)");
            values.put("EntityCompositePrimaryKeyMustBeTupleType",
                       "实体存在联合主键时，主键类型必须为Tuple元组类型");
            values.put("EntityField4ColumnUndefined",
                       "未在%1$2s实体中找到%2$2s列对应的字段");
            values.put("EntityFieldUndefined",
                       "未在%1$2s实体中找到%2$2s字段");
            values.put("GetObjectFieldValueFailed",
                       "未在%1$2s实体中找到%2$2s字段");
            values.put("UnknownValue",
                       "%1$2s的值%2$2s无效");
            values.put("TypeFieldOnlySupportOperation",
                       "%1$2s类型的字段只支持%2$2s操作");
            values.put("UnSupportOperation",
                       "不支持%1$2s操作");
            values.put("UnSupportOperationSymbol",
                       "不支持%1$2s操作符");
            values.put("UnSupportTargetType",
                       "不支持%1$2s目标操作类型");
            values.put("DataUndefined",
                       "数据不存在或已被移除");
            values.put("UseMasterOrmMethod",
                       "请使用getMasterOrm方法获取主库ORM");


            values.put("RowsDataException",
                       "rows data exception，%1$2s");
            values.put("QueryDataFailed",
                       "query data failed");
            values.put("InsertDataFailed",
                       "insert data failed");
            values.put("UpdateDataFailed",
                       "update data failed");
            values.put("DeleteDataFailed",
                       "delete data failed");
            values.put("DeleteOperationNeedDataOrCondition",
                       "delete operation needs to set data or where condition");
            values.put("CanNotGetDbNameFromUrl",
                       "无法从连接字符串中获取数据库名称，匹配所使用的正则表达式为：%1$2s");
            values.put("SqlServerRequireOrderBy4Paging",
                       "在SQL Server数据库种进行分页查询时必须指定ORDER BY语句");
        }
        return values.get(code);
    }
}
