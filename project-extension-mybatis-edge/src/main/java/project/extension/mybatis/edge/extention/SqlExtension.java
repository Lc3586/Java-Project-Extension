package project.extension.mybatis.edge.extention;

import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.model.DynamicMethod;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.string.StringExtension;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

/**
 * SQL语句相关拓展方法
 *
 * @author LCTR
 * @date 2022-03-22
 */
public class SqlExtension {
    /**
     * SQL注入验证正则
     */
    private static final Pattern injectionFilter = Pattern.compile(".*([';]+|(--)+).*",
                                                                   Pattern.CASE_INSENSITIVE);

    /**
     * 检查是否存在SQL注入
     *
     * @param value 需要检查的内容
     * @return 是否存在SQL注入
     */
    public static Boolean checkInjection(List<String> value) {
        return value.stream()
                    .anyMatch(SqlExtension::checkInjection);
    }

    /**
     * 检查是否存在SQL注入
     *
     * @param value 需要检查的内容
     * @return 是否存在SQL注入
     */
    public static Boolean checkInjection(String value) {
        return StringUtils.hasText(value) && injectionFilter.matcher(value)
                                                            .matches();
    }

    /**
     * 防止SQL注入
     *
     * @param value 需要检查的内容
     * @return 转以后的内容
     */
    public static String replaceInjection(String value) {
        return StringUtils.hasText(value)
               ? value.replaceAll(".*([';]+|(--)+).*",
                                  "")
               : value;
    }

    /**
     * 转换名称
     *
     * @param name            名称
     * @param nameConvertType 命名规则
     * @return 符合命名规则的值
     */
    public static String convertName(String name,
                                     NameConvertType nameConvertType) {
        switch (nameConvertType) {
            default:
            case None:
                return name;
            case PascalCaseToUnderscore:
                return StringExtension.pascalCaseToUnderScore(name);
            case PascalCaseToUnderscoreWithUpper:
                return StringExtension.pascalCaseToUnderScore(name)
                                      .toUpperCase(Locale.ROOT);
            case PascalCaseToUnderscoreWithLower:
                return StringExtension.pascalCaseToUnderScore(name)
                                      .toLowerCase(Locale.ROOT);
            case ToUpper:
                return name.toUpperCase(Locale.ROOT);
            case ToLower:
                return name.toLowerCase(Locale.ROOT);
        }
    }

    /**
     * 还原名称
     *
     * @param name            名称
     * @param nameConvertType 命名规则
     * @return 符合命名规则的值
     */
    public static String reductionName(String name,
                                       NameConvertType nameConvertType) {
        switch (nameConvertType) {
            default:
            case None:
                return name;
            case PascalCaseToUnderscore:
            case PascalCaseToUnderscoreWithUpper:
            case PascalCaseToUnderscoreWithLower:
                return StringExtension.underscoreToPascalCase(name);
            case ToUpper:
                return name.toLowerCase(Locale.ROOT);
            case ToLower:
                return name.toUpperCase(Locale.ROOT);
        }
    }

//    /**
//     * 获取数据库标识符
//     *
//     * @param dbType 数据库类型
//     * @return
//     */
//    public static char[] getDatabaseCharacter(DbType dbType) {
//        char[] result = new char[2];
//        switch (dbType) {
//            case OdbcMySql:
//                Arrays.fill(result, '`');
//                break;
//            case OdbcSqlServer:
//                result[0] = '[';
//                result[1] = ']';
//                break;
//            case OdbcOracle:
//            case OdbcDameng:
//            case OdbcPostgreSQL:
//            default:
//                Arrays.fill(result, '"');
//                break;
//        }
//        return result;
//    }
//
//    /**
//     * 获取名称
//     * <p>`id`</p>
//     *
//     * @param value  值
//     * @param dbType 数据库类型
//     * @return 名称
//     */
//    public static String getValueWithCharacter(String value, DbType dbType) {
//        //标识符
//        char[] character = getDatabaseCharacter(dbType);
//        return String.format("%s%s%s", character[0], value, character[1]);
//    }
//
//    /**
//     * 获取别名+名称
//     * <p>`a`.`id`</p>
//     *
//     * @param name   名称
//     * @param alias  别名
//     * @param dbType 数据库类型
//     * @return 别名+名称
//     */
//    public static String getNameWithAlias(String name, String alias, DbType dbType) {
//        return String.format("%s.%s", getValueWithCharacter(alias, dbType), getValueWithCharacter(name, dbType));
//    }
//
//    /**
//     * 获取字段的数据库列名
//     *
//     * @param fieldName 字段名称
//     * @param type      数据类型
//     * @return 数据库列名
//     */
//    public static String getColumnByFieldName(String fieldName, Class<?> type) {
//        Class<?> classz = SchemaExtention.getGenericType(type);
//
//        while (true) {
//            for (Field field : classz.getDeclaredFields()) {
//                if (field.getName().equals(fieldName)) {
//                    ColumnType columnAttribute = AnnotationUtils.findAnnotation(field, ColumnType.class);
//                    if (columnAttribute != null) return columnAttribute.column();
//                    return fieldName;
//                }
//            }
//
//            //递归处理父类类型
//            Class<?> superClassz = SchemaExtention.getSuperModelType(classz);
//            if (superClassz != null && !superClassz.equals(classz)) {
//                classz = superClassz;
//                continue;
//            }
//
//            break;
//        }
//        return fieldName;
//    }
//
//    /**
//     * 获取动态Sql语句
//     * <p>包含占位符的Sql语句</p>
//     *
//     * @param resultType 返回数据类型
//     * @param dbType     数据库类型
//     * @param parameter  参数
//     * @return 查询语句的列名部分
//     * @throws ClassNotFoundException 未找到指定类型
//     */
//    public static String getDynamicSql(Class<?> resultType, DbType dbType, Object parameter) throws Throwable {
//        String sql = "";
//
//        Map<String, Object> excutorParameterValues = ExecutorExtension.getParameterValues(parameter, true, ExecutorParameter.分页设置, ExecutorParameter.其他标签, ExecutorParameter.动态过滤条件, ExecutorParameter.动态排序条件, ExecutorParameter.自定义SQL, ExecutorParameter.自定义过滤SQL, ExecutorParameter.自定义排序SQL);
//
//        String alias;
//
//        //数据表别名
//        Alias aliasAttribute = AnnotationUtils.findAnnotation(resultType, Alias.class);
//        if (aliasAttribute != null) alias = aliasAttribute.value();
//        else alias = (String) CollectionsExtension.tryGet(excutorParameterValues, ExecutorParameter.数据表别名, "a").b;
//
//        //SELECT部分
//        String select;
//        Object otherTag = CollectionsExtension.tryGet(excutorParameterValues, ExecutorParameter.其他标签).b;
//        if (otherTag != null) select = getSelect(resultType, dbType, alias, (String[]) otherTag);
//        else select = getSelect(resultType, dbType, alias);
//        if (!StringUtils.hasText(select))
//            select = "*";
//
//        //FROM部分
//        String from = (String) CollectionsExtension.tryGet(excutorParameterValues, ExecutorParameter.自定义SQL).b;
//        if (StringUtils.hasText(from))
//            from = String.format("(%s) %s", from, StringUtils.hasText(alias) ? String.format(" AS %s", getValueWithCharacter(alias, dbType)) : "");
//        else from = getFrom(resultType, dbType, alias);
//
//        //WHERE部分
//        String where = "";
//        //自定义SQL过滤条件
//        String where_c = (String) CollectionsExtension.tryGet(excutorParameterValues, ExecutorParameter.自定义过滤SQL, "").b;
//        //动态过滤条件
//        String where_d = dynamicFilter2Sql((List<DynamicFilter>) CollectionsExtension.tryGet(excutorParameterValues, ExecutorParameter.动态过滤条件).b, dbType, alias, resultType);
//        if (StringUtils.hasText(where_c) || StringUtils.hasText(where_d)) {
//            if (StringUtils.hasText(where_c) && StringUtils.hasText(where_d))
//                where = String.format("WHERE (%s) AND (%s)", where_c, where_d);
//            else where = String.format("WHERE %s %s", where_c, where_d);
//        }
//
//        //ORDER BY部分
//        String orderby = "";
//        //自定义SQL排序条件
//        String orderby_c = (String) CollectionsExtension.tryGet(excutorParameterValues, ExecutorParameter.自定义排序SQL, "").b;
//        //动态排序条件
//        String orderby_d = dynamicOrder2Sql((DynamicOrder) CollectionsExtension.tryGet(excutorParameterValues, ExecutorParameter.动态排序条件).b, dbType, alias, resultType);
//        if (StringUtils.hasText(orderby_c) || StringUtils.hasText(orderby_d)) {
//            if (StringUtils.hasText(orderby_c) && StringUtils.hasText(orderby_d))
//                orderby = String.format("ORDER BY %s, %s", orderby_c, orderby_d);
//            else orderby = String.format("ORDER BY %s %s", orderby_c, orderby_d);
//        }
//
//        sql = String.format("SELECT %s \rFROM %s \r%s \r%s", select, from, where, orderby);
//
//        //分页
//        sql = pagination2Sql((Pagination) CollectionsExtension.tryGet(excutorParameterValues, ExecutorParameter.分页设置).b, dbType, sql, orderby, true);
//
//        return sql;
//    }
//

    /**
     * 获取动态查询语句
     *
     * @param method    方法
     * @param parameter 参数
     */
    public static String getDynamicSql(String method,
                                       Object parameter)
            throws
            Throwable {
        switch (method) {
            case DynamicMethod.BaseQueryList:
                return DynamicMethod.QueryList;
            case DynamicMethod.BaseSingle:
                return DynamicMethod.Single;
            case DynamicMethod.BaseQuerySingle:
                return DynamicMethod.QuerySingle;
            case DynamicMethod.BaseSingleByKey:
                return DynamicMethod.SingleByKey;
            case DynamicMethod.BaseInsert:
                return DynamicMethod.Insert;
            case DynamicMethod.BaseBatchInsert:
                return DynamicMethod.BatchInsert;
            case DynamicMethod.BaseQueryInsert:
                return DynamicMethod.QueryInsert;
            case DynamicMethod.BaseUpdate:
                return DynamicMethod.Update;
            case DynamicMethod.BaseBatchUpdate:
                return DynamicMethod.BatchUpdate;
            case DynamicMethod.BaseQueryUpdate:
                return DynamicMethod.QueryUpdate;
            case DynamicMethod.BaseDelete:
                return DynamicMethod.Delete;
            case DynamicMethod.BaseBatchDelete:
                return DynamicMethod.BatchDelete;
            case DynamicMethod.BaseQueryDelete:
                return DynamicMethod.QueryDelete;
            case DynamicMethod.BaseDeleteByKey:
                return DynamicMethod.DeleteByKey;
            case DynamicMethod.BaseDeleteByKeys:
                return DynamicMethod.DeleteByKeys;
            case DynamicMethod.QueryList:
//                return getDynamicQueryList(ExecutorExtension.getDynamicSetting(parameter), ExecutorExtension.getExecutorParameters(parameter).get(0));
                throw new Exception(String.format("待完善%s",
                                                  method));
            default:
                Field field = parameter.getClass()
                                       .getDeclaredField(method);
                field.setAccessible(true);
                return field.get(parameter)
                            .toString();
        }
    }
//
//    /**
//     * 获取查询多条记录的语句
//     *
//     * @param setting   设置
//     * @param parameter 参数
//     * @return
//     * @throws Throwable
//     */
//    public static String getDynamicQueryList(DynamicSqlSetting setting, Object parameter) throws Throwable {
//        return getDynamicSql(setting.getResultType(), setting.getDbType(), parameter);
//    }
//
//    /**
//     * 获取查询语句的SELECT部分
//     *
//     * @param resultType 返回数据类型
//     * @param dbType     数据库类型
//     * @param alias      数据表别名
//     * @param otherTags  其他标签
//     * @return 查询语句的SELECT部分
//     */
//    public static String getSelect(String resultType, DbType dbType, String alias, String... otherTags) throws ClassNotFoundException {
//        Class<?> classz = Class.forName(resultType);
//        return getSelect(classz, dbType, alias, otherTags);
//    }
//
//    /**
//     * 获取查询语句的SELECT部分
//     *
//     * @param resultType 返回数据类型
//     * @param dbType     数据库类型
//     * @param alias      数据表别名
//     * @param otherTags  其他标签
//     * @return 查询语句的SELECT部分
//     */
//    public static String getSelect(Class<?> resultType, DbType dbType, String alias, String... otherTags) {
//        //读取缓存
//        String cacheKey = String.format("%s:%s-%s-%s-%s", dbType, resultType.getTypeName(), alias, String.join("|", otherTags), "SELECT");
//        String sql = CacheExtension.getSql(cacheKey);
//        if (StringUtils.hasText(sql)) return sql;
//
//        List<String> columns = new ArrayList<>();
//        List<Field> fields = SchemaExtention.getFieldsWithTags(resultType, true, true, otherTags);
//        for (Field field : fields) {
//            ColumnType columnAttribute = AnnotationUtils.findAnnotation(field, ColumnType.class);
//            if (columnAttribute != null) columns.add(columnAttribute.column());
//            else columns.add(field.getName());
//        }
//
//        sql = String.join(", ", columns.stream().map(c -> getNameWithAlias(c, alias, dbType)).collect(Collectors.toList()));
//
//        //Sql语句添加至缓存
//        CacheExtension.setSql(cacheKey, sql);
//
//        return sql;
//    }
//
//    /**
//     * 获取查询语句的FROM部分
//     *
//     * @param resultType 返回数据类型
//     * @param dbType     数据库类型
//     * @param alias      数据表别名
//     * @return 查询语句的FROM部分
//     * @throws ClassNotFoundException
//     */
//    public static String getFrom(String resultType, DbType dbType, String alias) throws ClassNotFoundException {
//        Class<?> classz = Class.forName(resultType);
//        return getFrom(classz, dbType, alias);
//    }
//
//    /**
//     * 获取查询语句的FROM部分
//     *
//     * @param resultType 返回数据类型
//     * @param dbType     数据库类型
//     * @param alias      数据表别名
//     * @return 查询语句的FROM部分
//     * @throws ClassNotFoundException
//     */
//    public static String getFrom(Class<?> resultType, DbType dbType, String alias) {
//        //读取缓存
//        String cacheKey = String.format("%s:%s-%s-%s", dbType, resultType.getTypeName(), alias, "FROM");
//        String sql = CacheExtension.getSql(cacheKey);
//        if (StringUtils.hasText(sql)) return sql;
//
//        String schema = "", tableName;
//
//        Class<?> superClassz = SchemaExtention.getSuperModelType(resultType);
//
//        Table tableAttribute = AnnotationUtils.findAnnotation(superClassz, Table.class);
//        if (tableAttribute != null) {
//            schema = tableAttribute.schema();
//            tableName = tableAttribute.name();
//        } else tableName = superClassz.getSimpleName();
//
//        sql = String.format(" %s%s%s ",
//                //`dbo`.
//                StringUtils.hasText(schema) ? String.format("%s.", getValueWithCharacter(schema, dbType)) : "",
//                //`table1`
//                getValueWithCharacter(tableName, dbType),
//                // as `a`
//                StringUtils.hasText(alias) ? String.format(" AS %s", getValueWithCharacter(alias, dbType)) : "");
//
//        //Sql语句添加至缓存
//        CacheExtension.setSql(cacheKey, sql);
//
//        return sql;
//    }
//
//    /**
//     * 分页设置转为SQL语句
//     *
//     * @param pagination  分页设置
//     * @param dbType      数据库类型
//     * @param originalSql 原始sql
//     * @param orderBySql  排序语句
//     * @param getCount    获取总数
//     * @return SQL语句
//     */
//    public static String pagination2Sql(Pagination pagination, DbType dbType, String originalSql, String orderBySql, boolean getCount) throws Throwable {
//        if (pagination == null || pagination.getNope() || pagination.getUserPageHelper()) return originalSql;
//
//        int pageNum = pagination.getPageNum();
//
//        if (pageNum == -1) return originalSql;
//
//        if (getCount && !pagination.getUserPageHelper()) {
//            //不使用分页插件时在这里获取数据总数
//            String selectCountSql = String.format("SELECT COUNT(1) \nFROM (%s) AS %s", originalSql, getValueWithCharacter(String.format("t_%s", new Date().getTime()), dbType));
//            pagination.setRecordCount(SqlSessionExtension.count(selectCountSql));
//        }
//
//        int pageSize = pagination.getPageSize();
//
//        int offset = (pageNum - 1) * pageSize;
//
//        switch (dbType) {
//            case OdbcSqlServer:
//                String alias1 = getValueWithCharacter(String.format("t1_%s", new Date().getTime()), dbType);
//                String alias2 = getValueWithCharacter(String.format("t2_%s", new Date().getTime()), dbType);
//                return String.format("SELECT %s.* \nFROM (\n\tSELECT %s.*, \n\tROW_NUMBER() OVER(%s) AS ROWID \n\tFROM (%s) AS %s) AS %s \nWHERE ROWID BETWEEN %s AND %s", alias1, alias2, orderBySql, originalSql, alias2, alias1, offset, offset + pageSize);
//            case OdbcSqlServer_2012_plus:
//                return String.format("%s \rOFFSET %s ROWS FETCH NEXT %s ROWS ONLY", pageNum, pageSize);
//            case OdbcPostgreSQL:
//                return String.format("%s \nLIMIT %s OFFSET %s ", originalSql, pageSize, offset);
//            case OdbcOracle:
//                String alias3 = getValueWithCharacter(String.format("t1_%s", new Date().getTime()), dbType);
//                return String.format("SELECT %s.* \nFROM (%s) AS %s \nWHERE ROWNUM BETWEEN %s AND %s", alias3, originalSql, alias3, offset, offset + pageSize);
//            case OdbcDameng:
//                return String.format("%s \nLIMIT %s,%s", originalSql, offset, offset + pageSize);
//            default:
//            case OdbcMySql:
//                return String.format("%s \nLIMIT %s,%s ", originalSql, offset, pageSize);
//        }
//    }
//
//    /**
//     * 动态过滤条件转为SQL语句
//     *
//     * @param filters    动态过滤条件
//     * @param dbType     数据库类型
//     * @param alias      数据表别名
//     * @param resultType 返回数据类型
//     * @return SQL语句
//     * @throws ParseException 数据转换失败
//     */
//    public static String dynamicFilter2Sql(List<DynamicFilter> filters, DbType dbType, String alias, Class<?> resultType) throws ParseException {
//        if (filters == null || filters.size() == 0) return "";
//
//        String sql = "";
//
////        if (filters.size() > 1) sql += "(";
//
//        for (int i = 0; i < filters.size(); i++) {
//            DynamicFilter filter = filters.get(i);
//            if (filter == null) continue;
//
//            //数据表别名
//            String alias_filter = filter.getAlias();
//            if (!StringUtils.hasText(alias_filter)) alias_filter = alias;
//
//            //数据表别名+列名
//            String column = getNameWithAlias(getColumnByFieldName(filter.getField(), resultType), alias_filter, dbType);
//
//            boolean skip = false;
//
//            List<DynamicFilter> subFilters = filter.getDynamicFilter();
//            if (subFilters != null && subFilters.size() > 0)
//                sql += String.format(subFilters.size() > 1 ? "(%s) " : "%s ", dynamicFilter2Sql(subFilters, dbType, alias, resultType));
//            else {
//                Object value = filter.getValue();
//
//                String value2Field = "";
//                if (filter.getValueIsField()) {
//                    value2Field = value == null ? null : String.valueOf(value);
//                    if (StringUtils.hasText(value2Field))
//                        value2Field = getNameWithAlias(getColumnByFieldName(value2Field, resultType), alias_filter, dbType);
//                    else skip = true;
//                }
//
//                if (!skip) switch (filter.getCompare()) {
//                    default:
//                    case Eq:
//                        if (value == null) sql += String.format("%s IS NULL ", column);
//                        else {
//                            if (filter.getValueIsField()) sql += String.format("%s = %s ", column, value2Field);
//                            else if (!StringUtils.hasText(String.valueOf(value)))
//                                continue;
//                            else sql += String.format("'%s' = '%s' ", column, value);
//                        }
//                        break;
//                    case NotEq:
//                        if (value == null) sql += String.format("%s IS NOT NULL ", column);
//                        else {
//                            if (filter.getValueIsField()) sql += String.format("%s != %s ", column, value2Field);
//                            else if (!StringUtils.hasText(String.valueOf(value)))
//                                continue;
//                            else sql += String.format("'%s' != '%s' ", column, value);
//                        }
//                        break;
//                    case Le:
//                        if (filter.getValueIsField()) sql += String.format("%s <= %s ", column, value2Field);
//                        else if (!StringUtils.hasText(String.valueOf(value)))
//                            continue;
//                        else sql += String.format("'%s' <= '%s' ", column, value);
//                        break;
//                    case Lt:
//                        if (filter.getValueIsField()) sql += String.format("%s < %s ", column, value2Field);
//                        else if (!StringUtils.hasText(String.valueOf(value)))
//                            continue;
//                        else sql += String.format("'%s' < '%s' ", column, value);
//                        break;
//                    case Ge:
//                        if (filter.getValueIsField()) sql += String.format("%s >= %s ", column, value2Field);
//                        else if (!StringUtils.hasText(String.valueOf(value)))
//                            continue;
//                        else sql += String.format("'%s' >= '%s' ", column, value);
//                        break;
//                    case Gt:
//                        if (filter.getValueIsField()) sql += String.format("%s > %s ", column, value2Field);
//                        else if (!StringUtils.hasText(String.valueOf(value)))
//                            continue;
//                        else sql += String.format("'%s' > '%s' ", column, value);
//                        break;
//                    case In:
//                        if (!StringUtils.hasText(String.valueOf(value))) {
//                            skip = true;
//                            break;
//                        }
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s LIKE CONCAT('%', %s, '%') ", column, value2Field);
//                        else sql += String.format("%s LIKE '%%s%' ", column, value);
//                        break;
//                    case NotIn:
//                        if (!StringUtils.hasText(String.valueOf(value))) {
//                            skip = true;
//                            break;
//                        }
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s NOT LIKE CONCAT('%', %s, '%') ", column, value2Field);
//                        else sql += String.format("%s NOT LIKE '%%s%' ", column, value);
//                        break;
//                    case InStart:
//                        if (!StringUtils.hasText(String.valueOf(value))) {
//                            skip = true;
//                            break;
//                        }
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s LIKE CONCAT(%s, '%') ", column, value2Field);
//                        else sql += String.format("%s LIKE '%s%' ", column, value);
//                        break;
//                    case NotInStart:
//                        if (!StringUtils.hasText(String.valueOf(value))) {
//                            skip = true;
//                            break;
//                        }
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s NOT LIKE CONCAT(%s, '%') ", column, value2Field);
//                        else sql += String.format("%s NOT LIKE '%s%' ", column, value);
//                        break;
//                    case InEnd:
//                        if (!StringUtils.hasText(String.valueOf(value))) {
//                            skip = true;
//                            break;
//                        }
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s LIKE CONCAT('%', %s) ", column, value2Field);
//                        else sql += String.format("%s LIKE '%%s' ", column, value);
//                        break;
//                    case NotInEnd:
//                        if (!StringUtils.hasText(String.valueOf(value))) {
//                            skip = true;
//                            break;
//                        }
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s NOT LIKE CONCAT('%', %s) ", column, value2Field);
//                        else sql += String.format("%s NOT LIKE '%%s' ", column, value);
//                        break;
//                    case IncludedIn:
//                        if (!StringUtils.hasText(String.valueOf(value))) {
//                            skip = true;
//                            break;
//                        }
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s LIKE CONCAT('%', %s, '%') ", value2Field, column);
//                        else sql += String.format("'%s' LIKE CONCAT('%', %s, '%') ", value, column);
//                        break;
//                    case NotIncludedIn:
//                        if (!StringUtils.hasText(String.valueOf(value))) {
//                            skip = true;
//                            break;
//                        }
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s NOT LIKE CONCAT('%', %s, '%') ", value2Field, column);
//                        else sql += String.format("'%s' NOT LIKE CONCAT('%', %s, '%') ", value, column);
//                        break;
//                    case InSet:
//                        if (value == null || !value.getClass().equals(ArrayList.class) || ((ArrayList) value).size() == 0) {
//                            skip = true;
//                            break;
//                        }
//
//                        ArrayList values_InSet = (ArrayList) value;
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s IN (%s) ", column, String.join(", ", (List<String>) values_InSet.stream().map(o -> String.format("", o)).collect(Collectors.toList())));
//                        else
//                            sql += String.format("%s IN ('%s') ", column, String.join("', '", (List<String>) values_InSet.stream().map(o -> o.toString()).collect(Collectors.toList())));
//                        break;
//                    case NotInSet:
//                        if (value == null || !value.getClass().equals(ArrayList.class) || ((ArrayList) value).size() == 0) {
//                            skip = true;
//                            break;
//                        }
//
//                        ArrayList values_NotInSet = (ArrayList) value;
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s NOT IN (%s) ", column, String.join(", ", (List<String>) values_NotInSet.stream().map(o -> String.format("", o)).collect(Collectors.toList())));
//                        else
//                            sql += String.format("%s NOT IN ('%s') ", column, String.join("', '", (List<String>) values_NotInSet.stream().map(o -> o.toString()).collect(Collectors.toList())));
//                        break;
//                    case Range:
//                        if (value == null || !value.getClass().equals(ArrayList.class) || ((ArrayList) value).size() == 0) {
//                            skip = true;
//                            break;
//                        }
//
//                        ArrayList values_Range = (ArrayList) value;
//
//                        if (filter.getValueIsField())
//                            sql += String.format("%s >= %s AND %s < %s ", column, values_Range.get(0), column, values_Range.get(1));
//                        else
//                            sql += String.format("%s >= '%s' AND %s < '%s' ", column, values_Range.get(0), column, values_Range.get(1));
//                        break;
//                    case DateRange:
//                        if (value == null || !value.getClass().equals(ArrayList.class) || ((ArrayList) value).size() == 0) {
//                            skip = true;
//                            break;
//                        }
//
//                        List<String> values_DateRange = (List<String>) ((ArrayList) value).stream().map(o -> o.toString()).collect(Collectors.toList());
//
//                        if (filter.getValueIsField()) {
//                            sql += String.format("%s >= %s AND %s < %s ", column, values_DateRange.get(0), column, values_DateRange.get(1));
//                        } else {
//                            Calendar calendar_1 = Calendar.getInstance();
//                            SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d?$", values_DateRange.get(1))) {
//                                calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(values_DateRange.get(1)));
//                                calendar_1.add(Calendar.DAY_OF_MONTH, 1);
//                            } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?$", values_DateRange.get(1))) {
//                                calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(values_DateRange.get(1) + "-01"));
//                                calendar_1.add(Calendar.MONTH, 1);
//                            } else if (Pattern.matches("^\\d\\d\\d\\d$", values_DateRange.get(1))) {
//                                calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(values_DateRange.get(1) + "-01-01"));
//                                calendar_1.add(Calendar.YEAR, 1);
//                            } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?$", values_DateRange.get(1))) {
//                                calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(values_DateRange.get(1) + ":00:00"));
//                                calendar_1.add(Calendar.HOUR, 1);
//                            } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?:\\d\\d?$", values_DateRange.get(1))) {
//                                calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(values_DateRange.get(1) + ":00"));
//                                calendar_1.add(Calendar.MINUTE, 1);
//                            } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?:\\d\\d?:\\d\\d?$", values_DateRange.get(1))) {
//                                calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(values_DateRange.get(1)));
//                                calendar_1.add(Calendar.SECOND, 1);
//                            } else
//                                throw new ParseException("DateRange 要求 Value[1] 格式必须为：yyyy、yyyy-MM、yyyy-MM-dd、yyyy-MM-dd HH、yyyy、yyyy-MM-dd HH:mm、yyyy-MM-dd HH:mm:ss其中一种", 0);
//
//                            Calendar calendar_0 = Calendar.getInstance();
//                            if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d?$", values_DateRange.get(0))) {
//                                calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(values_DateRange.get(0)));
//                            } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?$", values_DateRange.get(0))) {
//                                calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(values_DateRange.get(0) + "-01"));
//                            } else if (Pattern.matches("^\\d\\d\\d\\d$", values_DateRange.get(0))) {
//                                calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(values_DateRange.get(0) + "-01-01"));
//                            } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?$", values_DateRange.get(0))) {
//                                calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(values_DateRange.get(0) + ":00:00"));
//                            } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?:\\d\\d?$", values_DateRange.get(0))) {
//                                calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(values_DateRange.get(0) + ":00"));
//                            } else
//                                calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(values_DateRange.get(0)));
//
//                            values_DateRange.clear();
//                            values_DateRange.add(defaultDateFormat.format(calendar_0.getTime()));
//                            values_DateRange.add(defaultDateFormat.format(calendar_1.getTime()));
//                            sql += String.format("%s >= '%s' AND %s < '%s' ", column, values_DateRange.get(0), column, values_DateRange.get(1));
//                        }
//                        break;
//                }
//            }
//
//            if (!skip && i != filters.size() - 1) sql += filter.getRelation().toString();
//        }
//
////        if (filters.size() > 1) sql += ")";
//
//        return sql;
//    }
//
//    /**
//     * 动态排序条件转为SQL语句
//     *
//     * @param order      动态排序条件
//     * @param dbType     数据库类型
//     * @param alias      数据表别名
//     * @param resultType 返回数据类型
//     * @return SQL语句
//     */
//    public static String dynamicOrder2Sql(DynamicOrder order, DbType dbType, String alias, Class<?> resultType) {
//        if (order == null) return "";
//
//        //数据表别名
//        String alias_filter = order.getAlias();
//        if (!StringUtils.hasText(alias_filter)) alias_filter = alias;
//
//        //数据表别名+列名
//        String column = getNameWithAlias(getColumnByFieldName(order.getField(), resultType), alias_filter, dbType);
//
//        OrderMethod method = order.getMethod();
//        if (method == null) method = OrderMethod.DESC;
//
//        String sql = String.format("%s %s", column, method);
//
//        String advancedOrder = advancedOrder2Sql(order.getAdvancedOrder(), dbType, alias, resultType);
//        sql += String.format("%s%s", StringUtils.hasText(advancedOrder) ? "," : "", advancedOrder);
//
//        if (StringUtils.hasText(sql)) return String.format(" %s", sql);
//
//        return sql;
//    }
//
//    /**
//     * 高级排序条件转为SQL语句
//     *
//     * @param orders     高级排序条件
//     * @param dbType     数据库类型
//     * @param alias      数据表别名
//     * @param resultType 返回数据类型
//     * @return SQL语句
//     */
//    public static String advancedOrder2Sql(List<AdvancedOrder> orders, DbType dbType, String alias, Class<?> resultType) {
//        if (orders == null || orders.size() == 0) return "";
//
//        String sql = "";
//
//        for (int i = 0; i < orders.size(); i++) {
//            AdvancedOrder order = orders.get(i);
//            if (order == null) continue;
//
//            //数据表别名
//            String alias_order = order.getAlias();
//            if (!StringUtils.hasText(alias_order)) alias_order = alias;
//
//            //数据表别名+列名
//            String column = getNameWithAlias(getColumnByFieldName(order.getField(), resultType), alias_order, dbType);
//
//            OrderMethod method = order.getMethod();
//
//            sql += String.format("%s %s", column, method);
//
//            if (i != orders.size() - 1) sql += ",";
//        }
//
//        return sql;
//    }
}
