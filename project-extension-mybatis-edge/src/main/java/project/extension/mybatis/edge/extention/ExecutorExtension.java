package project.extension.mybatis.edge.extention;

import org.apache.ibatis.binding.MapperMethod;
import org.springframework.core.annotation.AnnotationUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.annotations.ExecutorSetting;
import project.extension.mybatis.edge.model.DynamicSqlSetting;
import project.extension.mybatis.edge.model.ExecutorParameter;
import project.extension.openapi.extention.SchemaExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询设置相关拓展方法
 *
 * @author LCTR
 * @date 2022-03-25
 * @deprecated 请使用RepositoryProvider来操作数据库
 */
@Deprecated
public class ExecutorExtension {
//    /**
//     * 获取参数集合
//     * <p>支持以下配置方式（可同时使用）：</p>
//     * <p>1、首先在类上添加注解, @Pagination</p>
//     * <p>2、设置动态过滤条件，相关字段上添加注解, @Pagination(PaginationParameter.动态过滤条件)</p>
//     * <p>3、设置自定义过滤SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义过滤SQL)</p>
//     * <p>4、设置高级排序，相关字段上添加注解, @Pagination(PaginationParameter.高级排序)</p>
//     * <p>5、设置自定义排序SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义排序SQL)</p>
//     * <p>6、设置自定义SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义SQL)</p>
//     *
//     * @param type          类型
//     * @param parameterType 参数类型
//     * @param dbType        数据库类型
//     * @param alias         数据表别名
//     * @param otherTags     其他标签
//     * @return 参数集合
//     */
//    public static Map<String, String> getParameters(Class<?> type, Class<?> parameterType, DbType dbType, String alias, String... otherTags) {
//        Map<String, String> paramters = new HashMap<>();
//
//        Class<?> classz = parameterType;
//
//        while (true) {
//            if (classz.getAnnotation(ExecutorSetting.class) != null) {
//                for (Field field : classz.getDeclaredFields()) {
//                    ExecutorSetting executorSettingAttribute = AnnotationUtils.findAnnotation(field, ExecutorSetting.class);
//                    if (executorSettingAttribute == null) continue;
//
//                    if (executorSettingAttribute.parameter().equals(ExecutorParameter.分页设置))
//                        paramters.put(ExecutorParameter.分页设置, getParameterString(ExecutorParameter.分页设置, field.getName(), dbType.toString()));
//                    else if (executorSettingAttribute.parameter().equals(ExecutorParameter.动态过滤条件))
//                        paramters.put(ExecutorParameter.动态过滤条件, getParameterString(ExecutorParameter.动态过滤条件, field.getName(), dbType.toString(), alias));
//                    else if (executorSettingAttribute.parameter().equals(ExecutorParameter.动态排序条件))
//                        paramters.put(ExecutorParameter.动态排序条件, getParameterString(ExecutorParameter.动态排序条件, field.getName(), dbType.toString(), alias));
//                    else if (executorSettingAttribute.parameter().equals(ExecutorParameter.自定义过滤SQL))
//                        paramters.put(ExecutorParameter.自定义过滤SQL, getParameterString(ExecutorParameter.自定义过滤SQL, field.getName()));
//                    else if (executorSettingAttribute.parameter().equals(ExecutorParameter.自定义排序SQL))
//                        paramters.put(ExecutorParameter.自定义排序SQL, getParameterString(ExecutorParameter.自定义排序SQL, field.getName()));
//                    else if (executorSettingAttribute.parameter().equals(ExecutorParameter.自定义SQL))
//                        paramters.put(ExecutorParameter.自定义SQL, getParameterString(ExecutorParameter.自定义SQL, field.getName(), dbType.toString(), alias, type.getTypeName()));
//                    else if (executorSettingAttribute.parameter().equals(ExecutorParameter.其他标签))
//                        paramters.put(ExecutorParameter.其他标签, getParameterString(ExecutorParameter.其他标签, field.getName(), dbType.toString(), alias, type.getTypeName(), otherTags == null ? "" : String.join(",", otherTags)));
//                }
//            }
//
//            Class<?> superClassz = SchemaExtention.getSuperModelType(classz);
//            if (superClassz == null || superClassz.equals(Object.class) || superClassz.equals(classz)) break;
//
//            classz = superClassz;
//        }
//
//        return paramters;
//    }

    /**
     * 获取指定的参数字段集合
     * <p>支持以下配置方式（可同时使用）：</p>
     * <p>1、首先在类上添加注解, @Pagination</p>
     * <p>2、设置动态过滤条件，相关字段上添加注解, @Pagination(PaginationParameter.动态过滤条件)</p>
     * <p>3、设置自定义过滤SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义过滤SQL)</p>
     * <p>4、设置高级排序，相关字段上添加注解, @Pagination(PaginationParameter.高级排序)</p>
     * <p>5、设置自定义排序SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义排序SQL)</p>
     * <p>6、设置自定义SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义SQL)</p>
     *
     * @param parameterType      参数类型
     * @param executorParameters 参数类型
     * @return 参数集合，键：参数类型，值：字段名
     */
    public static Map<String, Field> getParameters(Class<?> parameterType,
                                                   String... executorParameters) {
        Map<String, Field> paramters = new HashMap<>();
        Class<?> classz = parameterType;

        while (true) {
            if (classz.getAnnotation(ExecutorSetting.class) != null) {
                for (Field field : classz.getDeclaredFields()) {
                    ExecutorSetting executorSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                              ExecutorSetting.class);
                    if (executorSettingAttribute == null) continue;

                    for (String executorParameter : executorParameters) {
                        if (executorSettingAttribute.parameter()
                                                    .equals(executorParameter))
                            paramters.put(executorParameter,
                                          field);
                    }
                }
            }

            Class<?> superClassz = SchemaExtension.getSuperModelType(classz);
            if (superClassz.equals(Object.class) || superClassz.equals(classz)) break;

            classz = superClassz;
        }

        return paramters;
    }

    /**
     * 获取指定的参数值集合
     * <p>支持以下配置方式（可同时使用）：</p>
     * <p>1、首先在类上添加注解, @Pagination</p>
     * <p>2、设置动态过滤条件，相关字段上添加注解, @Pagination(PaginationParameter.动态过滤条件)</p>
     * <p>3、设置自定义过滤SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义过滤SQL)</p>
     * <p>4、设置高级排序，相关字段上添加注解, @Pagination(PaginationParameter.高级排序)</p>
     * <p>5、设置自定义排序SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义排序SQL)</p>
     * <p>6、设置自定义SQL，相关字段上添加注解, @Pagination(PaginationParameter.自定义SQL)</p>
     *
     * @param parameter          参数
     * @param executorParameters 参数类型
     * @return 参数集合，键：参数类型，值：字段名
     */
    public static Map<String, Object> getParameterValues(Object parameter,
                                                         String... executorParameters)
            throws
            IllegalAccessException {
        Map<String, Object> paramterValues = new HashMap<>();

        Map<String, Field> paramters = getParameters(parameter.getClass(),
                                                     executorParameters);
        for (String key : paramters.keySet()) {
            Field field = paramters.get(key);
            field.setAccessible(true);

            Object value = field.get(parameter);
            if (value != null)
                paramterValues.put(key,
                                   value);
        }

        return paramterValues;
    }

    /**
     * 获取参数字符串
     *
     * @param executorParameter 参数
     * @param values            值集合
     * @return 参数字符串
     */
    public static String getParameterString(String executorParameter,
                                            String... values) {
        if (values == null || values.length == 0)
            return String.format("￥{%s}",
                                 executorParameter);
        else
            return String.format("￥{%s&%s}",
                                 executorParameter,
                                 String.join("&",
                                             values));
    }

    /**
     * 获取动态Sql设置
     *
     * @param parameter 参数
     * @return 配置
     */
    public static DynamicSqlSetting getDynamicSetting(Object parameter)
            throws
            IllegalAccessException {
        DynamicSqlSetting setting = null;
        if (parameter.getClass()
                     .equals(MapperMethod.ParamMap.class)) {
            //多参数
            MapperMethod.ParamMap<Object> parameterMap = (MapperMethod.ParamMap<Object>) parameter;
            setting = (DynamicSqlSetting) parameterMap.get("arg0");
        } else {
            Map<String, Object> dynamicSqlSettingValues = ExecutorExtension.getParameterValues(
                    parameter,
                    ExecutorParameter.返回值类型,
                    ExecutorParameter.主键类型,
                    ExecutorParameter.数据库类型);
            setting = new DynamicSqlSetting(
                    (Class<?>) CollectionsExtension.tryGet(dynamicSqlSettingValues,
                                                           ExecutorParameter.实体类型,
                                                           Integer.class).b,
                    (Class<?>) CollectionsExtension.tryGet(dynamicSqlSettingValues,
                                                           ExecutorParameter.返回值类型,
                                                           Object.class).b);
        }
        return setting;
    }

    /**
     * 获取参数
     *
     * @param parameter
     * @return
     */
    public static List<Object> getExecutorParameters(Object parameter) {
        List<Object> executorParameters = new ArrayList<>();
        if (parameter.getClass()
                     .equals(MapperMethod.ParamMap.class)) {
            //多参数
            MapperMethod.ParamMap<Object> parameterMap = (MapperMethod.ParamMap<Object>) parameter;
            for (int i = 0; true; i++) {
                String name = String.format("arg%s",
                                            i);
                if (parameterMap.containsKey(name)) {
                    Object item = parameterMap.get(name);
                    if (!item.getClass()
                             .equals(DynamicSqlSetting.class))
                        executorParameters.add(item);
                } else
                    break;
            }
        } else
            executorParameters.add(parameter);

        return executorParameters;
    }
}
