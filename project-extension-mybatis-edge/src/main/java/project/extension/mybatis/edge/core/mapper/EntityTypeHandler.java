package project.extension.mybatis.edge.core.mapper;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.JdbcType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.annotations.*;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.extention.SqlExtension;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.openapi.annotations.OpenApiMainTag;
import project.extension.openapi.annotations.OpenApiMainTags;
import project.extension.openapi.annotations.OpenApiSchema;
import project.extension.openapi.extention.SchemaExtension;
import project.extension.openapi.model.OpenApiSchemaFormat;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类型处理类
 *
 * @author LCTR
 * @date 2022-03-29
 */
@Component
public class EntityTypeHandler {
    public EntityTypeHandler(BaseConfig config) {
        this.config = config;
    }

    private final BaseConfig config;

    /**
     * 获取主键字段集合
     * <p>@ColumnSetting 设置主键</p>
     *
     * @param entityType 实体类型
     * @param <T>        实体类型
     * @return 字段集合
     */
    public static <T> List<Field> getPrimaryKeyField(Class<T> entityType) {
        List<Field> fields = new ArrayList<>();

        for (Field field : entityType.getDeclaredFields()) {
            ColumnSetting executorSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                    ColumnSetting.class);
            if (executorSettingAttribute != null && executorSettingAttribute.primaryKey()) fields.add(field);
        }

        return fields;
    }

    /**
     * 获取主键字段+列集合
     * <p>@ColumnSetting 设置主键</p>
     *
     * @param entityType      实体类型
     * @param nameConvertType 命名规则
     * @param <T>             实体类型
     * @return 字段+列集合
     */
    public static <T> Map<String, String> getPrimaryKeyFieldNameWithColumns(Class<T> entityType,
                                                                            NameConvertType nameConvertType) {
        Map<String, String> fieldWithColumns = new HashMap<>();
        for (Field field : entityType.getDeclaredFields()) {
            ColumnSetting executorSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                    ColumnSetting.class);
            if (executorSettingAttribute != null && executorSettingAttribute.primaryKey())
                fieldWithColumns.put(field.getName(),
                                     getColumn(field,
                                               nameConvertType));
        }

        return fieldWithColumns;
    }

    /**
     * 获取字段对应的列名
     * <p>@ColumnSetting 设置列名</p>
     *
     * @param fields          字段集合
     * @param nameConvertType 命名规则
     * @return 列名集合
     */
    public static List<String> getColumns(Collection<Field> fields,
                                          NameConvertType nameConvertType) {
        return fields.stream()
                     .map(x -> getColumn(x,
                                         nameConvertType))
                     .collect(Collectors.toList());
    }

    /**
     * 获取字段对应的列名
     * <p>@ColumnSetting 设置列名</p>
     *
     * @param field           字段
     * @param nameConvertType 命名规则
     * @return 列名集合
     */
    public static String getColumn(Field field,
                                   NameConvertType nameConvertType) {
        ColumnSetting columnSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                              ColumnSetting.class);
        if (columnSettingAttribute != null && StringUtils.hasText(columnSettingAttribute.name()))
            return columnSettingAttribute.name();
        return getColumn(field.getName(),
                         nameConvertType);
    }

    /**
     * 获取字段名称对应的列名
     *
     * @param field           字段名称
     * @param nameConvertType 命名规则
     * @return 列名集合
     */
    public static String getColumn(String field,
                                   NameConvertType nameConvertType) {
        return SqlExtension.convertName(field,
                                        nameConvertType);
    }

    /**
     * 获取字段的数据库列名
     * <p>@ColumnSetting 设置列名</p>
     *
     * @param fieldNames      字段名称
     * @param entityType      实体类型
     * @param nameConvertType 命名规则
     * @return 数据库列名
     */
    public static List<String> getColumnsByFieldName(Collection<String> fieldNames,
                                                     Class<?> entityType,
                                                     NameConvertType nameConvertType) {
        return fieldNames.stream()
                         .map(fieldName -> getColumnByFieldName(fieldName,
                                                                entityType,
                                                                nameConvertType))
                         .collect(Collectors.toList());
    }

    /**
     * 获取字段的数据库列名
     * <p>@ColumnSetting 设置列名</p>
     *
     * @param fieldName       字段名称
     * @param entityType      实体类型
     * @param nameConvertType 命名规则
     * @return 数据库列名
     */
    public static String getColumnByFieldName(String fieldName,
                                              Class<?> entityType,
                                              NameConvertType nameConvertType) {
        while (true) {
            for (Field field : entityType.getDeclaredFields()) {
                if (field.getName()
                         .equals(fieldName)) return getColumn(field,
                                                              nameConvertType);
            }

            //递归处理父类类型
            Class<?> superClazz = SchemaExtension.getSuperModelType(entityType);
            if (!superClazz.equals(entityType)) {
                entityType = superClazz;
                continue;
            }

            break;
        }

        return getColumn(fieldName,
                         nameConvertType);
    }

    /**
     * 获取表名
     * <p>@TableSetting 设置表名（可选，未设置时将使用类名）、模式名（可选）</p>
     *
     * @param entityType 实体类型
     * @return a: 模式名（可能为空）, b: 表名
     */
    public Tuple2<String, String> getTableName(Class<?> entityType) {
        return getTableName(entityType,
                            config.getNameConvertType());
    }

    /**
     * 获取表名
     * <p>@TableSetting 设置表名（可选，未设置时将使用类名）、模式名（可选）</p>
     *
     * @param entityType      实体类型
     * @param nameConvertType 命名规则
     * @return a: 模式名（可能为空）, b: 表名
     */
    public static Tuple2<String, String> getTableName(Class<?> entityType,
                                                      NameConvertType nameConvertType) {
        Tuple2<String, String> result = new Tuple2<>();
        TableSetting tableSettingAttribute = AnnotationUtils.findAnnotation(entityType,
                                                                            TableSetting.class);
        if (tableSettingAttribute != null) {
            result.a = tableSettingAttribute.schema();
            result.b = tableSettingAttribute.name();
        }

        if (!StringUtils.hasText(result.b)) result.b = SqlExtension.convertName(entityType.getSimpleName(),
                                                                                nameConvertType);
        return result;
    }

    /**
     * 获取字段
     *
     * @param column          列名
     * @param entityType      实体类型
     * @param nameConvertType 命名规则
     * @return 字段
     */
    public static String getFieldName(String column,
                                      Class<?> entityType,
                                      NameConvertType nameConvertType)
            throws
            ModuleException {
        return getFieldByColumn(column,
                                entityType,
                                nameConvertType).getName();
    }

    /**
     * 获取列对应的字段
     *
     * @param column          列名
     * @param entityType      实体类型
     * @param nameConvertType 命名规则
     * @return 字段
     */
    public static Field getFieldByColumn(String column,
                                         Class<?> entityType,
                                         NameConvertType nameConvertType) {
        Field result = null;

        while (true) {
            for (Field field : entityType.getDeclaredFields()) {
                ColumnSetting columnSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                      ColumnSetting.class);
                if (columnSettingAttribute != null && columnSettingAttribute.name()
                                                                            .equals(column)) return field;

                if (field.getName()
                         .equalsIgnoreCase(SqlExtension.reductionName(column,
                                                                      nameConvertType))) result = field;
            }

            //递归处理父类类型
            Class<?> superClazz = SchemaExtension.getSuperModelType(entityType);
            if (!superClazz.equals(entityType)) {
                entityType = superClazz;
                continue;
            }

            break;
        }

        if (result == null)
            throw new ModuleException(Strings.getEntityField4ColumnUndefined(entityType.getTypeName(),
                                                                             column));

        return result;
    }

    /**
     * 获取实体类型列集合
     *
     * @param entityType        实体类型
     * @param withOutPrimaryKey 排除主键
     * @param nameConvertType   命名规则
     * @return 列名集合
     */
    public static List<String> getColumnsByEntityType(Class<?> entityType,
                                                      boolean withOutPrimaryKey,
                                                      NameConvertType nameConvertType) {
        return getColumns(getColumnFieldsByEntityType(entityType,
                                                      withOutPrimaryKey),
                          nameConvertType);
    }

    /**
     * 获取实体类型列名集合
     *
     * @param entityType        实体类型
     * @param withOutPrimaryKey 排除主键
     * @return 列名集合
     */
    public static List<Field> getColumnFieldsByEntityType(Class<?> entityType,
                                                          boolean withOutPrimaryKey) {
        List<Field> fields = new ArrayList<>();
        while (true) {
//            EntityMapping entityMappingAttribute = AnnotationUtils.findAnnotation(entityType,
//                                                                                  EntityMapping.class);

            for (Field field : entityType.getDeclaredFields()) {
                if (isIgnoreColumn(field,
                                   withOutPrimaryKey)) continue;

                //必须是业务模型才会包括那些添加了实体映射设置的字段
//                if (entityMappingAttribute != null
//                        && field.isAnnotationPresent(EntityMappingSetting.class)) {
//                    //处理加了实体映射设置的字段
//                    Optional<Field> optionalField = getColumnField(field,
//                                                                   entityMappingAttribute,
//                                                                   withOutPrimaryKey);
//                    if (optionalField.isPresent()) {
//                        fields.add(optionalField.get());
//                        continue;
//                    }
//                }

                //默认保留没加任何注解的字段
                fields.add(field);
            }

            //递归处理父类类型
            Class<?> superClazz = SchemaExtension.getSuperModelType(entityType);
            if (!superClazz.equals(entityType)) {
                entityType = superClazz;
                continue;
            }

            break;
        }
        return fields;
    }

    /**
     * 获取业务模型列相关的字段
     *
     * @param dtoType           业务模型
     * @param mainTagLevel      主标签等级
     * @param customTags        自定义标签
     * @param withOutPrimaryKey 排除主键
     * @param inherit           仅继承成员
     * @param nameConvertType   命名规则
     * @return 列名集合
     */
    public static List<String> getColumnsByDtoType(Class<?> dtoType,
                                                   int mainTagLevel,
                                                   Collection<String> customTags,
                                                   boolean withOutPrimaryKey,
                                                   boolean inherit,
                                                   NameConvertType nameConvertType) {
        return getColumns(getColumnFieldsByDtoType(dtoType,
                                                   mainTagLevel,
                                                   customTags,
                                                   withOutPrimaryKey,
                                                   inherit),
                          nameConvertType);
    }

    /**
     * 获取业务模型列字段集合
     *
     * @param dtoType           业务模型
     * @param mainTagLevel      主标签等级
     * @param customTags        自定义标签
     * @param withOutPrimaryKey 排除主键
     * @param inherit           仅继承成员
     */
    public static List<Field> getColumnFieldsByDtoType(Class<?> dtoType,
                                                       int mainTagLevel,
                                                       Collection<String> customTags,
                                                       boolean withOutPrimaryKey,
                                                       boolean inherit) {
        List<Field> fields = new ArrayList<>();

        EntityMapping entityMappingAttribute = null;

        if (!inherit) {
            entityMappingAttribute = AnnotationUtils.findAnnotation(dtoType,
                                                                    EntityMapping.class);
            if (entityMappingAttribute != null) {
                //映射到实体的业务模型
                for (Field field : dtoType.getDeclaredFields()) {
                    getColumnField(field,
                                   entityMappingAttribute,
                                   withOutPrimaryKey).ifPresent(x -> {
                        //是否忽略列
                        if (!isIgnoreColumn(field,
                                            withOutPrimaryKey)) fields.add(x);
                    });
                }
            }
        }

        //如果指定了标签则获取继承类的信息
        if (dtoType.isAnnotationPresent(OpenApiMainTag.class) || dtoType.isAnnotationPresent(OpenApiMainTags.class)) {
            //继承到实体的业务模型
            List<String> tags = new ArrayList<>();
            String[] mainTags = SchemaExtension.getMainTag(dtoType,
                                                           mainTagLevel);
            if (CollectionsExtension.anyPlus(mainTags)) tags.addAll(Arrays.asList(mainTags));
            if (CollectionsExtension.anyPlus(customTags)) tags.addAll(customTags);

            List<Field> openApiFields = null;

            if (CollectionsExtension.anyPlus(tags)) {
                openApiFields = SchemaExtension.getFieldsWithTags(dtoType,
                                                                  true,
                                                                  false,
                                                                  tags);

                //过滤忽略的列
                openApiFields = openApiFields.stream()
                                             .filter(x -> !isIgnoreColumn(x,
                                                                          withOutPrimaryKey))
                                             .collect(Collectors.toList());
            } else if (entityMappingAttribute == null)
                //如果没有标签，则直接使用实体类的所有相关字段
                openApiFields = getColumnFieldsByEntityType(getEntityType(dtoType),
                                                            withOutPrimaryKey);

            if (CollectionsExtension.anyPlus(openApiFields)) fields.addAll(openApiFields);
        } else if (entityMappingAttribute == null) fields.addAll(getColumnFieldsByEntityType(getEntityType(dtoType),
                                                                                             withOutPrimaryKey));

        return fields;
    }

    /**
     * 获取列字段
     *
     * @param field                  字段
     * @param entityMappingAttribute 实体映射设置
     * @param withOutPrimaryKey      排除主键
     */
    public static Optional<Field> getColumnField(Field field,
                                                 EntityMapping entityMappingAttribute,
                                                 boolean withOutPrimaryKey) {
        if (isIgnoreField(field)) return Optional.empty();

        EntityMappingSetting entityMappingSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                            EntityMappingSetting.class);
        String entityFieldName = field.getName();
        Field entityField;

        if (entityMappingSettingAttribute != null) {
            //忽略字段或者忽略主键
            if (entityMappingSettingAttribute.ignore() || (withOutPrimaryKey
                    && entityMappingSettingAttribute.primaryKey())) return Optional.empty();

            //列名来自字段本身
            if (entityMappingSettingAttribute.self()) {
                return Optional.of(field);
            } else {
                Class<?> entityType = entityMappingSettingAttribute.entityType()
                                                                   .equals(Void.class)
                                      ? entityMappingAttribute.entityType()
                                      : entityMappingSettingAttribute.entityType();
                entityFieldName = StringUtils.hasText(entityMappingSettingAttribute.entityFieldName())
                                  ? entityMappingSettingAttribute.entityFieldName()
                                  : entityFieldName;

                try {
                    entityField = entityType.getDeclaredField(entityFieldName);
                } catch (NoSuchFieldException ex) {
                    throw new ModuleException(Strings.getEntityFieldUndefined(entityType.getTypeName(),
                                                                              entityFieldName),
                                              ex);
                }
            }
        } else {
            Class<?> entityType = entityMappingAttribute.entityType();
            try {
                entityField = entityType.getDeclaredField(entityFieldName);
            } catch (NoSuchFieldException ex) {
                throw new ModuleException(Strings.getEntityFieldUndefined(entityType.getTypeName(),
                                                                          entityFieldName),
                                          ex);
            }
        }

        return Optional.of(entityField);
    }

    /**
     * 是否为忽略的字段
     *
     * @param field 字段
     */
    public static boolean isIgnoreField(Field field) {
        return field.isAnnotationPresent(IgnoreEntityMapping.class);
    }

    /**
     * 是否为忽略的列
     *
     * @param field             字段
     * @param withOutPrimaryKey 排除主键
     */
    public static boolean isIgnoreColumn(Field field,
                                         boolean withOutPrimaryKey) {
        ColumnSetting columnSetting = AnnotationUtils.findAnnotation(field,
                                                                     ColumnSetting.class);
        return columnSetting != null && (columnSetting.ignore() || (withOutPrimaryKey && columnSetting.primaryKey()));
    }

    /**
     * 是否为主键
     *
     * @param field 字段
     */
    public static boolean isPrimaryKey(Field field) {
        ColumnSetting columnSetting = AnnotationUtils.findAnnotation(field,
                                                                     ColumnSetting.class);
        return columnSetting != null && columnSetting.primaryKey();
    }

    /**
     * 获取字段
     * <p>按以下先后顺序搜索字段</p>
     * <p>1、递归搜索模型类型和其基类的字段</p>
     * <p>2、搜索映射模型的字段</p>
     *
     * @param fieldName         字段名
     * @param modelType         模型类型
     * @param entityMappingType 映射模型类型
     */
    public static Field getFieldByFieldName(String fieldName,
                                            Class<?> modelType,
                                            Class<?> entityMappingType) {
        try {
            //搜索模型类型的字段
            return modelType.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ex1) {
            //搜索模型类型基类的字段
            Class<?> superClazz = SchemaExtension.getSuperModelType(modelType);
            if (!superClazz.equals(Object.class) && !superClazz.equals(modelType)) return getFieldByFieldName(fieldName,
                                                                                                              superClazz,
                                                                                                              entityMappingType);

            //搜索映射模型的字段
            if (entityMappingType != null) {
                Field mappingField;
                try {
                    mappingField = entityMappingType.getDeclaredField(fieldName);
                } catch (NoSuchFieldException ex2) {
                    throw new ModuleException(Strings.getEntityFieldUndefined(entityMappingType.getTypeName(),
                                                                              fieldName),
                                              ex2);
                }
                Optional<Field> optionalField = getColumnField(mappingField,
                                                               AnnotationUtils.findAnnotation(entityMappingType,
                                                                                              EntityMapping.class),
                                                               false);
                if (optionalField.isPresent()) return optionalField.get();
            }

            throw new ModuleException(Strings.getEntityFieldUndefined(modelType.getTypeName(),
                                                                      fieldName),
                                      ex1);
        }
    }

    /**
     * 获取键值对数据集中字段对应的值
     *
     * @param mapResult 键值对数据集
     * @param fieldName 字段名
     * @return 值
     */
    public Object getMapValueByFieldName(Map<String, Object> mapResult,
                                         String fieldName) {
        return mapResult.get(SqlExtension.convertName(fieldName,
                                                      config.getNameConvertType()));
    }

    /**
     * 获取键值对数据集中字段对应的值
     *
     * @param mapResult       键值对数据集
     * @param fieldName       字段名
     * @param nameConvertType 命名规则
     * @return 值
     */
    public static Object getMapValueByFieldName(Map<String, Object> mapResult,
                                                String fieldName,
                                                NameConvertType nameConvertType) {
        return mapResult.get(SqlExtension.convertName(fieldName,
                                                      nameConvertType));
    }

    /**
     * 获取列的Jdbc数据类型
     *
     * @param column          列名
     * @param entityType      实体类型
     * @param nameConvertType 命名规则
     * @param <T>             实体类型
     */
    public static <T> JdbcType getJdbcTypeByColumn(String column,
                                                   Class<T> entityType,
                                                   NameConvertType nameConvertType) {
        return getJdbcType(getFieldByColumn(column,
                                            entityType,
                                            nameConvertType));
    }

    /**
     * 获取字段的Jdbc数据类型
     *
     * @param fieldName  字段名
     * @param entityType 实体类型
     * @param <T>        实体类型
     */
    public static <T> JdbcType getJdbcType(String fieldName,
                                           Class<T> entityType) {
        return getJdbcType(getFieldByFieldName(fieldName,
                                               entityType,
                                               null));
    }

    /**
     * 获取字段的Jdbc数据类型
     * <p>请使用@MappingSetting注解精确设置此数据类型</p>
     * <p>@MappingSetting注解中未设置有效数据时会根据以下规则进行推导：</p>
     * <p>1、String类型的字段</p>
     * <p>1.1、@ColumnSetting注解中的数据长度</p>
     * <p>1.2、无法判断时返回JdbcType.NVARCHAR</p>
     * <hr>
     * <p>2、Date类型的字段</p>
     * <p>2.1、@OpenApiSchema注解中的format</p>
     * <p>2.2、@JsonFormat注解中的pattern</p>
     * <p>2.3、@JSONField注解中的format</p>
     * <p>2.4、无法判断时返回JdbcType.TIMESTAMP</p>
     * <hr>
     * <p>3、无法判断时会返回JdbcType.UNDEFINED</p>
     * <hr>
     *
     * @param field 字段
     */
    public static JdbcType getJdbcType(Field field) {
        MappingSetting mappingSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                MappingSetting.class);
        if (mappingSettingAttribute != null && !mappingSettingAttribute.jdbcType()
                                                                       .equals(JdbcType.UNDEFINED))
            return mappingSettingAttribute.jdbcType();

        ColumnSetting columnSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                              ColumnSetting.class);
        if (field.getType()
                 .equals(String.class)) {
            if (columnSettingAttribute == null) return JdbcType.VARCHAR;
            else if (columnSettingAttribute.length() == -1) return JdbcType.LONGNVARCHAR;
            else if (columnSettingAttribute.length() == -2) return JdbcType.LONGVARCHAR;
            else return JdbcType.VARCHAR;
        } else if (field.getType()
                        .equals(Character.class) || field.getType()
                                                         .equals(char.class)) return JdbcType.CHAR;
        else if (field.getType()
                      .equals(Boolean.class) || field.getType()
                                                     .equals(boolean.class)) return JdbcType.BIT;
        else if (field.getType()
                      .equals(Byte.class) || field.getType()
                                                  .equals(byte.class)) return JdbcType.TINYINT;
        else if (field.getType()
                      .equals(Short.class) || field.getType()
                                                   .equals(short.class)) return JdbcType.SMALLINT;
        else if (field.getType()
                      .equals(Integer.class) || field.getType()
                                                     .equals(int.class)) return JdbcType.INTEGER;
        else if (field.getType()
                      .equals(Long.class) || field.getType()
                                                  .equals(long.class)) return JdbcType.BIGINT;
        else if (field.getType()
                      .equals(Float.class) || field.getType()
                                                   .equals(float.class)) return JdbcType.FLOAT;
        else if (field.getType()
                      .equals(Double.class) || field.getType()
                                                    .equals(double.class)) return JdbcType.DOUBLE;
        else if (field.getType()
                      .equals(BigDecimal.class)) return JdbcType.DECIMAL;
        else if (field.getType()
                      .equals(java.sql.Date.class)) return JdbcType.DATE;
        else if (field.getType()
                      .equals(java.sql.Time.class)) return JdbcType.TIME;
        else if (field.getType()
                      .equals(Date.class)) {
            OpenApiSchema openApiSchemaAttribute = AnnotationUtils.findAnnotation(field,
                                                                                  OpenApiSchema.class);
            if (openApiSchemaAttribute != null) {
                switch (openApiSchemaAttribute.format()) {
                    case OpenApiSchemaFormat.string_datetime:
                    case OpenApiSchemaFormat.string_date_original:
                    case OpenApiSchemaFormat.string_timespan:
                        return JdbcType.TIMESTAMP;
                    case OpenApiSchemaFormat.string_date:
                        return JdbcType.DATE;
                    case OpenApiSchemaFormat.string_time:
                        return JdbcType.TIME;
                }
            }

            String format = null;
            JsonFormat jsonFormatAttribute = AnnotationUtils.findAnnotation(field,
                                                                            JsonFormat.class);
            if (jsonFormatAttribute != null) format = jsonFormatAttribute.pattern();
            if (!StringUtils.hasText(format)) {
                JSONField jsonFieldAttribute = AnnotationUtils.findAnnotation(field,
                                                                              JSONField.class);
                if (jsonFieldAttribute != null) format = jsonFieldAttribute.format();
            }
            if (StringUtils.hasText(format)) {
                if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d? \\d\\d?:\\d\\d?:\\d\\d?$",
                                    format)) return JdbcType.TIMESTAMP;
                else if (Pattern.matches("^\\d\\d\\d\\d[\\-/]\\d\\d?[\\-/]\\d\\d?$",
                                         format)) return JdbcType.DATE;
                else if (Pattern.matches("^\\d\\d?:\\d\\d?:\\d\\d?",
                                         format)) return JdbcType.TIME;
            }

            return JdbcType.TIMESTAMP;
        } else return JdbcType.UNDEFINED;
    }

    /**
     * 获取java数据类型对应的Jdbc数据类型
     *
     * @param type java数据类型
     * @return Jdbc数据类型
     */
    public static JdbcType getJdbcType(Class<?> type) {
        if (type.equals(String.class)) return JdbcType.VARCHAR;
        else if (type.equals(Character.class) || type.equals(char.class)) return JdbcType.CHAR;
        else if (type.equals(Boolean.class) || type.equals(boolean.class)) return JdbcType.BIT;
        else if (type.equals(Byte.class) || type.equals(byte.class)) return JdbcType.TINYINT;
        else if (type.equals(Short.class) || type.equals(short.class)) return JdbcType.SMALLINT;
        else if (type.equals(Integer.class) || type.equals(int.class)) return JdbcType.INTEGER;
        else if (type.equals(Long.class) || type.equals(long.class)) return JdbcType.BIGINT;
        else if (type.equals(Float.class) || type.equals(float.class)) return JdbcType.FLOAT;
        else if (type.equals(Double.class) || type.equals(double.class)) return JdbcType.DOUBLE;
        else if (type.equals(BigDecimal.class)) return JdbcType.DECIMAL;
        else if (type.equals(Date.class)) return JdbcType.TIMESTAMP;
        else if (type.equals(java.sql.Date.class)) return JdbcType.DATE;
        else if (type.equals(java.sql.Time.class)) return JdbcType.TIME;
        else return JdbcType.UNDEFINED;
    }

    /**
     * 获取实体类型
     *
     * @param type 类型
     */
    public static Class<?> getEntityType(Class<?> type) {
        if (type.getDeclaredAnnotation(TableSetting.class) != null) return type;

        Class<?> superClassz = type.getSuperclass();
        if (superClassz.equals(Object.class) || superClassz.equals(type))
            throw new ModuleException(Strings.getEntityUndefined(type.getTypeName()));
        return getEntityType(superClassz);
    }
}
