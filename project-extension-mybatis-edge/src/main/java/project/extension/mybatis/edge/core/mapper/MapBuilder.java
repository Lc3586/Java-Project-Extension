package project.extension.mybatis.edge.core.mapper;

import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.annotations.ColumnSetting;
import project.extension.mybatis.edge.annotations.MappingSetting;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.openapi.extention.SchemaExtension;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据映射构建器
 * <p>在字段上添加以下注解将会忽略该字段</p>
 * <p>1、@OpenApiIgnore</p>
 *
 * @author LCTR
 * @date 2022-04-03
 */
public class MapBuilder {
    /**
     * 获取参数映射表
     *
     * @param config        配置
     * @param msId          标识
     * @param parameterType 参数类型
     * @param mainTagLevel  主标签等级（null时默认为0）
     * @param customTags    自定义标签
     * @param <TParameter>  参数类型
     * @return 参数映射表
     */
    public static <TParameter> ParameterMap getParameterMap(Configuration config,
                                                            String msId,
                                                            Class<TParameter> parameterType,
                                                            @Nullable
                                                                    Integer mainTagLevel,
                                                            @Nullable
                                                                    Collection<String> customTags) {
        if (parameterType == null || parameterType.equals(Object.class))
            return new ParameterMap.Builder(config,
                                            msId,
                                            Object.class,
                                            new ArrayList<>()).build();

        final TypeHandlerRegistry registry = config.getTypeHandlerRegistry();

        List<ParameterMapping> parameterMappings = new ArrayList<>();

        if (!parameterType.getTypeName()
                          .startsWith("java.lang.")) {
            //获取标签
            List<String> tags = new ArrayList<>();
            String[] mainTags = SchemaExtension.getMainTag(parameterType,
                                                           mainTagLevel == null
                                                           ? 0
                                                           : mainTagLevel);
            if (CollectionsExtension.anyPlus(mainTags))
                tags.addAll(Arrays.asList(mainTags));
            if (CollectionsExtension.anyPlus(customTags))
                tags.addAll(customTags);

            for (Field field : SchemaExtension.getFieldsWithTags(parameterType,
                                                                 tags)) {
                MappingSetting mappingSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                        MappingSetting.class);

                ColumnSetting columnSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                      ColumnSetting.class);

                if (mappingSettingAttribute != null
                        && mappingSettingAttribute.ignore())
                    continue;

                ParameterMapping.Builder parameterMapping = new ParameterMapping.Builder(config,
                                                                                         field.getName(),
                                                                                         registry.getTypeHandler(field.getType()));

                //自增主键
                if (columnSettingAttribute != null && columnSettingAttribute.isIdentity())
                    parameterMapping.mode(ParameterMode.OUT);

                parameterMappings.add(parameterMapping.build());
            }
        }

        return new ParameterMap.Builder(config,
                                        msId,
                                        parameterType,
                                        parameterMappings).build();
    }

    /**
     * 获取参数映射表
     *
     * @param config                配置
     * @param msId                  标识
     * @param parameterType         参数类型
     * @param parameterHashMap      全部参数（key为字段名的键值对集合）
     * @param outParameterHashMap   输出参数（key为字段名的键值对集合）
     * @param inOutParameterHashMap 输入输出参数（key为字段名的键值对集合）
     * @param <TParameter>          参数类型
     * @return 参数映射表
     */
    public static <TParameter> ParameterMap getHashMapParameterMap(Configuration config,
                                                                   String msId,
                                                                   Class<TParameter> parameterType,
                                                                   Map<String, Object> parameterHashMap,
                                                                   Map<String, Class<?>> outParameterHashMap,
                                                                   Map<String, Class<?>> inOutParameterHashMap) {
        final TypeHandlerRegistry registry = config.getTypeHandlerRegistry();

        List<ParameterMapping> parameterMappings = new ArrayList<>();

        if ((parameterType == null || !parameterType.equals(Object.class))
                && CollectionsExtension.anyPlus(parameterHashMap)) {
            if (parameterType == null)
                for (String parameterName : parameterHashMap.keySet()) {
                    parameterMappings.add(new ParameterMapping.Builder(config,
                                                                       parameterName,
                                                                       registry.getTypeHandler(
                                                                               parameterHashMap.get(parameterName)
                                                                                               .getClass())).build());
                }
            else
                for (String fieldName : parameterHashMap.keySet()) {
                    Field field = EntityTypeHandler.getFieldByFieldName(fieldName,
                                                                        parameterType,
                                                                        null);
                    MappingSetting mappingSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                            MappingSetting.class);
                    if (mappingSettingAttribute != null && mappingSettingAttribute.ignore())
                        continue;
                    parameterMappings.add(new ParameterMapping.Builder(config,
                                                                       fieldName,
                                                                       registry.getTypeHandler(field.getType())).build());
                }
        }

        if (CollectionsExtension.anyPlus(outParameterHashMap))
            for (String parameterName : outParameterHashMap.keySet()) {
                ParameterMapping.Builder parameterMapping = new ParameterMapping.Builder(config,
                                                                                         parameterName,
                                                                                         registry.getTypeHandler(
                                                                                                 outParameterHashMap.get(parameterName)));
                parameterMapping.mode(ParameterMode.OUT);
                parameterMapping.javaType(outParameterHashMap.get(parameterName));
                parameterMappings.add(parameterMapping.build());

                if (!parameterHashMap.containsKey(parameterName)) {
                    parameterHashMap.put(parameterName,
                                         null);
                }
            }

        if (CollectionsExtension.anyPlus(inOutParameterHashMap))
            for (String parameterName : inOutParameterHashMap.keySet()) {
                ParameterMapping.Builder parameterMapping = new ParameterMapping.Builder(config,
                                                                                         parameterName,
                                                                                         registry.getTypeHandler(
                                                                                                 inOutParameterHashMap.get(parameterName)));
                parameterMapping.mode(ParameterMode.INOUT);
                parameterMapping.javaType(outParameterHashMap.get(parameterName));
                parameterMappings.add(parameterMapping.build());

                if (!parameterHashMap.containsKey(parameterName)) {
                    parameterHashMap.put(parameterName,
                                         null);
                }
            }

        return new ParameterMap.Builder(config,
                                        msId,
                                        Map.class,
                                        parameterMappings).build();
    }

    /**
     * 获取返回数据映射表
     *
     * @param config             配置
     * @param msId               标识
     * @param resultType         返回值类型
     * @param mainTagLevel       主标签等级（null时默认为0）
     * @param customTags         自定义标签
     * @param withOutPrimaryKey  排除主键
     * @param withOutIdentityKey 排除自增列
     * @param nameConvertType    命名规则
     * @param <TResult>          返回值类型
     * @return 返回数据映射表
     */
    public static <TResult> ResultMap getResultMap(Configuration config,
                                                   String msId,
                                                   Class<TResult> resultType,
                                                   @Nullable
                                                           Integer mainTagLevel,
                                                   Collection<String> customTags,
                                                   boolean withOutPrimaryKey,
                                                   boolean withOutIdentityKey,
                                                   NameConvertType nameConvertType) {
        if (resultType == null || resultType.equals(Object.class))
            return new ResultMap.Builder(config,
                                         msId,
                                         Object.class,
                                         new ArrayList<>()).build();

        final TypeHandlerRegistry registry = config.getTypeHandlerRegistry();

        List<ResultMapping> resultMappings = new ArrayList<>();

        if (!resultType.getTypeName()
                       .startsWith("java.lang.")
                && !resultType.getTypeName()
                              .startsWith("java.util.")) {
            List<Field> fields = EntityTypeHandler.getColumnFieldsByDtoType(resultType,
                                                                            mainTagLevel == null
                                                                            ? 0
                                                                            : mainTagLevel,
                                                                            customTags,
                                                                            withOutPrimaryKey,
                                                                            withOutIdentityKey,
                                                                            false);
            for (Field field : fields) {
                TypeHandler<?> typeHandler = null;
                MappingSetting mappingSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                        MappingSetting.class);
                if (mappingSettingAttribute != null) {
                    if (mappingSettingAttribute.ignore())
                        continue;

                    if (mappingSettingAttribute.typeHandler() != Void.class)
                        typeHandler = registry.getTypeHandler(mappingSettingAttribute.typeHandler());
                    else if (mappingSettingAttribute.jdbcType() != JdbcType.UNDEFINED)
                        typeHandler = registry.getTypeHandler(mappingSettingAttribute.jdbcType());
                }

                if (typeHandler == null)
                    typeHandler = registry.getTypeHandler(field.getType());

                String fieldName = field.getName();
                String column = EntityTypeHandler.getColumn(field,
                                                            nameConvertType);
                resultMappings.add(new ResultMapping.Builder(config,
                                                             fieldName,
                                                             column,
                                                             typeHandler).build());
            }
        }

        return new ResultMap.Builder(config,
                                     msId,
                                     resultType,
                                     resultMappings).build();
    }

    /**
     * 获取返回数据映射表
     *
     * @param config          配置
     * @param msId            标识
     * @param resultType      返回数据类型
     * @param resultFields    返回数据字段集合
     * @param nameConvertType 命名规则
     * @param <TResult>       返回数据类型
     * @return 返回数据映射表
     */
    public static <TResult> ResultMap getHashMapResultMap(Configuration config,
                                                          String msId,
                                                          Class<TResult> resultType,
                                                          Collection<String> resultFields,
                                                          NameConvertType nameConvertType) {
        final TypeHandlerRegistry registry = config.getTypeHandlerRegistry();

        List<ResultMapping> resultMappings = new ArrayList<>();

        if (resultType != null
                && !resultType.equals(Object.class)
                && CollectionsExtension.anyPlus(resultFields))
            for (String fieldName : resultFields) {
                Field field = EntityTypeHandler.getFieldByFieldName(fieldName,
                                                                    resultType,
                                                                    null);
                MappingSetting mappingSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                        MappingSetting.class);
                if (mappingSettingAttribute != null && mappingSettingAttribute.ignore())
                    continue;
                String column = EntityTypeHandler.getColumn(field,
                                                            nameConvertType);
                resultMappings.add(new ResultMapping.Builder(config,
                                                             fieldName,
                                                             column,
                                                             registry.getTypeHandler(field.getType())).build());
            }

        return new ResultMap.Builder(config,
                                     msId,
                                     Map.class,
                                     resultMappings).build();
    }
}
