package project.extension.mybatis.edge.globalization;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串资源zh_CN
 *
 * @author LCTR
 * @date 2022-01-09
 */
public class Strings_zh_CN {
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
                       "已开启事务，不能禁用工作单元");
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
                       "SqlSessionFactory必须使用SpringManagedTransactionFactory才能使用Spring事务同步");
            values.put("CloseSqlSessionFailed",
                       "关闭Sql会话失败");
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
            values.put("FormatDateFailed",
                       "格式化日期{0}到{1}格式失败");
            values.put("DateFormatNonStandard",
                       "{0} 要求 {1} 格式必须为：{2}其中一种");
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
                       "获取{0}对象中{1}字段的值失败");
            values.put("GetObjectFieldValueFailed",
                       "未在{0}实体中找到{1}字段");
            values.put("UnknownValue",
                       "{0}的值{1}无效");
            values.put("TypeFieldOnlySupportOperation",
                       "{0}类型的字段只支持{1}操作");
            values.put("UnSupportOperation",
                       "不支持{0}操作");
            values.put("UnSupportOperationSymbol",
                       "不支持{0}操作符");
            values.put("UnSupportTargetType",
                       "不支持{0}目标操作类型");
            values.put("DataUndefined",
                       "数据不存在或已被移除");
            values.put("UseMasterOrmMethod",
                       "请使用getMasterOrm方法获取主库ORM");


            values.put("RowsDataException",
                       "数据库受影响行数有误，{0}");
            values.put("QueryDataFailed",
                       "查询数据失败");
            values.put("InsertDataFailed",
                       "插入数据失败");
            values.put("UpdateDataFailed",
                       "更新数据失败");
            values.put("DeleteDataFailed",
                       "删除数据失败");
            values.put("DeleteOperationNeedDataOrCondition",
                       "删除数据时如果未设置数据源，那么则必须设置WHERE条件");
            values.put("CanNotGetDbNameFromUrl",
                       "无法从连接字符串中获取数据库名称，匹配所使用的正则表达式为：{0}");
            values.put("SqlServerRequireOrderBy4Paging",
                       "在SQL Server数据库种进行分页查询时必须指定ORDER BY语句");
        }

        return values.get(code);
    }
}
