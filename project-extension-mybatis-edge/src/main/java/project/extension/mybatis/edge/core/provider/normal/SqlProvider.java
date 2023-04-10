package project.extension.mybatis.edge.core.provider.normal;

import org.apache.ibatis.type.JdbcType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.annotations.ColumnSetting;
import project.extension.mybatis.edge.annotations.EntityMapping;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.mapper.EntityTypeHandler;
import project.extension.mybatis.edge.extention.CacheExtension;
import project.extension.mybatis.edge.extention.SqlExtension;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.*;
import project.extension.mybatis.edge.model.OrderMethod;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;
import project.extension.tuple.Tuple3;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SQL语句构造器
 *
 * @author LCTR
 * @date 2022-03-31
 */
public abstract class SqlProvider {
    public SqlProvider(DataSourceConfig config,
                       char[] character) {
        this.config = config;
        this.character = character;
    }

    /**
     * 配置
     */
    private final DataSourceConfig config;

    /**
     * 标识符
     */
    private final char[] character;

    /**
     * 获取数据库标识符
     *
     * @return 标识符[头，尾]
     */
    public char[] getCharacter() {
        return character;
    }

    /**
     * 获取使用标识符包裹的名称
     * <p>返回值示例：`id`</p>
     *
     * @param value 值
     * @return 名称
     */
    public String getValueWithCharacter(String value) {
        return String.format("%s%s%s",
                             character[0],
                             value,
                             character[1]);
    }

    /**
     * 获取使用标识符包裹的别名和名称
     * <p>返回值示例：`a`.`id`</p>
     *
     * @param name  名称
     * @param alias 别名
     * @return 别名+名称
     */
    public String getNameWithAlias(String name,
                                   String alias) {
        if (StringUtils.hasText(alias)) return String.format("%s.%s",
                                                             getValueWithCharacter(alias),
                                                             getValueWithCharacter(name));
        else return getValueWithCharacter(name);
    }

    /**
     * 检查参数名是否冲突，如果冲突则生成新的参数名
     *
     * @param parameterName 参数名
     * @param allParameter  所有的参数
     * @return 不会冲突的参数名
     */
    public String checkParameterName(String parameterName,
                                     Map<String, Object> allParameter) {
        if (allParameter.containsKey(parameterName)) {
            int index = parameterName.lastIndexOf("___");
            int number = index < 0
                         ? -1
                         : Integer.parseInt(parameterName.substring(index + 3));
            return checkParameterName(String.format("%s___%s",
                                                    index < 0
                                                    ? parameterName
                                                    : parameterName.substring(0,
                                                                              index),
                                                    ++number),
                                      allParameter);
        } else return parameterName;
    }

    /**
     * 替换sql语句中的参数
     *
     * @param sql             sql语句
     * @param allParameter    全部参数
     * @param customParameter 自定义参数集合
     * @param noParameter     无参数语句
     * @return 处理后的sql语句
     */
    public String replaceParameter(String sql,
                                   Map<String, Object> allParameter,
                                   Map<String, Object> customParameter,
                                   boolean noParameter)
            throws
            ModuleException {
        for (String key : customParameter.keySet()) {
            Pattern pattern = Pattern.compile(String.format("@%s",
                                                            key),
                                              Pattern.CASE_INSENSITIVE);

            Matcher matcher = pattern.matcher(sql);

            if (!matcher.find()) continue;

            Object value = customParameter.get(key);

            sql = matcher.replaceAll(noParameter
                                     ? value2ValueSql(value)
                                     : value2ParameterSql(value.getClass(),
                                                          key,
                                                          value,
                                                          null,
                                                          allParameter,
                                                          false).b);
        }
        return sql;
    }

    /**
     * 列名转Sql语句
     *
     * @param columns 列名
     * @return Sql语句
     */
    public String columns2Sql(Collection<String> columns) {
        return columns2Sql(columns,
                           null);
    }

    /**
     * 列名转Sql语句
     *
     * @param columns 列名
     * @param alias   表别名
     * @return Sql语句
     */
    public String columns2Sql(Collection<String> columns,
                              String alias) {
        return columns.stream()
                      .map(c -> getNameWithAlias(c,
                                                 alias))
                      .collect(Collectors.joining(", "));
    }

    /**
     * 字段名转Sql值语句
     *
     * @param fieldName  字段名
     * @param entityType 实体类型
     * @param data       数据
     * @return Sql语句
     */
    public String fieldName2ValueSql(String fieldName,
                                     Class<?> entityType,
                                     Object data,
                                     boolean dataIsValue)
            throws
            ModuleException {
        return field2ValueSql(EntityTypeHandler.getFieldByFieldName(fieldName,
                                                                    entityType,
                                                                    null),
                              data,
                              dataIsValue);
    }

    /**
     * 字段名转Sql值语句
     *
     * @param field 字段
     * @param data  数据
     * @return Sql语句
     */
    public String field2ValueSql(Field field,
                                 Object data,
                                 boolean dataIsValue)
            throws
            ModuleException {
        try {
            field.setAccessible(true);
            return value2ValueSql(field.getType(),
                                  data == null
                                  ? null
                                  : dataIsValue
                                    ? data
                                    : field.get(data));
        } catch (IllegalAccessException ex) {
            throw new ModuleException(Strings.getGetObjectFieldValueFailed(data == null
                                                                           ? "null"
                                                                           : data.getClass()
                                                                                 .getTypeName(),
                                                                           field.getName()));
        }
    }

    /**
     * 值Sql值语句
     *
     * @param value 值
     * @return Sql语句
     */
    public String value2ValueSql(Object value) {
        return value2ValueSql(value.getClass(),
                              value);
    }

    /**
     * 字段名转Sql值语句
     *
     * @param type  类型
     * @param value 值
     * @return Sql语句
     */
    public String value2ValueSql(Class<?> type,
                                 Object value) {
        if (value == null || (type.equals(String.class) && String.valueOf(value)
                                                                 .equals(""))) return "NULL";

        //#TODO 数据类型待完善
        String value_;
        if (type.equals(Date.class)) value_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
        else value_ = value.toString();
        return String.format("'%s'",
                             value_);
    }

    /**
     * 字段名转Sql参数语句
     *
     * @param fieldNames  字段名集合
     * @param entityType  实体类型
     * @param noParameter 无参数语句
     * @param parameter   参数集合
     * @param data        数据
     * @return Sql参数语句
     */
    public String fieldName2ParameterSql(Collection<String> fieldNames,
                                         Class<?> entityType,
                                         boolean noParameter,
                                         Map<String, Object> parameter,
                                         Object data)
            throws
            ModuleException {
        List<String> valuesSqlList = new ArrayList<>();
        for (String fieldName : fieldNames) {
            if (noParameter) valuesSqlList.add(fieldName2ValueSql(fieldName,
                                                                  entityType,
                                                                  data,
                                                                  false));
            else valuesSqlList.add(fieldName2ParameterSql(fieldName,
                                                          entityType,
                                                          parameter,
                                                          data,
                                                          false,
                                                          false).b);
        }
        return String.join(", \r\n\t\t",
                           valuesSqlList);
    }

    /**
     * 字段名转Sql参数语句
     *
     * @param fieldName   字段名
     * @param entityType  实体类型
     * @param parameter   参数集合
     * @param data        数据
     * @param dataIsValue 数据就是字段的值
     * @param ignoreCase  忽略大小写
     * @return a: 参数名, b: Sql参数语句
     */
    public Tuple2<String, String> fieldName2ParameterSql(String fieldName,
                                                         Class<?> entityType,
                                                         Map<String, Object> parameter,
                                                         Object data,
                                                         boolean dataIsValue,
                                                         boolean ignoreCase)
            throws
            ModuleException {
        return field2ParameterSql(EntityTypeHandler.getFieldByFieldName(fieldName,
                                                                        entityType,
                                                                        null),
                                  parameter,
                                  data,
                                  dataIsValue,
                                  ignoreCase);
    }

    /**
     * 字段名转Sql参数语句
     *
     * @param fieldName  字段名
     * @param entityType 实体类型
     * @param parameter  参数集合
     * @return Sql参数语句
     */
    public String fieldName2ParameterSql(String fieldName,
                                         Class<?> entityType,
                                         Map<String, Object> parameter)
            throws
            ModuleException {
        return field2ParameterSql(EntityTypeHandler.getFieldByFieldName(fieldName,
                                                                        entityType,
                                                                        null),
                                  parameter);
    }

    /**
     * 字段转Sql参数语句
     *
     * @param field     字段
     * @param parameter 参数集合
     * @return Sql参数语句
     */
    public String field2ParameterSql(Field field,
                                     Map<String, Object> parameter) {
        return field2ParameterSql(field,
                                  field.getName(),
                                  parameter);
    }

    /**
     * 字段转Sql参数语句
     *
     * @param field         字段
     * @param parameterName 参数名
     * @param parameter     参数集合
     * @return Sql参数语句
     */
    public String field2ParameterSql(Field field,
                                     String parameterName,
                                     Map<String, Object> parameter) {
        return value2ParameterSql(field.getType(),
                                  parameterName,
                                  parameter);
    }

    /**
     * 字段转Sql参数语句
     *
     * @param field       字段
     * @param data        数据
     * @param parameter   参数集合
     * @param dataIsValue 参数就是值
     * @param ignoreCase  忽略大小写
     * @return a: 参数名, b: Sql参数语句
     */
    public Tuple2<String, String> field2ParameterSql(Field field,
                                                     Map<String, Object> parameter,
                                                     Object data,
                                                     boolean dataIsValue,
                                                     boolean ignoreCase)
            throws
            ModuleException {
        try {
            field.setAccessible(true);
            return value2ParameterSql(field.getType(),
                                      field.getName(),
                                      data == null
                                      ? null
                                      : dataIsValue
                                        ? data
                                        : field.get(data),
                                      EntityTypeHandler.getJdbcType(field,
                                                                    config.getDbType()),
                                      parameter,
                                      ignoreCase);
        } catch (IllegalAccessException ex) {
            throw new ModuleException(Strings.getGetObjectFieldValueFailed(data.getClass()
                                                                               .getTypeName(),
                                                                           field.getName()),
                                      ex);
        }
    }

    /**
     * 字段转Sql参数语句
     *
     * @param type          类型
     * @param parameterName 参数名
     * @param parameter     参数集合
     * @return Sql参数语句
     */
    public String value2ParameterSql(Class<?> type,
                                     String parameterName,
                                     Map<String, Object> parameter) {
        String parameterName_ = checkParameterName(parameterName,
                                                   parameter);
        return String.format("#{%s,javaType=%s,jdbcType=%s}",
                             parameterName_,
                             type.getSimpleName(),
                             EntityTypeHandler.getJdbcType(type));
    }

    /**
     * 字段转Sql参数语句
     *
     * @param type          类型
     * @param parameterName 参数名
     * @param value         值
     * @param jdbcType      JDBC类型
     * @param parameter     参数集合
     * @param ignoreCase    忽略大小写
     * @return Sql参数语句
     */
    public Tuple2<String, String> value2ParameterSql(Class<?> type,
                                                     String parameterName,
                                                     Object value,
                                                     JdbcType jdbcType,
                                                     Map<String, Object> parameter,
                                                     boolean ignoreCase)
            throws
            ModuleException {
        if (value == null) return new Tuple2<>(parameterName,
                                               "NULL");

        parameterName = checkParameterName(parameterName,
                                           parameter);
        //#TODO 数据类型的处理待完善
        if (!value.getClass()
                  .equals(type)) {
            if (type.equals(Byte.class)) value = Byte.parseByte(value.toString());
            else if (type.equals(Short.class)) value = Short.parseShort(value.toString());
            else if (type.equals(Integer.class)) value = Integer.parseInt(value.toString());
            else if (type.equals(Long.class)) value = Long.parseLong(value.toString());
            else if (type.equals(Double.class)) value = Double.parseDouble(value.toString());
            else if (type.equals(Float.class)) value = Float.parseFloat(value.toString());
            else if (type.equals(BigDecimal.class)) value = new BigDecimal(value.toString());
            else if (type.equals(Date.class)) {
                try {
                    value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.toString());
                } catch (ParseException ex) {
                    throw new ModuleException(Strings.getFormatDateFailed(value.toString(),
                                                                          "yyyy-MM-dd HH:mm:ss"));
                }
            } else if (type.equals(Time.class)) value = new Time(Long.parseLong(value.toString()));
            else if (type.equals(java.sql.Date.class)) value = new java.sql.Date(Long.parseLong(value.toString()));
            else if (type.equals(java.sql.Timestamp.class))
                value = new java.sql.Timestamp(Long.parseLong(value.toString()));
            else if (type.equals(String.class)) value = String.valueOf(value);
        }

        if (type.equals(String.class) && ignoreCase) value = String.valueOf(value)
                                                                   .toLowerCase(Locale.ROOT);

        parameter.put(parameterName,
                      value);

        StringBuilder sb = new StringBuilder("#{");
        sb.append(parameterName);

        if (jdbcType == null)
            jdbcType = EntityTypeHandler.getJdbcType(type);

        if (!jdbcType.equals(JdbcType.BLOB))
            sb.append(String.format(",javaType=%s",
                                    type.getSimpleName()));
        sb.append(String.format(",jdbcType=%s",
                                jdbcType));
        sb.append("}");

        return new Tuple2<>(parameterName,
                            sb.toString());
    }

    /**
     * 指定字段名+Sql语句转更新记录的Sql语句
     *
     * @param entityType                  实体类型
     * @param customSetByFieldNameWithSql 字段名+Sql语句
     * @param alias                       表别名
     * @return Sql语句
     */
    public String customSetByFieldNameWithSql2UpdateSql(Class<?> entityType,
                                                        Map<String, String> customSetByFieldNameWithSql,
                                                        String alias) {
        List<String> sqlList = new ArrayList<>();
        for (String fieldName : customSetByFieldNameWithSql.keySet()) {
            sqlList.add(String.format("\r\n\t\t%s = %s",
                                      getNameWithAlias(EntityTypeHandler.getColumnByFieldName(fieldName,
                                                                                              entityType,
                                                                                              config.getNameConvertType()),
                                                       alias),
                                      customSetByFieldNameWithSql.get(fieldName)));
        }
        return String.join(", ",
                           sqlList);
    }

    /**
     * 指定字段名+值转更新记录的Sql语句
     *
     * @param entityType            实体类型
     * @param customSetByFieldNames 字段名+值
     * @param alias                 表别名
     * @param noParameter           无参数语句
     * @param parameter             参数
     * @return Sql语句
     */
    public String customSetByFieldNames2UpdateSql(Class<?> entityType,
                                                  Map<String, Object> customSetByFieldNames,
                                                  String alias,
                                                  boolean noParameter,
                                                  Map<String, Object> parameter)
            throws
            ModuleException {
        List<String> sqlList = new ArrayList<>();
        for (String fieldName : customSetByFieldNames.keySet()) {
            Field field = EntityTypeHandler.getFieldByFieldName(fieldName,
                                                                entityType,
                                                                null);
            sqlList.add(String.format("\r\n\t\t%s = %s",
                                      getNameWithAlias(EntityTypeHandler.getColumn(field,
                                                                                   config.getNameConvertType()),
                                                       alias),
                                      noParameter
                                      ? field2ValueSql(field,
                                                       customSetByFieldNames.get(fieldName),
                                                       true)
                                      : field2ParameterSql(field,
                                                           parameter,
                                                           customSetByFieldNames.get(fieldName),
                                                           true,
                                                           false).b));
        }
        return String.join(", ",
                           sqlList);
    }

    /**
     * 指定字段名+动态更新转更新记录的Sql语句
     *
     * @param entityType  实体类型
     * @param setters     动态更新
     * @param alias       表别名
     * @param noParameter 无参数语句
     * @param parameter   参数
     * @return Sql语句
     */
    public String dynamicSetters2UpdateSql(Class<?> entityType,
                                           List<DynamicSetter> setters,
                                           String alias,
                                           boolean noParameter,
                                           Map<String, Object> parameter)
            throws
            ModuleException {
        List<String> sqlList = new ArrayList<>();
        for (DynamicSetter setter : setters) {
            Field field = EntityTypeHandler.getFieldByFieldName(setter.getFieldName(),
                                                                entityType,
                                                                null);
            sqlList.add(String.format("\r\n\t\t%s = %s",
                                      getNameWithAlias(EntityTypeHandler.getColumn(field,
                                                                                   config.getNameConvertType()),
                                                       alias),
                                      noParameter
                                      ? dynamicSetterExpression2ValueSql(entityType,
                                                                         alias,
                                                                         setter.getMemberType(),
                                                                         setter.getExpression())
                                      : dynamicSetterExpression2ParameterSql(entityType,
                                                                             alias,
                                                                             setter.getMemberType(),
                                                                             setter.getExpression(),
                                                                             parameter)));
        }
        return String.join(", ",
                           sqlList);
    }

    /**
     * 操作符号转Sql语句
     *
     * @param memberType      成员类型
     * @param operationSymbol 操作符
     * @return Sql语句
     */
    public String operationSymbol2Sql(Class<?> memberType,
                                      OperationSymbol operationSymbol)
            throws
            ModuleException {
        if (memberType.equals(String.class) && !operationSymbol.equals(OperationSymbol.Plus))
            throw new ModuleException(Strings.getTypeFieldOnlySupportOperation("String",
                                                                               "OperationSymbol.Plus"));

        switch (operationSymbol) {
            case Plus:
                if (memberType.equals(String.class)) return ", ";
                else return "+ ";
            case Reduce:
                return "- ";
            case Become:
                return "* ";
            case Except:
                return "/ ";
            case Remainder:
                return "% ";
            default:
                throw new ModuleException(Strings.getUnSupportOperationSymbol(operationSymbol.toString()));
        }
    }

    /**
     * 动态更新表达式目标对象转更新记录的无参Sql语句
     *
     * @param entityType 实体类型
     * @param alias      数据表别名
     * @param memberType 成员类型
     * @param target     目标对象
     * @return Sql语句
     */
    public String dynamicSetterTarget2ValueSql(Class<?> entityType,
                                               String alias,
                                               Class<?> memberType,
                                               DynamicSetterTarget target)
            throws
            ModuleException {
        switch (target.getType()) {
            case FieldName:
                return getNameWithAlias(EntityTypeHandler.getColumnByFieldName(target.getFieldName(),
                                                                               entityType,
                                                                               config.getNameConvertType()),
                                        alias);
            case Value:
                return value2ValueSql(target.getValue());
            case Expression:
                return String.format("(%s)",
                                     dynamicSetterExpression2ValueSql(entityType,
                                                                      alias,
                                                                      memberType,
                                                                      target.getExpression()));
            default:
                throw new ModuleException(Strings.getUnSupportTargetType(target.getType()
                                                                               .toString()));
        }
    }

    /**
     * 动态更新表达式目标对象转更新记录的带参数Sql语句
     *
     * @param entityType 实体类型
     * @param alias      数据表别名
     * @param memberType 成员类型
     * @param target     目标对象
     * @param parameter  参数
     * @return Sql语句
     */
    public String dynamicSetterTarget2ParameterSql(Class<?> entityType,
                                                   String alias,
                                                   Class<?> memberType,
                                                   DynamicSetterTarget target,
                                                   Map<String, Object> parameter)
            throws
            ModuleException {
        switch (target.getType()) {
            case FieldName:
                return getNameWithAlias(EntityTypeHandler.getColumnByFieldName(target.getFieldName(),
                                                                               entityType,
                                                                               config.getNameConvertType()),
                                        alias);
            case Value:
                return value2ParameterSql(memberType,
                                          "value",
                                          target.getValue(),
                                          null,
                                          parameter,
                                          false).b;
            case Expression:
                return String.format("(%s)",
                                     dynamicSetterExpression2ParameterSql(entityType,
                                                                          alias,
                                                                          memberType,
                                                                          target.getExpression(),
                                                                          parameter));
            default:
                throw new ModuleException(Strings.getUnSupportTargetType(target.getType()
                                                                               .toString()));
        }
    }

    /**
     * 动态更新表达式转更新记录的无参Sql语句
     *
     * @param entityType 实体类型
     * @param alias      数据表别名
     * @param memberType 成员类型
     * @param expression 表达式
     * @return Sql语句
     */
    public String dynamicSetterExpression2ValueSql(Class<?> entityType,
                                                   String alias,
                                                   Class<?> memberType,
                                                   DynamicSetterExpression expression)
            throws
            ModuleException {
        StringBuilder sb = new StringBuilder();

        sb.append(dynamicSetterTarget2ValueSql(entityType,
                                               alias,
                                               memberType,
                                               expression.getStart()));
        sb.append(dynamicSetterOperation2ValueSql(entityType,
                                                  alias,
                                                  memberType,
                                                  expression.getOperations()));

        if (memberType.equals(String.class)) return String.format("CONCAT(%s)",
                                                                  sb);
        else return sb.toString();
    }

    /**
     * 动态更新操作转Sql语句
     *
     * @param entityType       实体类型
     * @param alias            数据表别名
     * @param memberType       成员类型
     * @param setterOperations 操作集合
     * @return Sql语句
     */
    public String dynamicSetterOperation2ValueSql(Class<?> entityType,
                                                  String alias,
                                                  Class<?> memberType,
                                                  List<DynamicSetterOperation> setterOperations)
            throws
            ModuleException {
        StringBuilder sb = new StringBuilder();
        for (DynamicSetterOperation setterOperation : setterOperations) {
            sb.append(operationSymbol2Sql(memberType,
                                          setterOperation.getOperationSymbol()));
            sb.append(dynamicSetterTarget2ValueSql(entityType,
                                                   alias,
                                                   memberType,
                                                   setterOperation.getTarget()));
        }
        return sb.toString();
    }

    /**
     * 动态更新表达式转更新记录的Sql语句
     *
     * @param entityType 实体类型
     * @param alias      数据表别名
     * @param memberType 成员类型
     * @param expression 表达式
     * @param parameter  参数
     * @return Sql语句
     */
    public String dynamicSetterExpression2ParameterSql(Class<?> entityType,
                                                       String alias,
                                                       Class<?> memberType,
                                                       DynamicSetterExpression expression,
                                                       Map<String, Object> parameter)
            throws
            ModuleException {
        StringBuilder sb = new StringBuilder();

        sb.append(dynamicSetterTarget2ParameterSql(entityType,
                                                   alias,
                                                   memberType,
                                                   expression.getStart(),
                                                   parameter));
        sb.append(dynamicSetterOperation2ParameterSql(entityType,
                                                      alias,
                                                      memberType,
                                                      expression.getOperations(),
                                                      parameter));

        if (memberType.equals(String.class)) return String.format("CONCAT(%s)",
                                                                  sb);
        else return sb.toString();
    }

    /**
     * 动态更新操作转更新记录的Sql语句
     *
     * @param entityType       实体类型
     * @param alias            数据表别名
     * @param memberType       成员类型
     * @param setterOperations 操作集合
     * @param parameter        参数
     * @return Sql语句
     */
    public String dynamicSetterOperation2ParameterSql(Class<?> entityType,
                                                      String alias,
                                                      Class<?> memberType,
                                                      List<DynamicSetterOperation> setterOperations,
                                                      Map<String, Object> parameter)
            throws
            ModuleException {
        StringBuilder sb = new StringBuilder();
        for (DynamicSetterOperation setterOperation : setterOperations) {
            sb.append(operationSymbol2Sql(memberType,
                                          setterOperation.getOperationSymbol()));
            sb.append(dynamicSetterTarget2ParameterSql(entityType,
                                                       alias,
                                                       memberType,
                                                       setterOperation.getTarget(),
                                                       parameter));
        }
        return sb.toString();
    }

    /**
     * 字段名+列名转更新记录的Sql语句
     *
     * @param entityType       实体类型
     * @param fieldWithColumns 字段名+列名
     * @param alias            表别名
     * @param noParameter      无参数语句
     * @param parameter        参数集合
     * @param data             数据
     * @return Sql语句
     */
    public String fieldWithColumns2UpdateSql(Class<?> entityType,
                                             Map<String, String> fieldWithColumns,
                                             String alias,
                                             boolean noParameter,
                                             Map<String, Object> parameter,
                                             Object data)
            throws
            ModuleException {
        List<String> sqlList = new ArrayList<>();
        for (String fieldName : fieldWithColumns.keySet()) {
            Field field = EntityTypeHandler.getFieldByFieldName(fieldName,
                                                                entityType,
                                                                null);
            sqlList.add(String.format("\r\n\t\t%s = %s",
                                      getNameWithAlias(fieldWithColumns.get(fieldName),
                                                       alias),
                                      noParameter
                                      ? field2ValueSql(field,
                                                       data,
                                                       false)
                                      : field2ParameterSql(field,
                                                           parameter,
                                                           data,
                                                           false,
                                                           false).b));
        }
        return String.join(", ",
                           sqlList);
    }

    /**
     * 主键字段名+列名转更新记录的条件Sql语句
     *
     * @param entityType                 实体类型
     * @param primaryKeyFieldWithColumns 字段名+列名
     * @param alias                      表别名
     * @param noParameter                无参数语句
     * @param parameter                  参数集合
     * @param data                       数据
     * @return Sql语句
     */
    public String primaryKeyFieldWithColumns2WhereSql(Class<?> entityType,
                                                      Map<String, String> primaryKeyFieldWithColumns,
                                                      String alias,
                                                      boolean noParameter,
                                                      Map<String, Object> parameter,
                                                      Object data)
            throws
            ModuleException {
        List<String> where = new ArrayList<>();
        for (String fieldName : primaryKeyFieldWithColumns.keySet()) {
            Field field = EntityTypeHandler.getFieldByFieldName(fieldName,
                                                                entityType,
                                                                null);
            where.add(String.format("%s = %s",
                                    getNameWithAlias(primaryKeyFieldWithColumns.get(fieldName),
                                                     alias),
                                    noParameter
                                    ? field2ValueSql(field,
                                                     data,
                                                     false)
                                    : field2ParameterSql(field,
                                                         parameter,
                                                         data,
                                                         false,
                                                         false).b));
        }
        return String.join(" AND ",
                           where);
    }

    /**
     * 表名转Sql语句
     *
     * @param name 表名
     * @return Sql语句
     */
    public String getName2Sql(String name) {
        return getName2Sql(null,
                           name,
                           null);
    }

    /**
     * 对象名称转Sql语句
     *
     * @param schema 模式名
     * @param name   表名
     * @return Sql语句
     */
    public String getName2Sql(String schema,
                              String name) {
        return getName2Sql(schema,
                           name,
                           null);
    }

    /**
     * 获取业务模型对应的列名
     *
     * @param entityType         实体类型
     * @param dtoType            业务模型类型
     * @param mainTagLevel       主标签等级
     * @param customTags         自定义标签
     * @param withOutPrimaryKey  排除主键
     * @param withOutIdentityKey 排除自增列
     * @param inherit            仅继承成员
     * @return 列名集合
     */
    public Collection<String> getColumns(Class<?> entityType,
                                         Class<?> dtoType,
                                         int mainTagLevel,
                                         Collection<String> customTags,
                                         boolean withOutPrimaryKey,
                                         boolean withOutIdentityKey,
                                         boolean inherit)
            throws
            ModuleException {
        //读取缓存
        String cacheKey = String.format("%s:%s:%s",
                                        this.config.getName(),
                                        entityType.getTypeName(),
                                        dtoType == null
                                        ? ""
                                        : dtoType.getTypeName());
        Collection<String> columns = CacheExtension.getDtoTypeColumns(cacheKey);
        if (CollectionsExtension.anyPlus(columns)) return columns;

        if (dtoType == null) columns = EntityTypeHandler.getColumnsByEntityType(entityType,
                                                                                withOutPrimaryKey,
                                                                                withOutIdentityKey,
                                                                                config.getNameConvertType());
        else columns = EntityTypeHandler.getColumnFieldsByDtoType(dtoType,
                                                                  mainTagLevel,
                                                                  customTags,
                                                                  withOutPrimaryKey,
                                                                  withOutIdentityKey,
                                                                  inherit)
                                        .stream()
                                        .map(x -> EntityTypeHandler.getColumn(x,
                                                                              config.getNameConvertType()))
                                        .collect(Collectors.toList());

        //添加至缓存
        CacheExtension.setDtoTypeColumns(cacheKey,
                                         columns);

        return columns;
    }

    /**
     * 获取业务模型对应的字段+列名
     *
     * @param entityType         实体类型
     * @param mainTagLevel       主标签等级
     * @param customTags         自定义标签
     * @param withOutPrimaryKey  排除主键
     * @param withOutIdentityKey 排除自增列
     * @param inherit            仅继承成员
     * @return 字段+列名集合
     */
    public Map<String, String> getFieldNameWithColumns(Class<?> entityType,
                                                       int mainTagLevel,
                                                       Collection<String> customTags,
                                                       boolean withOutPrimaryKey,
                                                       boolean withOutIdentityKey,
                                                       boolean inherit) {
        return getFieldNameWithColumns(entityType,
                                       null,
                                       mainTagLevel,
                                       customTags,
                                       null,
                                       null,
                                       null,
                                       null,
                                       withOutPrimaryKey,
                                       withOutIdentityKey,
                                       inherit);
    }

    /**
     * 获取业务模型对应的字段+列名
     *
     * @param entityType         实体类型
     * @param dtoType            业务模型类型
     * @param mainTagLevel       主标签等级
     * @param customTags         自定义标签
     * @param withOutPrimaryKey  排除主键
     * @param withOutIdentityKey 排除自增列
     * @param inherit            仅继承成员
     * @return 字段+列名集合
     */
    public Map<String, String> getFieldNameWithColumns(Class<?> entityType,
                                                       Class<?> dtoType,
                                                       int mainTagLevel,
                                                       Collection<String> customTags,
                                                       boolean withOutPrimaryKey,
                                                       boolean withOutIdentityKey,
                                                       boolean inherit) {
        return getFieldNameWithColumns(entityType,
                                       dtoType,
                                       mainTagLevel,
                                       customTags,
                                       null,
                                       null,
                                       null,
                                       null,
                                       withOutPrimaryKey,
                                       withOutIdentityKey,
                                       inherit);
    }

    /**
     * 获取业务模型对应的字段+列名
     *
     * @param entityType          实体类型
     * @param dtoType             业务模型类型
     * @param customTags          自定义标签
     * @param exceptionFieldNames 包括的字段
     * @param exceptionColumns    包括的列
     * @param ignoreFieldNames    忽略的字段
     * @param ignoreColumns       忽略的列
     * @param withOutPrimaryKey   排除主键
     * @param withOutIdentityKey  排除自增列
     * @param inherit             仅继承成员
     * @return 字段+列名集合
     */
    public Map<String, String> getFieldNameWithColumns(Class<?> entityType,
                                                       Class<?> dtoType,
                                                       int mainTagLevel,
                                                       Collection<String> customTags,
                                                       Collection<String> exceptionFieldNames,
                                                       Collection<String> exceptionColumns,
                                                       Collection<String> ignoreFieldNames,
                                                       Collection<String> ignoreColumns,
                                                       boolean withOutPrimaryKey,
                                                       boolean withOutIdentityKey,
                                                       boolean inherit)
            throws
            ModuleException {
        //读取缓存
        String cacheKey = String.format("DS-%s:ET-%s:DT-%s:MTL-%s:CT-%s:WOPK-%s:WOIK:%s",
                                        this.config.getName(),
                                        entityType.getTypeName(),
                                        dtoType == null
                                        ? ""
                                        : dtoType.getTypeName(),
                                        mainTagLevel,
                                        customTags == null
                                        ? ""
                                        : String.join(",",
                                                      customTags),
                                        withOutPrimaryKey,
                                        withOutIdentityKey);
        Map<String, String> fieldWithColumns = CacheExtension.getFieldWithColumns(cacheKey);
        if (!CollectionsExtension.anyPlus(fieldWithColumns)) {
            fieldWithColumns = new HashMap<>();

            for (Field field : EntityTypeHandler.getColumnFieldsByDtoType(dtoType,
                                                                          mainTagLevel,
                                                                          customTags,
                                                                          withOutPrimaryKey,
                                                                          withOutIdentityKey,
                                                                          inherit)) {
                fieldWithColumns.put(field.getName(),
                                     EntityTypeHandler.getColumn(field,
                                                                 config.getNameConvertType()));
            }

            //添加至缓存
            CacheExtension.setFieldWithColumns(cacheKey,
                                               fieldWithColumns);
        }

        if (CollectionsExtension.anyPlus(exceptionFieldNames))
            CollectionsExtension.tryRemoveIfKeyNotIn(fieldWithColumns,
                                                     exceptionFieldNames);

        if (CollectionsExtension.anyPlus(exceptionColumns)) CollectionsExtension.tryRemoveIfValueNotIn(fieldWithColumns,
                                                                                                       exceptionColumns);

        if (CollectionsExtension.anyPlus(ignoreFieldNames)) CollectionsExtension.tryRemove(fieldWithColumns,
                                                                                           ignoreFieldNames);

        if (CollectionsExtension.anyPlus(ignoreColumns)) CollectionsExtension.tryRemoveIfValueIn(fieldWithColumns,
                                                                                                 ignoreColumns);

        return fieldWithColumns;
    }

    /**
     * 获取主键字段+列集合
     *
     * @param entityType        实体类型
     * @param tempKeyFieldNames 用作临时主键的字段
     * @param tempKeyColumns    用作临时主键的列
     * @param check             执行检查，如果没有数据则抛出异常
     * @return 字段+列名集合
     */
    public Map<String, String> getPrimaryKeyFieldWithColumns(Class<?> entityType,
                                                             Collection<String> tempKeyFieldNames,
                                                             Collection<String> tempKeyColumns,
                                                             boolean check)
            throws
            ModuleException {
        Map<String, String> primaryKeyFieldWithColumns = new HashMap<>();

        if (CollectionsExtension.anyPlus(tempKeyFieldNames)) {
            for (String fieldName : tempKeyFieldNames) {
                primaryKeyFieldWithColumns.put(fieldName,
                                               EntityTypeHandler.getColumnByFieldName(fieldName,
                                                                                      entityType,
                                                                                      config.getNameConvertType()));
            }
        }

        if (CollectionsExtension.anyPlus(tempKeyColumns)) {
            for (String column : tempKeyColumns) {
                primaryKeyFieldWithColumns.put(EntityTypeHandler.getFieldName(column,
                                                                              entityType,
                                                                              config.getNameConvertType()),
                                               column);
            }
        }

        //默认主键
        if (primaryKeyFieldWithColumns.size() == 0)
            primaryKeyFieldWithColumns.putAll(EntityTypeHandler.getPrimaryKeyFieldNameWithColumns(entityType,
                                                                                                  config.getNameConvertType()));

        if (check && primaryKeyFieldWithColumns.size() == 0)
            throw new ModuleException(Strings.getEntityPrimaryKeyUndefined());

        return primaryKeyFieldWithColumns;
    }

    /**
     * 表名转Sql语句
     *
     * @param schema    模式名
     * @param tableName 表名
     * @param alias     别名
     * @return Sql语句
     */
    public String getName2Sql(String schema,
                              String tableName,
                              String alias) {
        return String.format(" %s%s%s ",
                             //`dbo`.
                             StringUtils.hasText(schema)
                             ? String.format("%s.",
                                             getValueWithCharacter(schema))
                             : "",
                             //`table1`
                             getValueWithCharacter(tableName),
                             // as `a`
                             StringUtils.hasText(alias)
                             ? String.format(" AS %s",
                                             getValueWithCharacter(alias))
                             : "");
    }

    /**
     * 自定义查询语句转Sql语句
     *
     * @param sql   Sql语句
     * @param alias 别名
     * @return Sql语句
     */
    public String withSql2Sql(String sql,
                              String alias) {
        return String.format(" (%s)%s ",
                             sql,
                             // as `a`
                             StringUtils.hasText(alias)
                             ? String.format(" AS %s",
                                             getValueWithCharacter(alias))
                             : "");
    }

    /**
     * 动态过滤条件转为SQL语句
     *
     * @param filters     动态过滤条件
     * @param alias       数据表别名
     * @param noParameter 无参数语句
     * @param parameter   参数键值对
     * @param entityType  实体类型
     * @param dtoType     业务模型类型
     * @return SQL语句
     */
    public String dynamicFilter2Sql(List<DynamicFilter> filters,
                                    String alias,
                                    boolean noParameter,
                                    Map<String, Object> parameter,
                                    Class<?> entityType,
                                    Class<?> dtoType)
            throws
            ModuleException {
        if (filters == null || filters.size() == 0) return "";

        //sql语句
        StringBuilder sql = new StringBuilder();

        for (int i = 0; i < filters.size(); i++) {
            StringBuilder _sql = new StringBuilder();

            DynamicFilter filter = filters.get(i);
            if (filter == null) continue;

            boolean skip = false;

            List<DynamicFilter> subFilters = filter.getDynamicFilter();
            if (subFilters != null && subFilters.size() > 0) _sql.append(String.format(subFilters.size() > 1
                                                                                       ? "(%s) "
                                                                                       : "%s ",
                                                                                       dynamicFilter2Sql(subFilters,
                                                                                                         alias,
                                                                                                         noParameter,
                                                                                                         parameter,
                                                                                                         entityType,
                                                                                                         dtoType)));
            else {
                //筛选条件为自定义where条件
                if (StringUtils.hasText(filter.getCustomSql())) _sql.append(String.format("(%s)",
                                                                                          filter.getCustomSql()));
                else {
                    //未指定字段名
                    if (!StringUtils.hasText(filter.getFieldName())) continue;

                    //数据表别名
                    String alias_filter = filter.getAlias();
                    if (!StringUtils.hasText(alias_filter)) alias_filter = alias;

                    //字段
                    Field field = EntityTypeHandler.getFieldByFieldName(filter.getFieldName(),
                                                                        entityType,
                                                                        dtoType.isAnnotationPresent(EntityMapping.class)
                                                                        ? dtoType
                                                                        : null);

                    //数据表别名+列名
                    String column = EntityTypeHandler.getColumn(field,
                                                                config.getNameConvertType());
                    if (!StringUtils.hasText(column)) skip = true;
                    else column = getNameWithAlias(column,
                                                   alias_filter);

                    boolean ignoreCase = filter.getCompare()
                                               .name()
                                               .indexOf("IgnoreCase") > 0;

                    //忽略大小写的操作
                    if (!skip && ignoreCase) {
                        column = String.format("lower(%s)",
                                               column);
                    }

                    Object value = filter.getValue();

                    List<Object> valueList = new ArrayList<>();
                    List<Tuple2<String, String>> parameterList = new ArrayList<>();
                    List<String> value2Columns = new ArrayList<>();
                    if (!skip && filter.getValueIsFieldName()) {
                        //值是其他字段
                        if (value == null)
                            //空值
                            skip = true;
                        else if (value.getClass()
                                      .isArray() || Iterable.class.isAssignableFrom(value.getClass())) {
                            //字段集合
                            Collection<Object> values = value.getClass()
                                                             .isArray()
                                                        ? Arrays.asList((Object[]) value)
                                                        : (Collection<Object>) value;
                            for (Object item : values) {
                                value2Columns.add(EntityTypeHandler.getColumn(EntityTypeHandler.getFieldByFieldName(item.toString(),
                                                                                                                    entityType,
                                                                                                                    dtoType.isAnnotationPresent(EntityMapping.class)
                                                                                                                    ? dtoType
                                                                                                                    : null),
                                                                              config.getNameConvertType()));
                            }
                        } else if (StringUtils.hasText(String.valueOf(value)))
                            //单个字段
                            value2Columns.add(EntityTypeHandler.getColumn(EntityTypeHandler.getFieldByFieldName(String.valueOf(value),
                                                                                                                entityType,
                                                                                                                dtoType.isAnnotationPresent(EntityMapping.class)
                                                                                                                ? dtoType
                                                                                                                : null),
                                                                          config.getNameConvertType()));
                        else
                            //无效值
                            skip = true;

                        //忽略大小写的操作
                        if (!skip && ignoreCase) {
                            value2Columns = value2Columns.stream()
                                                         .map(x -> String.format("lower(%s)",
                                                                                 x))
                                                         .collect(Collectors.toList());
                        }
                    } else if (!skip) {
                        //值转为sql语句
                        if (value != null) {
                            if (value.getClass()
                                     .isArray() || Iterable.class.isAssignableFrom(value.getClass())) {
                                //集合
                                Collection<Object> values = value.getClass()
                                                                 .isArray()
                                                            ? Arrays.asList((Object[]) value)
                                                            : (Collection<Object>) value;

                                for (Object o : values) {
                                    if (noParameter) {
                                        //无参模式防止SQL注入
                                        String stringValue = o == null
                                                             ? null
                                                             : ignoreCase
                                                               ? o.toString()
                                                                  .toLowerCase(Locale.ROOT)
                                                               : o.toString();

                                        parameterList.add(new Tuple2<>(filter.getFieldName(),
                                                                       SqlExtension.replaceInjection(stringValue)));
                                        valueList.add(parameterList.get(parameterList.size() - 1));
                                    } else {
                                        valueList.add(o);
                                        parameterList.add(field2ParameterSql(field,
                                                                             parameter,
                                                                             //如果比较方式为日期范围，那么随便传个日期格式进去，以规避格式化日期的时候报错，参数的值会在后续的代码中更新为正确的值
                                                                             filter.getCompare()
                                                                                   .equals(FilterCompare.DateRange)
                                                                             ? new Date()
                                                                             : o,
                                                                             true,
                                                                             ignoreCase));
                                    }
                                }
                            } else {
                                //单个值

                                //无参模式防止SQL注入
                                if (noParameter) {
                                    if (value.getClass()
                                             .equals(Date.class)) {
                                        parameterList.add(new Tuple2<>(filter.getFieldName(),
                                                                       new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value)));
                                        valueList.add(parameterList.get(0));
                                    } else {
                                        String stringValue = ignoreCase
                                                             ? value.toString()
                                                                    .toLowerCase(Locale.ROOT)
                                                             : value.toString();
                                        parameterList.add(new Tuple2<>(filter.getFieldName(),
                                                                       SqlExtension.replaceInjection(stringValue)));
                                    }
                                } else {
                                    valueList.add(value);
                                    parameterList.add(field2ParameterSql(field,
                                                                         parameter,
                                                                         value,
                                                                         true,
                                                                         ignoreCase));
                                }
                            }
                        }
                    }

                    if (!skip) switch (filter.getCompare()) {
                        default:
                        case EqIgnoreCase:
                        case Eq:
                            if (value == null) _sql.append(String.format("%s IS NULL ",
                                                                         column));
                            else {
                                if (filter.getValueIsFieldName()) _sql.append(String.format("%s = %s ",
                                                                                            column,
                                                                                            value2Columns.get(0)));
                                else if (noParameter) _sql.append(String.format("%s = '%s' ",
                                                                                column,
                                                                                parameterList.get(0).b));
                                else _sql.append(String.format("%s = %s ",
                                                               column,
                                                               parameterList.get(0).b));
                            }
                            break;
                        case NotEq:
                            if (value == null) _sql.append(String.format("%s IS NOT NULL ",
                                                                         column));
                            else {
                                if (filter.getValueIsFieldName()) _sql.append(String.format("%s != %s ",
                                                                                            column,
                                                                                            value2Columns.get(0)));
                                else if (noParameter) _sql.append(String.format("%s != '%s' ",
                                                                                column,
                                                                                parameterList.get(0).b));
                                else _sql.append(String.format("%s != %s ",
                                                               column,
                                                               parameterList.get(0).b));
                            }
                            break;
                        case Le:
                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s <= %s ",
                                                                                        column,
                                                                                        value2Columns.get(0)));
                            else if (!StringUtils.hasText(parameterList.get(0).b)) continue;
                            else if (noParameter) _sql.append(String.format("%s <= '%s' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s <= %s ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case Lt:
                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s < %s ",
                                                                                        column,
                                                                                        value2Columns.get(0)));
                            else if (!StringUtils.hasText(parameterList.get(0).b)) continue;
                            else if (noParameter) _sql.append(String.format("%s < '%s' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s < %s ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case Ge:
                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s >= %s ",
                                                                                        column,
                                                                                        value2Columns.get(0)));
                            else if (!StringUtils.hasText(parameterList.get(0).b)) continue;
                            else if (noParameter) _sql.append(String.format("%s >= '%s' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s >= %s ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case Gt:
                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s > %s ",
                                                                                        column,
                                                                                        value2Columns.get(0)));
                            else if (!StringUtils.hasText(parameterList.get(0).b)) continue;
                            else if (noParameter) _sql.append(String.format("%s > '%s' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s > %s ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case In:
                            if (!StringUtils.hasText(parameterList.get(0).b)) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName())
                                _sql.append(String.format("%s LIKE CONCAT('%%', %s, '%%') ",
                                                          column,
                                                          value2Columns.get(0)));
                            else if (noParameter) _sql.append(String.format("%s LIKE '%%%s%%' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s LIKE CONCAT('%%', %s, '%%') ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case NotIn:
                            if (!StringUtils.hasText(parameterList.get(0).b)) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName())
                                _sql.append(String.format("%s NOT LIKE CONCAT('%%', %s, '%%') ",
                                                          column,
                                                          value2Columns.get(0)));
                            else if (noParameter) _sql.append(String.format("%s NOT LIKE '%%%s%%' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s NOT LIKE CONCAT('%%', %s, '%%') ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case InStart:
                            if (!StringUtils.hasText(parameterList.get(0).b)) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s LIKE CONCAT(%s, '%%') ",
                                                                                        column,
                                                                                        value2Columns.get(0)));
                            else if (noParameter) _sql.append(String.format("%s LIKE '%s%%' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s LIKE CONCAT(%s, '%%') ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case NotInStart:
                            if (!StringUtils.hasText(parameterList.get(0).b)) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s NOT LIKE CONCAT(%s, '%%') ",
                                                                                        column,
                                                                                        value2Columns.get(0)));
                            else if (noParameter) _sql.append(String.format("%s NOT LIKE '%s%%' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s NOT LIKE CONCAT(%s, '%%') ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case InEnd:
                            if (!StringUtils.hasText(parameterList.get(0).b)) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s LIKE CONCAT('%%', %s) ",
                                                                                        column,
                                                                                        value2Columns.get(0)));
                            else if (noParameter) _sql.append(String.format("%s LIKE '%%%s' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s LIKE CONCAT('%%', %s) ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case NotInEnd:
                            if (!StringUtils.hasText(parameterList.get(0).b)) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s NOT LIKE CONCAT('%%', %s) ",
                                                                                        column,
                                                                                        value2Columns.get(0)));
                            else if (noParameter) _sql.append(String.format("%s NOT LIKE '%%%s' ",
                                                                            column,
                                                                            parameterList.get(0).b));
                            else _sql.append(String.format("%s NOT LIKE CONCAT('%%', %s) ",
                                                           column,
                                                           parameterList.get(0).b));
                            break;
                        case IncludedIn:
                            if (!StringUtils.hasText(parameterList.get(0).b)) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName())
                                _sql.append(String.format("%s LIKE CONCAT('%%', %s, '%%') ",
                                                          value2Columns.get(0),
                                                          column));
                            else if (noParameter) _sql.append(String.format("'%s' LIKE CONCAT('%%', %s, '%%') ",
                                                                            parameterList.get(0).b,
                                                                            column));
                            else _sql.append(String.format("%s LIKE CONCAT('%%', %s, '%%') ",
                                                           parameterList.get(0).b,
                                                           column));
                            break;
                        case NotIncludedIn:
                            if (!StringUtils.hasText(parameterList.get(0).b)) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName())
                                _sql.append(String.format("%s NOT LIKE CONCAT('%%', %s, '%%') ",
                                                          value2Columns.get(0),
                                                          column));
                            else if (noParameter) _sql.append(String.format("'%s' NOT LIKE CONCAT('%%', %s, '%%') ",
                                                                            parameterList.get(0).b,
                                                                            column));
                            else _sql.append(String.format("%s NOT LIKE CONCAT('%%', %s, '%%') ",
                                                           parameterList.get(0).b,
                                                           column));
                            break;
                        case InSet:
                            if (parameterList.size() == 0 && value2Columns.size() == 0) {
                                //设置一个恒为false的条件，以达到和预期相同的结果
                                _sql.append(" 0 = 1 ");
//                            skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s IN (%s) ",
                                                                                        column,
                                                                                        String.join(", ",
                                                                                                    value2Columns)));
                            else if (noParameter) _sql.append(String.format("%s IN ('%s') ",
                                                                            column,
                                                                            parameterList.stream()
                                                                                         .map(x -> x.b)
                                                                                         .collect(Collectors.joining("', '"))));
                            else _sql.append(String.format("%s IN (%s) ",
                                                           column,
                                                           parameterList.stream()
                                                                        .map(x -> x.b)
                                                                        .collect(Collectors.joining(", "))));
                            break;
                        case NotInSet:
                            if (parameterList.size() == 0 && value2Columns.size() == 0) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s NOT IN (%s) ",
                                                                                        column,
                                                                                        String.join(", ",
                                                                                                    value2Columns)));
                            else if (noParameter) _sql.append(String.format("%s NOT IN ('%s') ",
                                                                            column,
                                                                            parameterList.stream()
                                                                                         .map(x -> x.b)
                                                                                         .collect(Collectors.joining("', '"))));
                            else _sql.append(String.format("%s NOT IN (%s) ",
                                                           column,
                                                           parameterList.stream()
                                                                        .map(x -> x.b)
                                                                        .collect(Collectors.joining(", "))));
                            break;
                        case Range:
                            if (parameterList.size() == 0 && value2Columns.size() == 0) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) _sql.append(String.format("%s >= %s AND %s < %s ",
                                                                                        column,
                                                                                        value2Columns.get(0),
                                                                                        column,
                                                                                        value2Columns.get(1)));
                            else if (noParameter) _sql.append(String.format("%s >= '%s' AND %s < '%s' ",
                                                                            column,
                                                                            parameterList.get(0).b,
                                                                            column,
                                                                            parameterList.get(1).b));
                            else _sql.append(String.format("%s >= %s AND %s < %s ",
                                                           column,
                                                           parameterList.get(0).b,
                                                           column,
                                                           parameterList.get(1).b));
                            break;
                        case DateRange:
                            if (parameterList.size() == 0 && value2Columns.size() == 0) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) {
                                _sql.append(String.format("%s >= %s AND %s < %s ",
                                                          column,
                                                          value2Columns.get(0),
                                                          column,
                                                          value2Columns.get(1)));
                            } else {
                                Calendar calendar_1 = Calendar.getInstance();
                                if (valueList.get(1)
                                             .getClass()
                                             .equals(Date.class)) {
                                    calendar_1.setTime((Date) valueList.get(1));
                                    calendar_1.add(Calendar.DAY_OF_MONTH,
                                                   1);
                                } else {
                                    if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d?$",
                                                        valueList.get(1)
                                                                 .toString())) {
                                        try {
                                            calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(valueList.get(1)
                                                                                                                 .toString()));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(1)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd"));
                                        }
                                        calendar_1.add(Calendar.DAY_OF_MONTH,
                                                       1);
                                    } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?$",
                                                               valueList.get(1)
                                                                        .toString())) {
                                        try {
                                            calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(valueList.get(1)
                                                                                                                 .toString()
                                                                                                                + "-01"));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(1)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd"));
                                        }
                                        calendar_1.add(Calendar.MONTH,
                                                       1);
                                    } else if (Pattern.matches("^\\d\\d\\d\\d$",
                                                               valueList.get(1)
                                                                        .toString())) {
                                        try {
                                            calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(valueList.get(1)
                                                                                                                 .toString()
                                                                                                                + "-01-01"));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(1)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd"));
                                        }
                                        calendar_1.add(Calendar.YEAR,
                                                       1);
                                    } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?$",
                                                               valueList.get(1)
                                                                        .toString())) {
                                        try {
                                            calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                                    valueList.get(1)
                                                             .toString() + ":00:00"));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(1)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd HH:mm:ss"));
                                        }
                                        calendar_1.add(Calendar.HOUR,
                                                       1);
                                    } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?:\\d\\d?$",
                                                               valueList.get(1)
                                                                        .toString())) {
                                        try {
                                            calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                                    valueList.get(1)
                                                             .toString() + ":00"));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(1)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd HH:mm:ss"));
                                        }
                                        calendar_1.add(Calendar.MINUTE,
                                                       1);
                                    } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?:\\d\\d?:\\d\\d?$",
                                                               valueList.get(1)
                                                                        .toString())) {
                                        try {
                                            calendar_1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(valueList.get(1)
                                                                                                                          .toString()));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(1)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd HH:mm:ss"));
                                        }
                                        calendar_1.add(Calendar.SECOND,
                                                       1);
                                    } else
                                        throw new ModuleException(Strings.getDateFormatNonStandard("DateRange",
                                                                                                   "Value[1]",
                                                                                                   "yyyy、yyyy-MM、yyyy-MM-dd、yyyy-MM-dd HH、yyyy、yyyy-MM-dd HH:mm、yyyy-MM-dd HH:mm:ss"));
                                }

                                Calendar calendar_0 = Calendar.getInstance();
                                if (valueList.get(0)
                                             .getClass()
                                             .equals(Date.class)) {
                                    calendar_0.setTime((Date) valueList.get(0));
                                } else {
                                    if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d?$",
                                                        valueList.get(0)
                                                                 .toString())) {
                                        try {
                                            calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(valueList.get(0)
                                                                                                                 .toString()));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(0)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd"));
                                        }
                                    } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?$",
                                                               valueList.get(0)
                                                                        .toString())) {
                                        try {
                                            calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(valueList.get(0)
                                                                                                                 .toString()
                                                                                                                + "-01"));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(0)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd"));
                                        }
                                    } else if (Pattern.matches("^\\d\\d\\d\\d$",
                                                               valueList.get(0)
                                                                        .toString())) {
                                        try {
                                            calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(valueList.get(0)
                                                                                                                 .toString()
                                                                                                                + "-01-01"));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(0)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd"));
                                        }
                                    } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?$",
                                                               valueList.get(0)
                                                                        .toString())) {
                                        try {
                                            calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                                    valueList.get(0)
                                                             .toString() + ":00:00"));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(0)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd HH:mm:ss"));
                                        }
                                    } else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?:\\d\\d?$",
                                                               valueList.get(0)
                                                                        .toString())) {
                                        try {
                                            calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                                    valueList.get(0)
                                                             .toString() + ":00"));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(0)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd HH:mm:ss"));
                                        }
                                    } else {
                                        try {
                                            calendar_0.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(valueList.get(0)
                                                                                                                          .toString()));
                                        } catch (ParseException ex) {
                                            throw new ModuleException(Strings.getFormatDateFailed(valueList.get(0)
                                                                                                           .toString(),
                                                                                                  "yyyy-MM-dd HH:mm:ss"));
                                        }
                                    }
                                }

                                if (noParameter) {
                                    //默认时间序列化格式
                                    SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    _sql.append(String.format("%s >= '%s' AND %s < '%s' ",
                                                              column,
                                                              defaultDateFormat.format(calendar_0.getTime()),
                                                              column,
                                                              defaultDateFormat.format(calendar_1.getTime())));
                                } else {
                                    //更新参数的值为处理后的值
                                    parameter.put(parameterList.get(0).a,
                                                  calendar_0.getTime());
                                    parameter.put(parameterList.get(1).a,
                                                  calendar_1.getTime());
                                    _sql.append(String.format("%s >= %s AND %s < %s ",
                                                              column,
                                                              parameterList.get(0).b,
                                                              column,
                                                              parameterList.get(1).b));
                                }
                            }
                            break;
                        case DateRangeStrict:
                            if (parameterList.size() == 0 && value2Columns.size() == 0) {
                                skip = true;
                                break;
                            }

                            if (filter.getValueIsFieldName()) {
                                _sql.append(String.format("%s >= %s AND %s < %s ",
                                                          column,
                                                          value2Columns.get(0),
                                                          column,
                                                          value2Columns.get(1)));
                            } else if (noParameter) {
                                //默认时间序列化格式
                                SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                _sql.append(String.format("%s >= '%s' AND %s < '%s' ",
                                                          column,
                                                          defaultDateFormat.format((Date) valueList.get(0)),
                                                          column,
                                                          defaultDateFormat.format((Date) valueList.get(1))));
                            } else _sql.append(String.format("%s >= %s AND %s < %s ",
                                                             column,
                                                             parameterList.get(0).b,
                                                             column,
                                                             parameterList.get(1).b));
                            break;
                    }
                }
            }

            if (!skip) {
                if (i != 0) sql.append(String.format(" %s ",
                                                     filter.getRelation()));
                sql.append(_sql);
            }
        }

        return sql.toString();
    }

    /**
     * 动态排序条件转为SQL语句
     *
     * @param order      动态排序条件
     * @param alias      数据表别名
     * @param entityType 实体类型
     * @param dtoType    业务模型类型
     * @return SQL语句
     */
    public String dynamicOrder2Sql(DynamicOrder order,
                                   String alias,
                                   Class<?> entityType,
                                   Class<?> dtoType)
            throws
            ModuleException {
        if (order == null) return "";

        //数据表别名
        String alias_filter = order.getAlias();
        if (!StringUtils.hasText(alias_filter)) alias_filter = alias;

        //数据表别名+列名
        StringBuilder sql = new StringBuilder();
        if (StringUtils.hasText(order.getFieldName())) {
            //字段
            Field field = EntityTypeHandler.getFieldByFieldName(order.getFieldName(),
                                                                entityType,
                                                                dtoType.isAnnotationPresent(EntityMapping.class)
                                                                ? dtoType
                                                                : null);

            String column = getNameWithAlias(EntityTypeHandler.getColumn(field,
                                                                         config.getNameConvertType()),
                                             alias_filter);

            OrderMethod method = order.getMethod();
            if (method == null) method = OrderMethod.DESC;

            sql.append(String.format(" %s %s ",
                                     column,
                                     method));
        }

        String advancedOrder = advancedOrder2Sql(order.getAdvancedOrder(),
                                                 alias,
                                                 entityType,
                                                 dtoType);
        sql.append(String.format(" %s %s ",
                                 StringUtils.hasText(advancedOrder)
                                 ? ", "
                                 : "",
                                 advancedOrder));

        return sql.toString();
    }

    /**
     * 高级排序条件转为SQL语句
     *
     * @param orders     高级排序条件
     * @param alias      数据表别名
     * @param entityType 实体类型
     * @param dtoType    业务模型类型
     * @return SQL语句
     */
    public String advancedOrder2Sql(List<AdvancedOrder> orders,
                                    String alias,
                                    Class<?> entityType,
                                    Class<?> dtoType)
            throws
            ModuleException {
        if (orders == null || orders.size() == 0) return "";

        StringBuilder sql = new StringBuilder();

        for (int i = 0; i < orders.size(); i++) {
            AdvancedOrder order = orders.get(i);
            if (order == null) continue;

            //数据表别名
            String alias_order = order.getAlias();
            if (!StringUtils.hasText(alias_order)) alias_order = alias;

            //字段
            Field field = EntityTypeHandler.getFieldByFieldName(order.getFieldName(),
                                                                entityType,
                                                                dtoType.isAnnotationPresent(EntityMapping.class)
                                                                ? dtoType
                                                                : null);

            //数据表别名+列名
            String column = getNameWithAlias(EntityTypeHandler.getColumn(field,
                                                                         config.getNameConvertType()),
                                             alias_order);

            OrderMethod method = order.getMethod();

            sql.append(String.format(" %s %s ",
                                     column,
                                     method));

            if (i != orders.size() - 1) sql.append(", ");
        }

        return sql.toString();
    }

    /**
     * 原始Sql语句转为获取是否存在记录的Sql语句
     *
     * @param originalSql 原始Sql语句
     * @return Sql语句
     */
    public String originalSql2AnySql(String originalSql) {
        return String.format("SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END \n\n"
                                     + "FROM (%s) AS %s ",
                             originalSql,
                             getValueWithCharacter(String.format("t_%s",
                                                                 new Date().getTime())));
    }

    /**
     * 原始Sql语句转为获取首条记录的Sql语句
     *
     * @param originalSql 原始Sql语句
     * @param offset      偏移量
     * @param count       数据量
     * @return Sql语句
     */
    public abstract String originalSql2LimitSql(String originalSql,
                                                int offset,
                                                int count)
            throws
            ModuleException;

    /**
     * 原始Sql语句转为获取总数的Sql语句
     *
     * @param originalSql 原始Sql语句
     * @return Sql语句
     */
    public String originalSql2CountSql(String originalSql) {
        return String.format("SELECT COUNT(1) \r\n"
                                     + "FROM (%s) AS %s ",
                             originalSql,
                             getValueWithCharacter(String.format("t_%s",
                                                                 new Date().getTime())));
    }

    /**
     * 分页设置转为SQL语句
     *
     * @param pagination  分页设置
     * @param originalSql 原始Sql语句
     * @return Sql语句
     */
    public String pagination2Sql(Pagination pagination,
                                 String originalSql)
            throws
            ModuleException {
        if (pagination == null || pagination.getNope() || pagination.getUserPageHelper()) return originalSql;
        return originalSql2LimitSql(originalSql,
                                    (pagination.getPageNum() - 1) * pagination.getPageSize(),
                                    pagination.getPageSize());
    }

    /**
     * 查询设置转Sql语句
     *
     * @param executor       查询设置
     * @param paging         包括分页
     * @param noParameter    无参数语句
     * @param withoutOrderBy 排除排序语句
     * @return Sql语句
     */
    public String executor2Sql(ExecutorDTO executor,
                               boolean paging,
                               boolean noParameter,
                               boolean withoutOrderBy)
            throws
            ModuleException {
        String sql = executor2Sql(executor,
                                  noParameter,
                                  withoutOrderBy,
                                  null,
                                  null,
                                  null,
                                  null,
                                  null);

        if (paging) sql = pagination2Sql(executor.getPagination(),
                                         sql);

        return sql;
    }

    /**
     * 查询设置转Sql语句
     *
     * @param executor         查询设置
     * @param noParameter      无参数语句
     * @param withoutOrderBy   排除排序语句
     * @param customColumnsSql 自定义列Sql语句
     * @param customFromSql    自定义From Sql语句
     * @param customWhereSql   自定义条件语句
     * @param customGroupBySql 自定义分组语句
     * @param customOrderBySql 自定义排序语句
     * @return Sql语句
     */
    public String executor2Sql(ExecutorDTO executor,
                               boolean noParameter,
                               boolean withoutOrderBy,
                               String customColumnsSql,
                               String customFromSql,
                               String customWhereSql,
                               String customGroupBySql,
                               String customOrderBySql)
            throws
            ModuleException {
        String alias = executor.getAlias();
        if (!StringUtils.hasText(alias)) alias = "a";

        //SELECT部分
        String columns = StringUtils.hasText(customColumnsSql)
                         ? customColumnsSql
                         : executor.getAllColumns()
                           ? " * "
                           : columns2Sql(CollectionsExtension.anyPlus(executor.getCustomFieldNames())
                                         ? executor.getCustomFieldNames()
                                                   .stream()
                                                   .map(c -> EntityTypeHandler.getColumnByFieldName(c,
                                                                                                    executor.getEntityType(),
                                                                                                    config.getNameConvertType()))
                                                   .collect(Collectors.toList())
                                         : getColumns(executor.getEntityType(),
                                                      executor.getDtoType(),
                                                      executor.getMainTagLevel(),
                                                      executor.getCustomTags(),
                                                      false,
                                                      false,
                                                      false),
                                         alias);

        //FROM部分
        String from;
        if (StringUtils.hasText(customFromSql))
            from = customFromSql;
        else if (executor.getWithSQL()
                         .size() > 0) {
            String withSql = executor.getWithSQL()
                                     .get(config.getDbType());
            if (withSql == null)
                withSql = CollectionsExtension.firstValue(executor.getWithSQL());

            from = withSql2Sql(withSql,
                               alias);
        } else
            from = getName2Sql(executor.getSchema(),
                               executor.getTableName(),
                               alias);

        //WHERE部分
        String where;
        if (StringUtils.hasText(customWhereSql)) where = customWhereSql;
        else {
            //自定义SQL过滤条件
            List<String> whereList = new ArrayList<>(executor.getWithWhereSQLs());
            //动态过滤条件
            whereList.add(dynamicFilter2Sql(executor.getDynamicFilters(),
                                            alias,
                                            noParameter,
                                            executor.getParameter(),
                                            executor.getEntityType(),
                                            executor.getDtoType()));

            //所有条件
            where = whereList.stream()
                             .filter(StringUtils::hasText)
                             .collect(Collectors.joining(") AND ("));
            if (StringUtils.hasText(where)) where = String.format("WHERE (%s)",
                                                                  where);
        }

        //GROUP BY 部分
        String groupBy = null;
        if (StringUtils.hasText(customGroupBySql)) groupBy = customGroupBySql;
        else if (CollectionsExtension.anyPlus(executor.getGroupByFieldNames()))
            groupBy = columns2Sql(executor.getGroupByFieldNames(),
                                  alias);
        if (!StringUtils.hasText(groupBy))
            groupBy = "";

        //ORDER BY部分
        String orderBy = null;
        if (!withoutOrderBy) {
            if (StringUtils.hasText(customOrderBySql)) orderBy = customOrderBySql;
            else {
                //自定义SQL排序条件
                String orderby_c = executor.getWithOrderBySQL();
                //动态排序条件
                String orderby_d = dynamicOrder2Sql(executor.getDynamicOrder(),
                                                    alias,
                                                    executor.getEntityType(),
                                                    executor.getDtoType());

                orderBy = Stream.of(orderby_c,
                                    orderby_d)
                                .filter(StringUtils::hasText)
                                .collect(Collectors.joining(", "));
                if (StringUtils.hasText(orderBy)) orderBy = String.format("ORDER BY %s",
                                                                          orderBy);
            }
        }
        if (!StringUtils.hasText(orderBy))
            orderBy = "";

        String sql = String.format("SELECT %s \r\n"
                                           + "FROM %s \r\n"
                                           + "%s \r\n"
                                           + "%s \r\n"
                                           + "%s",
                                   columns,
                                   from,
                                   where,
                                   groupBy,
                                   orderBy);

        //处理sql中的自定义参数
        if (executor.getCustomParameter()
                    .size() > 0) sql = replaceParameter(sql,
                                                        executor.getParameter(),
                                                        executor.getCustomParameter(),
                                                        noParameter);

        return sql;
    }

    /**
     * 查询设置转获取总记录数的Sql语句
     *
     * @param executor    查询设置
     * @param noParameter 无参数语句
     * @return Sql语句
     */
    public String executor2CountSql(ExecutorDTO executor,
                                    boolean noParameter,
                                    boolean withoutOrderBy)
            throws
            ModuleException {
        return executor2Sql(executor,
                            noParameter,
                            withoutOrderBy,
                            "COUNT(*)",
                            null,
                            null,
                            null,
                            "");
    }

    /**
     * 查询设置转获取最大值的Sql语句
     *
     * @param executor    查询设置
     * @param noParameter 无参数语句
     * @return Sql语句
     */
    public String executor2MaxSql(ExecutorDTO executor,
                                  boolean noParameter,
                                  boolean withoutOrderBy)
            throws
            ModuleException {
        return executor2Sql(executor,
                            noParameter,
                            withoutOrderBy,
                            String.format("MAX(%s)",
                                          getNameWithAlias(EntityTypeHandler.getColumnByFieldName(executor.getCustomFieldNames()
                                                                                                          .get(0),
                                                                                                  executor.getEntityType(),
                                                                                                  config.getNameConvertType()),
                                                           executor.getAlias())),
                            null,
                            null,
                            null,
                            "");
    }

    /**
     * 查询设置转获取最小值的Sql语句
     *
     * @param executor    查询设置
     * @param noParameter 无参数语句
     * @return Sql语句
     */
    public String executor2MinSql(ExecutorDTO executor,
                                  boolean noParameter,
                                  boolean withoutOrderBy)
            throws
            ModuleException {
        return executor2Sql(executor,
                            noParameter,
                            withoutOrderBy,
                            String.format("MIN(%s)",
                                          getNameWithAlias(EntityTypeHandler.getColumnByFieldName(executor.getCustomFieldNames()
                                                                                                          .get(0),
                                                                                                  executor.getEntityType(),
                                                                                                  config.getNameConvertType()),
                                                           executor.getAlias())),
                            null,
                            null,
                            null,
                            "");
    }

    /**
     * 查询设置转获取平均值的Sql语句
     *
     * @param executor    查询设置
     * @param noParameter 无参数语句
     * @return Sql语句
     */
    public String executor2AvgSql(ExecutorDTO executor,
                                  boolean noParameter)
            throws
            ModuleException {
        return executor2Sql(executor,
                            noParameter,
                            true,
                            String.format("AVG(%s)",
                                          getNameWithAlias(EntityTypeHandler.getColumnByFieldName(executor.getCustomFieldNames()
                                                                                                          .get(0),
                                                                                                  executor.getEntityType(),
                                                                                                  config.getNameConvertType()),
                                                           executor.getAlias())),
                            null,
                            null,
                            null,
                            "");
    }

    /**
     * 查询设置转获取合计值的Sql语句
     *
     * @param executor    查询设置
     * @param noParameter 无参数语句
     * @return Sql语句
     */
    public String executor2SumSql(ExecutorDTO executor,
                                  boolean noParameter)
            throws
            ModuleException {
        return executor2Sql(executor,
                            noParameter,
                            true,
                            String.format("SUM(%s)",
                                          getNameWithAlias(EntityTypeHandler.getColumnByFieldName(executor.getCustomFieldNames()
                                                                                                          .get(0),
                                                                                                  executor.getEntityType(),
                                                                                                  config.getNameConvertType()),
                                                           executor.getAlias())),
                            null,
                            null,
                            null,
                            "");
    }

    /**
     * 插入设置转脚本
     *
     * @param inserter    插入设置
     * @param noParameter 无参数语句
     * @param data        数据
     * @return 脚本
     */
    public String inserter2Script(InserterDTO inserter,
                                  boolean noParameter,
                                  Object data)
            throws
            ModuleException {
        //表名
        String tableName = getName2Sql(inserter.getSchema(),
                                       inserter.getTableName());

        //指定字段和列
        Map<String, String> fieldWithColumns = getFieldNameWithColumns(inserter.getEntityType(),
                                                                       inserter.getDtoType(),
                                                                       inserter.getMainTagLevel(),
                                                                       inserter.getCustomTags(),
                                                                       inserter.getExceptionFieldNames(),
                                                                       null,
                                                                       inserter.getIgnoreFieldNames(),
                                                                       null,
                                                                       false,
                                                                       true,
                                                                       true);

        //列
        String columns = columns2Sql(fieldWithColumns.values());

        //参数
        String parameters = fieldName2ParameterSql(new ArrayList<>(fieldWithColumns.keySet()),
                                                   inserter.getEntityType(),
                                                   noParameter,
                                                   inserter.getParameter(),
                                                   data);

        StringBuilder sb = new StringBuilder("<script>\n");

        switch (config.getDbType()) {
            case JdbcOracle12c:
            case JdbcOracle18c:
            case JdbcOracle19c:
            case JdbcOracle21c:
                //Oracle自增列使用序列赋值
                Field identityField = EntityTypeHandler.getIdentityKeyField(inserter.getEntityType());
                if (identityField != null) {
                    ColumnSetting columnSetting = AnnotationUtils.getAnnotation(identityField,
                                                                                ColumnSetting.class);
                    if (columnSetting != null) {
                        sb.append(String.format("        INSERT INTO %s \r\n",
                                                tableName));
                        sb.append(String.format("            (%s, %s) \r\n",
                                                EntityTypeHandler.getColumn(identityField,
                                                                            config.getNameConvertType()),
                                                columns));
                        sb.append("        VALUES \r\n");
                        sb.append(String.format("            (#{%s}, %s)\r\n",
                                                identityField.getName(),
                                                parameters));
                        break;
                    }
                }
            default:
                sb.append(String.format("        INSERT INTO %s \r\n",
                                        tableName));
                sb.append(String.format("            (%s) \r\n",
                                        columns));
                sb.append("        VALUES \r\n");
                sb.append(String.format("            (%s)\r\n",
                                        parameters));
                break;
        }

        sb.append("</script>");
        return sb.toString();
    }

    /**
     * 返回当前的获取序列值的脚本
     *
     * @param inserter 插入设置
     * @return 脚本
     */
    public Tuple3<Boolean, String, Class<?>> selectKey2Script(InserterDTO inserter)
            throws
            ModuleException {
        //Oracle自增列使用序列赋值
        Field identityField = EntityTypeHandler.getIdentityKeyField(inserter.getEntityType());
        if (identityField != null) {
            ColumnSetting columnSetting = AnnotationUtils.getAnnotation(identityField,
                                                                        ColumnSetting.class);
            if (columnSetting != null) {
                switch (config.getDbType()) {
                    case JdbcOracle12c:
                    case JdbcOracle18c:
                    case JdbcOracle19c:
                    case JdbcOracle21c:
                        return new Tuple3<>(true,
                                            String.format("SELECT %s.nextval FROM DUAL",
                                                          getName2Sql(inserter.getSchema(),
                                                                      columnSetting.oracleIdentitySequence())),
                                            identityField.getType());
                }
            }
        }

        return new Tuple3<>(false,
                            null,
                            null);
    }

    /**
     * 插入设置转批量脚本
     *
     * @param inserter 插入设置
     * @return 脚本
     */
    public String inserter2ForeachScript(InserterDTO inserter) {
        //#TODO 待调整
        //表名
        String tableName = getName2Sql(inserter.getSchema(),
                                       inserter.getTableName());

        //指定字段和列
        Map<String, String> fieldWithColumns = getFieldNameWithColumns(inserter.getEntityType(),
                                                                       inserter.getDtoType(),
                                                                       inserter.getMainTagLevel(),
                                                                       inserter.getCustomTags(),
                                                                       inserter.getExceptionFieldNames(),
                                                                       null,
                                                                       inserter.getIgnoreFieldNames(),
                                                                       null,
                                                                       false,
                                                                       true,
                                                                       true);

        //列
        String columns = columns2Sql(fieldWithColumns.values());

        //参数
        String parameters = fieldWithColumns.keySet()
                                            .stream()
                                            .map(field -> String.format("#{item.%s}",
                                                                        field))
                                            .collect(Collectors.joining(", "));

        return String.format("<script>\r\n"
                                     + "        INSERT INTO %s \r\n"
                                     + "            (%s) \r\n"
                                     + "        VALUES \r\n"
                                     + "        <foreach item=\"item\" collection=\"list\" separator=\",\">\r\n"
                                     + "            (%s)\r\n"
                                     + "        </foreach>\r\n"
                                     + "</script>",
                             tableName,
                             columns,
                             parameters);
    }

    /**
     * 更新设置转脚本
     *
     * @param updater     更新设置
     * @param noParameter 无参数语句
     * @param data        数据
     * @return 脚本
     */
    public String updater2Script(UpdaterDTO updater,
                                 boolean noParameter,
                                 Object data)
            throws
            ModuleException {
        //别名
        String alias = updater.getAlias();

        //表名
        String tableName = getName2Sql(updater.getSchema(),
                                       updater.getTableName(),
                                       alias);

        //SET部分 列+参数
        String set;
        if (updater.getCustomSetByFieldName()
                   .size() > 0 || updater.getDynamicSetters()
                                         .size() > 0) {
            //更新指定字段
            String set_c = customSetByFieldNames2UpdateSql(updater.getEntityType(),
                                                           updater.getCustomSetByFieldName(),
                                                           alias,
                                                           noParameter,
                                                           updater.getParameter());
            //指定Sql语句更新指定字段
            String set_sql = customSetByFieldNameWithSql2UpdateSql(updater.getEntityType(),
                                                                   updater.getCustomSetByFieldNameWithSql(),
                                                                   alias);
            //动态更新指定字段
            String set_cd = dynamicSetters2UpdateSql(updater.getEntityType(),
                                                     updater.getDynamicSetters(),
                                                     alias,
                                                     noParameter,
                                                     updater.getParameter());

            set = Stream.of(set_c,
                            set_sql,
                            set_cd)
                        .filter(StringUtils::hasText)
                        .collect(Collectors.joining(", "));
        } else {
            //更新业务模型相关字段
            //业务模型相关字段和列
            Map<String, String> fieldNameWithColumns = getFieldNameWithColumns(updater.getEntityType(),
                                                                               updater.getDtoType(),
                                                                               updater.getMainTagLevel(),
                                                                               updater.getCustomTags(),
                                                                               updater.getExceptionFieldNames(),
                                                                               null,
                                                                               updater.getIgnoreFieldNames(),
                                                                               null,
                                                                               true,
                                                                               true,
                                                                               true);
            set = fieldWithColumns2UpdateSql(updater.getEntityType(),
                                             fieldNameWithColumns,
                                             alias,
                                             noParameter,
                                             updater.getParameter(),
                                             data);
        }

        //WHERE部分
        String where = "";
        if (updater.getDataList()
                   .size() > 0) {
            //主键/虚拟主键 过滤条件
            Map<String, String> primaryKeyFieldWithColumns = getPrimaryKeyFieldWithColumns(updater.getEntityType(),
                                                                                           updater.getTempKeyFieldNames(),
                                                                                           null,
                                                                                           true);
            String where_p = primaryKeyFieldWithColumns2WhereSql(updater.getEntityType(),
                                                                 primaryKeyFieldWithColumns,
                                                                 alias,
                                                                 noParameter,
                                                                 updater.getParameter(),
                                                                 data);
            if (StringUtils.hasText(where_p)) where = String.format("WHERE %s",
                                                                    where_p);
        } else {
            //自定义SQL过滤条件
            List<String> where_c = updater.getWithWhereSQLs();
            //动态过滤条件
            String where_d = dynamicFilter2Sql(updater.getDynamicFilter(),
                                               alias,
                                               noParameter,
                                               updater.getParameter(),
                                               updater.getEntityType(),
                                               updater.getDtoType());
            where_c.add(where_d);
            where = where_c.stream()
                           .filter(StringUtils::hasText)
                           .collect(Collectors.joining(") AND ("));
            if (StringUtils.hasText(where)) where = String.format("WHERE (%s)",
                                                                  where);
        }

        String sql = String.format("<script>\r\n"
                                           + "        UPDATE %s \r\n"
                                           + "        SET \r\n"
                                           + "            %s \r\n"
                                           + "        %s \r\n"
                                           + "</script>",
                                   tableName,
                                   set,
                                   where);

        //处理sql中的自定义参数
        if (updater.getCustomParameter()
                   .size() > 0) sql = replaceParameter(sql,
                                                       updater.getParameter(),
                                                       updater.getCustomParameter(),
                                                       noParameter);

        return sql;
    }

    /**
     * 删除设置转脚本
     *
     * @param deleter     删除设置
     * @param noParameter 无参数语句
     * @param data        数据
     * @return 脚本
     */
    public String deleter2Script(DeleterDTO deleter,
                                 boolean noParameter,
                                 Object data)
            throws
            ModuleException {
        //别名
        String alias = deleter.getAlias();

        //表名
        String tableName = getName2Sql(deleter.getSchema(),
                                       deleter.getTableName(),
                                       alias);

        //WHERE部分
        String where = "";
        if (deleter.getDataList()
                   .size() > 0) {
            //主键/虚拟主键 过滤条件
            Map<String, String> primaryKeyFieldWithColumns = getPrimaryKeyFieldWithColumns(deleter.getEntityType(),
                                                                                           deleter.getTempKeyFieldNames(),
                                                                                           deleter.getTempKeyColums(),
                                                                                           true);
            String where_p = primaryKeyFieldWithColumns2WhereSql(deleter.getEntityType(),
                                                                 primaryKeyFieldWithColumns,
                                                                 alias,
                                                                 noParameter,
                                                                 deleter.getParameter(),
                                                                 data);
            if (StringUtils.hasText(where_p)) where = String.format("WHERE %s",
                                                                    where_p);
        } else {
            //自定义SQL过滤条件
            List<String> where_c = deleter.getWithWhereSQLs();
            //动态过滤条件
            String where_d = dynamicFilter2Sql(deleter.getDynamicFilter(),
                                               alias,
                                               noParameter,
                                               deleter.getParameter(),
                                               deleter.getEntityType(),
                                               deleter.getDtoType());
            where_c.add(where_d);
            where = where_c.stream()
                           .filter(StringUtils::hasText)
                           .collect(Collectors.joining(") AND ("));
            if (StringUtils.hasText(where)) where = String.format("WHERE (%s)",
                                                                  where);
        }

        String sql = String.format("<script>\r\n"
                                           + "        DELETE FROM %s \r\n"
                                           + "        %s \r\n"
                                           + "</script>",
                                   tableName,
                                   where);

        //处理sql中的自定义参数
        if (deleter.getCustomParameter()
                   .size() > 0) sql = replaceParameter(sql,
                                                       deleter.getParameter(),
                                                       deleter.getCustomParameter(),
                                                       noParameter);

        return sql;
    }
}
