package project.extension.mybatis.edge.extention;

import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.core.annotation.AnnotationUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.annotations.MappingSetting;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.openapi.extention.SchemaExtension;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据映射表帮助类
 * <p>在字段上添加以下注解将会忽略该字段</p>
 * <p>1、@OpenApiIgnore</p>
 *
 * @author LCTR
 * @date 2022-04-03
 */
public class MapBuilderExtension {
    /**
     * 获取参数映射表
     *
     * @param config        配置
     * @param msId          标识
     * @param parameterType 参数类型
     * @param mainTagLevel  主标签等级
     * @param customTags    自定义标签
     * @param <T>           参数类型
     * @return 参数映射表
     */
    public static <T> ParameterMap getParameterMap(Configuration config,
                                                   String msId,
                                                   Class<T> parameterType,
                                                   int mainTagLevel,
                                                   Collection<String> customTags) {
        if (parameterType == null)
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
                                                           mainTagLevel);
            if (CollectionsExtension.anyPlus(mainTags))
                tags.addAll(Arrays.asList(mainTags));
            if (CollectionsExtension.anyPlus(customTags))
                tags.addAll(customTags);

            for (Field field : SchemaExtension.getFieldsWithTags(parameterType,
                                                                 tags)) {
                MappingSetting mappingSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                        MappingSetting.class);
                if (mappingSettingAttribute != null && mappingSettingAttribute.ignore())
                    continue;
                parameterMappings.add(new ParameterMapping.Builder(config,
                                                                   field.getName(),
                                                                   registry.getTypeHandler(field.getType())).build());
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
     * @param config           配置
     * @param msId             标识
     * @param parameterType    参数类型
     * @param parameterHashMap key为字段名的键值对集合
     * @param <T>              参数类型
     * @return 参数映射表
     */
    public static <T, V> ParameterMap getHashMapParameterMap(Configuration config,
                                                             String msId,
                                                             Class<T> parameterType,
                                                             Map<String, V> parameterHashMap)
            throws
            NoSuchFieldException {
        if (parameterType == null && !CollectionsExtension.anyPlus(parameterHashMap))
            return new ParameterMap.Builder(config,
                                            msId,
                                            Object.class,
                                            new ArrayList<>()).build();

        final TypeHandlerRegistry registry = config.getTypeHandlerRegistry();

        List<ParameterMapping> parameterMappings = new ArrayList<>();
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
                Field field = RepositoryExtension.getFieldByFieldName(fieldName,
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

        return new ParameterMap.Builder(config,
                                        msId,
                                        parameterHashMap.getClass(),
                                        parameterMappings).build();
    }

    /**
     * 获取返回数据映射表
     *
     * @param config            配置
     * @param msId              标识
     * @param resultType        返回值类型
     * @param mainTagLevel      主标签等级
     * @param customTags        自定义标签
     * @param withOutPrimaryKey 排除主键
     * @param nameConvertType   命名规则
     * @param <T>               返回值类型
     * @return 返回数据映射表
     */
    public static <T> ResultMap getResultMap(Configuration config,
                                             String msId,
                                             Class<T> resultType,
                                             int mainTagLevel,
                                             Collection<String> customTags,
                                             boolean withOutPrimaryKey,
                                             NameConvertType nameConvertType)
            throws
            NoSuchFieldException {
        if (resultType == null)
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
            List<Field> fields = RepositoryExtension.getColumnFieldsByDtoType(resultType,
                                                                              mainTagLevel,
                                                                              customTags,
                                                                              withOutPrimaryKey,
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
                String column = RepositoryExtension.getColumn(field,
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
     * @param resultHashMap   key为字段名的键值对集合
     * @param nameConvertType 命名规则
     * @param <T>             返回数据类型
     * @return 返回数据映射表
     */
    public static <T, V> ResultMap getHashMapResultMap(Configuration config,
                                                       String msId,
                                                       Class<T> resultType,
                                                       Map<String, V> resultHashMap,
                                                       NameConvertType nameConvertType)
            throws
            NoSuchFieldException {
        if (resultType == null)
            return new ResultMap.Builder(config,
                                         msId,
                                         Object.class,
                                         new ArrayList<>()).build();

        final TypeHandlerRegistry registry = config.getTypeHandlerRegistry();

        List<ResultMapping> resultMappings = new ArrayList<>();

        for (String fieldName : resultHashMap.keySet()) {
            Field field = RepositoryExtension.getFieldByFieldName(fieldName,
                                                                  resultType,
                                                                  null);
            MappingSetting mappingSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                    MappingSetting.class);
            if (mappingSettingAttribute != null && mappingSettingAttribute.ignore())
                continue;
            String column = RepositoryExtension.getColumn(field,
                                                          nameConvertType);
            resultMappings.add(new ResultMapping.Builder(config,
                                                         fieldName,
                                                         column,
                                                         registry.getTypeHandler(field.getType())).build());
        }

        return new ResultMap.Builder(config,
                                     msId,
                                     resultHashMap.getClass(),
                                     resultMappings).build();
    }
}
