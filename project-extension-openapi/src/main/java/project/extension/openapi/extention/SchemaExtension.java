package project.extension.openapi.extention;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import com.alibaba.fastjson.JSONObject;
import project.extension.openapi.model.FieldValueChanged;
import project.extension.openapi.model.OpenApiSchemaFormat;
import project.extension.openapi.model.OpenApiSchemaType;
import project.extension.openapi.model.SchemaInfo;
import project.extension.tuple.Tuple2;
import project.extension.openapi.annotations.*;
import project.extension.tuple.Tuple3;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 架构相关拓展方法
 *
 * @author LCTR
 * @date 2022-03-18
 */
public class SchemaExtension {
    /**
     * 获取父类类型
     *
     * @param type 类型
     * @return 类型的父类类型，如果没有则返回它自己
     */
    public static Class<?> getSuperModelType(Class<?> type) {
//        //返回数组类型的元素类型
//        if (type.isArray()) return getSuperModelType(type.getComponentType());
//
        //获取继承类
        Class<?> superClazz = type.getSuperclass();
//        if (superClazz == null || superClazz.equals(Object.class)) return type;

        //是否标记为父类类型
        if (superClazz != null && !superClazz.equals(Object.class)) return superClazz;
//        if (superClazz != null && !superClazz.equals(Object.class)
//                && superClazz.getAnnotation(OpenApiSuperModel.class) != null) return superClazz;

        return type;
    }

    /**
     * 获取泛型集合元素类型
     *
     * @param clazz 类型
     * @param type  类型
     * @return 元素类型
     */
    public static Class<?> getGenericType(Class<?> clazz,
                                          Type type) {
        //返回数组类型的元素类型
        if (clazz.isArray()) return getGenericType(clazz.getComponentType());

        //返回泛型类型
        if (type instanceof ParameterizedType && Iterable.class.isAssignableFrom(clazz)) {
            return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }

        return clazz;
    }

    /**
     * 获取数组元素类型
     *
     * @param type 类型
     * @return 元素类型
     */
    public static Class<?> getGenericType(Class<?> type) {
        //返回数组类型的元素类型
        if (type.isArray()) return getGenericType(type.getComponentType());

        return null;
    }

    /**
     * 获取数组和泛型集合元素类型
     *
     * @param clazz 类型
     * @param type  泛型类型
     * @return a: 是否为数组或泛型集合,b: 元素类型
     */
    public static Tuple2<Boolean, Class<?>> tryGetCollectionItemType(Class<?> clazz,
                                                                     Type type) {
        Boolean flag = clazz.isArray() || (type instanceof ParameterizedType && Iterable.class.isAssignableFrom(clazz));
        return new Tuple2<>(flag,
                            flag
                            ? getGenericType(clazz,
                                             type)
                            : null);
    }

    /**
     * 转换类型
     *
     * @param value        数据
     * @param defaultValue 默认值
     * @param type         目标数据类型
     * @param <T>          目标数据类型
     * @return 已转为目标类型的数据，失败时为默认值
     */
    public static <T> Tuple2<Boolean, T> tryCast(Object value,
                                                 T defaultValue,
                                                 Class<T> type) {
        try {
            return new Tuple2<>(true,
                                JSONObject.parseObject(JSONObject.toJSONString(value),
                                                       type));
        } catch (Exception ex) {
            return new Tuple2<>(false,
                                defaultValue);
        }
    }

    /**
     * 过滤模型
     *
     * @param clazz 类型
     * @param after 处理成员之后
     */
    public static void filterModel(Class<?> clazz,
                                   Consumer<Tuple3<Class<?>, Field, Class<?>>> after) {
        filterModel(clazz,
                    after,
                    null,
                    false,
                    true);
    }

    /**
     * 过滤模型
     *
     * @param clazz       类型
     * @param after       处理成员之后
     * @param before      处理成员之前(返回false,跳过该成员)
     * @param innerModel  处理内部模型
     * @param getMainTags 自动获取类上的主标签
     * @param otherTags   其他标签集合
     */
    public static void filterModel(Class<?> clazz,
                                   Consumer<Tuple3<Class<?>, Field, Class<?>>> after,
                                   Function<Tuple3<Class<?>, Field, Class<?>>, Boolean> before,
                                   boolean innerModel,
                                   boolean getMainTags,
                                   String... otherTags) {
        filterModel(clazz,
                    after,
                    before,
                    innerModel,
                    getMainTags,
                    otherTags == null
                    ? null
                    : Arrays.stream(otherTags)
                            .collect(Collectors.toList()),
                    false);
    }

    /**
     * 过滤模型
     *
     * @param clazz       类型
     * @param after       处理成员之后
     * @param before      处理成员之前(返回false,跳过该成员)
     * @param innerModel  处理内部模型
     * @param getMainTags 自动获取类上的主标签
     * @param otherTags   其他标签集合
     * @param superModel  父类
     */
    public static void filterModel(Class<?> clazz,
                                   Consumer<Tuple3<Class<?>, Field, Class<?>>> after,
                                   Function<Tuple3<Class<?>, Field, Class<?>>, Boolean> before,
                                   boolean innerModel,
                                   boolean getMainTags,
                                   Collection<String> otherTags,
                                   boolean superModel) {
        //所有用于匹配附属标签的标签
        List<String> tags = null;
        //取主标签和其他标签的并集
        String[] mainTags = getMainTags
                            ? getMainTag(clazz)
                            : null;
        if (!CollectionsExtension.anyPlus(mainTags)) {
            if (CollectionsExtension.anyPlus(otherTags)) tags = new ArrayList<>(otherTags);
        } else if (CollectionsExtension.anyPlus(otherTags))
            tags = CollectionsExtension.union(Arrays.stream(mainTags)
                                                    .collect(Collectors.toList()),
                                              otherTags);
        else tags = Arrays.stream(mainTags)
                          .collect(Collectors.toList());

        //是否有标签
        boolean hasTag = CollectionsExtension.anyPlus(tags);
        if (hasTag)
            tags = tags.stream()
                       .distinct()
                       .collect(Collectors.toList());

        //严格模式
        boolean strictMode = clazz.getAnnotation(OpenApiSchemaStrictMode.class) != null;

        //获取所有字段
//        //先排序
//        Collection<Field> declaredFields = sort(Arrays.stream(clazz.getDeclaredFields())
//                                                      .collect(Collectors.toList()));

        //再过滤
        for (Field field : clazz.getDeclaredFields()) {
            //过滤前执行
            if (before != null && !before.apply(new Tuple3<>(clazz,
                                                             field,
                                                             clazz))) continue;

            OpenApiSchema schemaAttribute = AnnotationUtils.findAnnotation(field,
                                                                           OpenApiSchema.class);

            //附属标签是否匹配
            if (hasTag && !tags.contains("*") && !hasTag(field,
                                                         tags)) {
                if (superModel || (!strictMode && !field.getDeclaringClass()
                                                        .getTypeName()
                                                        .equals(
                                                                clazz.getTypeName())) || (strictMode
                        && schemaAttribute == null))
                    continue;
            }

            //是否忽略
            if (field.getAnnotation(OpenApiIgnore.class) != null) continue;

            //递归处理字段的泛型类型
            if (innerModel && schemaAttribute != null && schemaAttribute.type()
                                                                        .equals(OpenApiSchemaType.model))
                filterModel(getGenericType(field.getType(),
                                           field.getGenericType()),
                            after,
                            before,
                            !schemaAttribute.format()
                                            .equals(OpenApiSchemaFormat.model_once),
                            getMainTags,
                            null,
                            superModel);

            //过滤后执行
            if (after != null) after.accept(new Tuple3<>(clazz,
                                                         field,
                                                         clazz));
        }

        //递归处理泛型类型
        Class<?> genericClazz = getGenericType(clazz);
        if (genericClazz != null && !genericClazz.equals(clazz))
            filterModel(genericClazz,
                        after,
                        before,
                        innerModel,
                        getMainTags,
                        tags,
                        superModel);

        //递归处理父类类型
        Class<?> superClazz = getSuperModelType(clazz);
        if (!superClazz.equals(clazz))
            filterModel(superClazz,
                        after == null
                        ? null
                        : x -> after.accept(new Tuple3<>(x.a,
                                                         x.b,
                                                         clazz)),
                        before == null
                        ? null
                        : x -> before.apply(new Tuple3<>(x.a,
                                                         x.b,
                                                         clazz)),
                        innerModel,
                        false,
                        tags,
                        true);
    }

    /**
     * 获取排序后的字段集合
     *
     * @param fields 待排序的字段集合
     * @return 排序后的字段集合
     */
    public static Collection<Field> sort(Collection<Field> fields) {
        List<Tuple2<Field, Integer>> declaredFieldWithSort = new ArrayList<>();

        int i = 0;

        for (Field field : fields) {
            OpenApiSchema schemaAttribute = AnnotationUtils.findAnnotation(field,
                                                                           OpenApiSchema.class);
            declaredFieldWithSort.add(new Tuple2<>(field,
                                                   schemaAttribute == null
                                                   ? Integer.MAX_VALUE - fields.size() + i
                                                   : schemaAttribute.sort()));
            i++;
        }

        declaredFieldWithSort.sort(Comparator.comparing(x -> x.b));

        return declaredFieldWithSort.stream()
                                    .map(x -> x.a)
                                    .collect(Collectors.toList());
    }

    /**
     * 获取主标签名称
     *
     * @param type 类型
     * @return 主标签
     */
    public static String[] getMainTag(Class<?> type) {
        return getMainTag(type,
                          0);
    }

    /**
     * 获取主标签名称
     *
     * @param type  类型
     * @param level 等级
     * @return 主标签
     */
    public static String[] getMainTag(Class<?> type,
                                      int level) {
        OpenApiMainTags openApiMainTagsAttribute = AnnotationUtils.findAnnotation(type,
                                                                                  OpenApiMainTags.class);
        OpenApiMainTag openApiMainTagAttribute = null;
        if (openApiMainTagsAttribute != null) {
            for (OpenApiMainTag item : openApiMainTagsAttribute.value()) {
                if (item.level() == level) {
                    openApiMainTagAttribute = item;
                    break;
                }
            }
        } else openApiMainTagAttribute = AnnotationUtils.findAnnotation(type,
                                                                        OpenApiMainTag.class);

        return openApiMainTagAttribute == null
               ? null
               : openApiMainTagAttribute.names();
    }

    /**
     * 是否包含架构注解
     *
     * @param field 目标成员
     */
    public static boolean hasSchemaAttribute(Field field) {
        return field.getAnnotation(OpenApiSchema.class) != null;
    }

    /**
     * 是否包含标签
     *
     * @param field 目标成员
     * @param names 标签名称
     */
    public static boolean hasTag(Field field,
                                 String... names) {
        OpenApiSubTag attribute = AnnotationUtils.findAnnotation(field,
                                                                 OpenApiSubTag.class);
        return attribute != null && CollectionsExtension.anyPlus(attribute.names(),
                                                                 x -> Arrays.asList(names)
                                                                            .contains(x));
    }

    /**
     * 是否包含标签
     *
     * @param field 目标成员
     * @param names 标签名称
     */
    public static boolean hasTag(Field field,
                                 List<String> names) {
        OpenApiSubTag attribute = AnnotationUtils.findAnnotation(field,
                                                                 OpenApiSubTag.class);
        return attribute != null && CollectionsExtension.anyPlus(attribute.names(),
                                                                 x -> names.stream()
                                                                           .anyMatch(x::equals));
    }

    /**
     * 根据主标签和其他标签获取字段集合
     *
     * @param type      类型
     * @param inherit   仅继承成员
     * @param otherTags 需要额外匹配的标签
     * @return 字段集合
     */
    public static List<Field> getFieldsWithMainTagAndOtherTags(Class<?> type,
                                                               boolean inherit,
                                                               String... otherTags) {
        return getFieldsWithTags(type,
                                 inherit,
                                 true,
                                 new ArrayList<>(Arrays.asList(otherTags)));
    }

    /**
     * 根据主标签获取字段集合
     *
     * @param type 类型
     * @return 字段集合
     */
    public static List<Field> getFieldsWithMainTag(Class<?> type) {
        return getFieldsWithMainTag(type,
                                    false);
    }

    /**
     * 根据主标签获取字段集合
     *
     * @param type    类型
     * @param inherit 仅继承成员
     * @return 字段集合
     */
    public static List<Field> getFieldsWithMainTag(Class<?> type,
                                                   boolean inherit) {
        String[] mainTags = getMainTag(type);
        return getFieldsWithTags(type,
                                 inherit,
                                 false,
                                 new ArrayList<>(Arrays.asList(mainTags)));
    }

    /**
     * 获取属性集合
     * <p>严格模式：输出添加了OpenApiSchema注解得字段</p>
     * <p>非严格模式：输出所有字段</p>
     *
     * @param type 类型
     * @return 字段集合
     */
    public static List<Field> getFields(Class<?> type) {
        return getFieldsWithTags(type,
                                 false,
                                 false,
                                 null);
    }

    /**
     * 获取属性集合
     * <p>严格模式：输出添加了OpenApiSchema注解得字段</p>
     * <p>非严格模式：输出所有字段</p>
     *
     * @param type    类型
     * @param inherit 仅继承成员
     * @return 字段集合
     */
    public static List<Field> getFields(Class<?> type,
                                        boolean inherit) {
        return getFieldsWithTags(type,
                                 inherit,
                                 false,
                                 null);
    }

    /**
     * 获取属性集合
     * <p>严格模式：输出添加了OpenApiSchema注解得字段</p>
     * <p>非严格模式：输出所有字段</p>
     *
     * @param type         类型
     * @param inherit      仅继承成员
     * @param mainTagLevel 主标签等级
     * @param customTags   自定义标签
     * @return 字段集合
     */
    public static List<Field> getFields(Class<?> type,
                                        boolean inherit,
                                        int mainTagLevel,
                                        Collection<String> customTags) {
        List<String> tags = new ArrayList<>();
        String[] mainTags = SchemaExtension.getMainTag(type,
                                                       mainTagLevel);
        if (CollectionsExtension.anyPlus(mainTags)) tags.addAll(Arrays.asList(mainTags));
        if (CollectionsExtension.anyPlus(customTags)) tags.addAll(customTags);

        return getFieldsWithTags(type,
                                 inherit,
                                 false,
                                 tags);
    }

    /**
     * 根据标签获取字段集合
     *
     * @param type 类型
     * @param tags 标签集合
     * @return 字段集合
     */
    public static List<Field> getFieldsWithTags(Class<?> type,
                                                String... tags) {
        return getFieldsWithTags(type,
                                 false,
                                 false,
                                 new ArrayList<>(Arrays.asList(tags)));
    }

    /**
     * 根据标签获取字段集合
     *
     * @param type 类型
     * @param tags 标签集合
     * @return 字段集合
     */
    public static List<Field> getFieldsWithTags(Class<?> type,
                                                Collection<String> tags) {
        return getFieldsWithTags(type,
                                 false,
                                 false,
                                 tags);
    }

    /**
     * 根据标签获取属性集合
     *
     * @param type        类型
     * @param inherit     仅继承成员
     * @param getMainTags 自动获取类上的主标签
     * @param tags        需要匹配的标签集合
     * @return 字段集合
     */
    public static List<Field> getFieldsWithTags(Class<?> type,
                                                boolean inherit,
                                                boolean getMainTags,
                                                Collection<String> tags) {
        List<Field> result = new ArrayList<>();

        filterModel(type,
                    tuple2 -> {
                        if (inherit && tuple2.a.equals(tuple2.c)) return;
                        result.add(tuple2.b);
                    },
                    null,
                    false,
                    getMainTags,
                    tags,
                    false);

        return new ArrayList<>(sort(result));
    }

    /**
     * 获取字段名称集合
     *
     * @param type 类型
     * @return 字段名称集合
     */
    public static List<String> getFieldsName(Class<?> type) {
        return getFieldsNameWithTags(type,
                                     false);
    }

    /**
     * 获取字段名集合
     *
     * @param type 类型
     * @param tag  标签
     * @return 字段名集合
     */
    public static List<String> getFieldsNameWithTag(Class<?> type,
                                                    String tag) {
        return getFieldsNameWithTag(type,
                                    tag,
                                    false);
    }

    /**
     * 获取字段名集合
     *
     * @param type    类型
     * @param tag     标签
     * @param inherit 仅继承成员
     * @return 字段名集合
     */
    public static List<String> getFieldsNameWithTag(Class<?> type,
                                                    String tag,
                                                    boolean inherit) {
        return getFieldsWithTags(type,
                                 inherit,
                                 false,
                                 Collections.singleton(tag)).stream()
                                                            .map(Field::getName)
                                                            .collect(
                                                                    Collectors.toList());
    }

    /**
     * 获取字段名称集合
     *
     * @param type 类型
     * @param tags 标签集合
     * @return 字段名称集合
     */
    public static List<String> getFieldsNameWithTags(Class<?> type,
                                                     String... tags) {
        return getFieldsNameWithTags(type,
                                     false,
                                     tags);
    }

    /**
     * 获取字段名称集合
     *
     * @param type    类型
     * @param inherit 仅继承成员
     * @param tags    标签集合
     * @return 字段名称集合
     */
    public static List<String> getFieldsNameWithTags(Class<?> type,
                                                     boolean inherit,
                                                     String... tags) {
        return getFieldsWithTags(type,
                                 inherit,
                                 false,
                                 new ArrayList<>(Arrays.asList(tags))).stream()
                                                                      .map(
                                                                              Field::getName)
                                                                      .collect(Collectors.toList());
    }

    /**
     * 获取字段名称集合
     *
     * @param type      类型
     * @param inherit   仅继承成员
     * @param otherTags 需要额外匹配的标签集合
     * @return 字段名称集合
     */
    public static List<String> getFieldsNameWithMainTagAndOtherTags(Class<?> type,
                                                                    boolean inherit,
                                                                    String... otherTags) {
        return getFieldsWithMainTagAndOtherTags(type,
                                                inherit,
                                                otherTags).stream()
                                                          .map(Field::getName)
                                                          .collect(
                                                                  Collectors.toList());
    }

    /**
     * 根据主标签创建键值对集合
     *
     * @param obj              对象
     * @param <TOpenApiSchema> 架构类型
     * @return 键值对集合
     */
    public static <TOpenApiSchema> Hashtable<String, Object> getNameAndValueDicWithMainTag(TOpenApiSchema obj)
            throws
            IllegalAccessException {
        return getNameAndValueDicWithMainTag(obj,
                                             false);
    }

    /**
     * 根据主标签创建键值对集合
     *
     * @param obj              对象
     * @param inherit          仅继承成员
     * @param <TOpenApiSchema> 架构类型
     * @return 键值对集合
     */
    public static <TOpenApiSchema> Hashtable<String, Object> getNameAndValueDicWithMainTag(TOpenApiSchema obj,
                                                                                           boolean inherit)
            throws
            IllegalAccessException {
        Hashtable<String, Object> dic = new Hashtable<>();
        List<Field> fields = getFieldsWithMainTag(obj.getClass(),
                                                  inherit);
        for (Field field : fields) {
            dic.put(field.getName(),
                    field.get(obj));
        }
        return dic;
    }

    /**
     * 获取或创建架构属性信息
     *
     * @param type 类型
     * @return 架构属性信息
     */
    public static Hashtable<String, List<String>> getOrNullForPropertyDic(Class<?> type) {
        return getOrNullForPropertyDic(type,
                                       false,
                                       null,
                                       null);
    }

    /**
     * 获取或创建架构属性信息
     *
     * @param type       类型
     * @param innerModel 处理内部模型
     * @return 架构属性信息
     */
    public static Hashtable<String, List<String>> getOrNullForPropertyDic(Class<?> type,
                                                                          boolean innerModel) {
        return getOrNullForPropertyDic(type,
                                       innerModel,
                                       null,
                                       null);
    }

    /**
     * 获取或创建架构属性信息
     * <p>如果在特别输出参数和特别忽略参数中同时指定了同一个属性，那么最终不会输出该属性</p>
     *
     * @param type            类型
     * @param innerModel      处理内部模型
     * @param exceptionFields 特别输出的字段
     * @param ignoreFields    特别忽略的字段
     * @return 架构属性信息
     */
    public static Hashtable<String, List<String>> getOrNullForPropertyDic(Class<?> type,
                                                                          boolean innerModel,
                                                                          Hashtable<String, List<String>> exceptionFields,
                                                                          Hashtable<String, List<String>> ignoreFields) {
        //读取缓存
        Hashtable<String, List<String>> fieldNameDic = CacheExtension.getFieldsOfTypeDic(type,
                                                                                         CollectionsExtension.anyPlus(
                                                                                                 exceptionFields)
                                                                                                 && CollectionsExtension.anyPlus(ignoreFields));

        Hashtable<String, List<Field>> fieldDic = new Hashtable<>();

        if (!CollectionsExtension.anyPlus(fieldDic)) {
            //过滤模型
            filterModel(type,
                        tuple3 -> {
                            String typeName = tuple3.c.getTypeName();
                            if (!fieldDic.containsKey(typeName))
                                fieldDic.put(typeName,
                                             new ArrayList<>(Arrays.asList(tuple3.b)));
                            else
                                fieldDic.get(typeName)
                                        .add(tuple3.b);

                            //类型对应命名空间添加至缓存
                            if (CacheExtension.assemblyOfTypeCache.exist(typeName)) {
                                String packageName = tuple3.a.getPackage()
                                                             .getName();
                                CacheExtension.assemblyOfTypeCache.addOrUpdate(typeName,
                                                                               packageName);
                            }
                        },
                        null,
                        innerModel,
                        true);

            //获取排序后的值
            fieldDic.forEach(
                    (k, v) -> fieldNameDic.put(k,
                                               sort(v).stream()
                                                      .map(Field::getName)
                                                      .collect(Collectors.toList())));

            //类型属性集合添加至缓存
            CacheExtension.setFieldsOfTypeDic(type,
                                              fieldNameDic);
        }

        //添加特别指定的字段
        if (exceptionFields != null) for (String key : exceptionFields.keySet()) {
            if (!fieldNameDic.containsKey(key)) fieldNameDic.put(key,
                                                                 exceptionFields.get(key));
            else fieldNameDic.put(key,
                                  CollectionsExtension.union(fieldNameDic.get(key),
                                                             exceptionFields.get(key)));
        }

        //过滤忽略的字段
        if (ignoreFields != null) for (String key : ignoreFields.keySet()) {
            if (fieldNameDic.containsKey(key))
                fieldNameDic.put(key,
                                 CollectionsExtension.except(fieldNameDic.get(key),
                                                             exceptionFields.get(key)));
        }

        return fieldNameDic;
    }

    /**
     * 获取数据更改信息
     *
     * @param former           以前的数据
     * @param current          当前的数据
     * @param <TOpenApiSchema> 比对数据的类型
     * @return 字段值变更信息集合
     */
    public static <TOpenApiSchema> List<FieldValueChanged> getPropertyValueChangeds(TOpenApiSchema former,
                                                                                    TOpenApiSchema current) {
        Class<?> classz = former.getClass(), superClassz;

        //主标签
        String[] mainTags = getMainTag(classz);
        Stream<String> mainTagsStream = Arrays.stream(mainTags);

        //返回数据
        List<FieldValueChanged> result = new ArrayList<>();

        while (true) {
            result.addAll(Arrays.stream(classz.getDeclaredFields())
                                //筛选匹配标签的字段
                                .filter(f -> mainTagsStream.anyMatch(t -> hasTag(f,
                                                                                 t)))
                                //创建字段值变更信息
                                .map(f -> {
                                    f.setAccessible(true);
                                    FieldValueChanged fieldValueChanged = new FieldValueChanged();
                                    fieldValueChanged.setDeclaringClass(f.getDeclaringClass());

                                    fieldValueChanged.setName(f.getName());
                                    try {
                                        fieldValueChanged.setFormerValue(f.get(former));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        fieldValueChanged.setCurrentValue(f.get(current));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }

                                    OpenApiDescription descriptionAttribute = AnnotationUtils.findAnnotation(f,
                                                                                                             OpenApiDescription.class);
                                    if (descriptionAttribute != null)
                                        fieldValueChanged.setDescription(descriptionAttribute.value());

                                    return fieldValueChanged;
                                })
                                //过滤未变更的数据
                                .filter(x -> {
                                    if (x.getFormerValue() == null) return x.getCurrentValue() != null;

                                    return x.getFormerValue()
                                            .equals(x.getCurrentValue());
                                })
                                .collect(Collectors.toList()));

            //获取并处理父类类型
            superClassz = getSuperModelType(classz);
            if (superClassz.equals(classz)) break;
            else classz = superClassz;
        }

        return result;
    }

    /**
     * 获取默认结构信息
     *
     * @param clazz 类型
     */
    public static SchemaInfo getSchemaInfo(Class<?> clazz) {
        SchemaInfo schemaInfo = new SchemaInfo(OpenApiSchemaType.string,
                                               "",
                                               false,
                                               null);

        if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
            schemaInfo.setType(OpenApiSchemaType.boolean_);
        } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
            schemaInfo.setType(OpenApiSchemaType.integer);
            schemaInfo.setFormat(OpenApiSchemaFormat.integer_byte);
        } else if (Short.class.equals(clazz) || short.class.equals(clazz) || Integer.class.equals(
                clazz) || int.class.equals(clazz)) {
            schemaInfo.setType(OpenApiSchemaType.integer);
            schemaInfo.setFormat(OpenApiSchemaFormat.integer_int32);
        } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
            schemaInfo.setType(OpenApiSchemaType.integer);
            schemaInfo.setFormat(OpenApiSchemaFormat.integer_int64);
        } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
            schemaInfo.setType(OpenApiSchemaType.number);
            schemaInfo.setFormat(OpenApiSchemaFormat.number_float);
        } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
            schemaInfo.setType(OpenApiSchemaType.number);
            schemaInfo.setFormat(OpenApiSchemaFormat.number_double);
        } else if (BigDecimal.class.equals(clazz)) {
            schemaInfo.setType(OpenApiSchemaType.number);
            schemaInfo.setFormat(OpenApiSchemaFormat.number_decimal);
        } else if (Date.class.equals(clazz)) {
            schemaInfo.setType(OpenApiSchemaType.string);
            schemaInfo.setFormat(OpenApiSchemaFormat.string_datetime);
        }

        return schemaInfo;
    }

    /**
     * 获取结构信息
     *
     * @param field 字段
     */
    public static SchemaInfo getSchemaInfo(Field field) {
        OpenApiSchema schemaAttribute = AnnotationUtils.findAnnotation(field,
                                                                       OpenApiSchema.class);
        if (schemaAttribute == null)
            return getSchemaInfo(field.getType());

        return new SchemaInfo(schemaAttribute.type(),
                              schemaAttribute.format(),
                              StringUtils.hasText(schemaAttribute.valueExample()),
                              schemaAttribute.valueExample());
    }
}
