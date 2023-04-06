package project.extension.openapi.extention;

import io.swagger.v3.oas.models.examples.Example;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.cryptography.MD5Utils;
import project.extension.object.ObjectExtension;
import project.extension.openapi.annotations.*;
import project.extension.openapi.model.*;
import project.extension.tuple.Tuple2;
import project.extension.type.TypeExtension;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 接口文档相关扩展方法
 *
 * @author LCTR
 * @date 2022-03-21
 */
public class OpenApiExtension {
    /**
     * 获取或创建接口说明类
     *
     * @param clazz          类型
     * @param genericTypeMap 泛型类型
     * @param dtoTypeMap     业务模型类型
     * @return 说明类
     */
    public static IOpenApiAny getOrNullFor(Class<?> clazz,
                                           Map<Class<?>, Map<Integer, Map<String, Class<?>>>> genericTypeMap,
                                           Map<Class<?>, Tuple2<Integer, String[]>> dtoTypeMap)
            throws
            Exception {
        return getOrNullFor(clazz,
                            genericTypeMap,
                            dtoTypeMap,
                            true);
    }

    /**
     * 获取或创建接口说明类
     *
     * @param clazz          类型
     * @param genericTypeMap 泛型类型
     * @param dtoTypeMap     业务模型类型
     * @param innerModel     处理内部模型
     * @return 说明类
     */
    public static IOpenApiAny getOrNullFor(Class<?> clazz,
                                           Map<Class<?>, Map<Integer, Map<String, Class<?>>>> genericTypeMap,
                                           Map<Class<?>, Tuple2<Integer, String[]>> dtoTypeMap,
                                           boolean innerModel)
            throws
            Exception {
        //生成缓存Key
        StringBuilder cacheKeySB = new StringBuilder(clazz.getTypeName());
        for (Class<?> type : genericTypeMap.keySet()) {
            cacheKeySB.append(type.getTypeName());
            for (Integer level : genericTypeMap.get(type)
                                               .keySet()) {
                cacheKeySB.append(level);
                for (String name : genericTypeMap.get(type)
                                                 .get(level)
                                                 .keySet()) {
                    cacheKeySB.append(name);
                    cacheKeySB.append(genericTypeMap.get(type)
                                                    .get(level)
                                                    .get(name)
                                                    .getTypeName());
                }
            }
        }
        String cacheKey = MD5Utils.hash(cacheKeySB.toString());

        //检查是否存在缓存
        if (CacheExtension.openApiObjectCache.exist(cacheKey)) return CacheExtension.openApiObjectCache.get(cacheKey);

        //创建
        IOpenApiAny any = createFor(clazz,
                                    null,
                                    genericTypeMap,
                                    dtoTypeMap,
                                    new ArrayList<>(),
                                    innerModel,
                                    0);

        //更新缓存
        CacheExtension.openApiObjectCache.addOrUpdate(cacheKey,
                                                      any);

        return any;
    }

    /**
     * 获取类型的泛型参数
     *
     * @param clazz          类型
     * @param type           泛型类型
     * @param genericTypeMap 泛型参数集合
     * @param level          层级
     */
    private static void getParameterizedTypeArguments(Class<?> clazz,
                                                      ParameterizedType type,
                                                      Map<Class<?>, Map<Integer, Map<String, Class<?>>>> genericTypeMap,
                                                      int level) {
        TypeVariable<?>[] fieldTypeParameters = clazz.getTypeParameters();
        Type[] fieldTypes = type.getActualTypeArguments();

        for (int i = 0; i < fieldTypes.length; i++) {
            TypeVariable<?> fieldTypeParameter = fieldTypeParameters[i];
            Type fieldType = fieldTypes[i];

            if (fieldType instanceof ParameterizedType) {
                //1、字段的类型参数又是泛型类型
                ParameterizedType fieldParameterizedType = (ParameterizedType) fieldType;
                getParameterizedTypeArguments((Class<?>) fieldParameterizedType.getRawType(),
                                              fieldParameterizedType,
                                              genericTypeMap,
                                              ++level);
            } else if (!(fieldType instanceof TypeVariable)) {
                //2、添加字段的泛型参数类型
                if (!genericTypeMap.containsKey(clazz))
                    genericTypeMap.put(clazz,
                                       new HashMap<>());
                if (!genericTypeMap.get(clazz)
                                   .containsKey(level))
                    genericTypeMap.get(clazz)
                                  .put(level,
                                       new HashMap<>());
                genericTypeMap.get(clazz)
                              .get(level)
                              .put(fieldTypeParameter.getName(),
                                   (Class<?>) fieldType);
            }
            //3、当前类型的泛型参数类型
        }
    }

    /**
     * 获取字段擦除类型原本的类型
     *
     * @param clazz          类的类型
     * @param field          字段
     * @param genericTypeMap 泛型参数集合
     * @param level          层级
     * @return 如果是擦除类型则返回原本的类型
     */
    private static Class<?> getGenericType(Class<?> clazz,
                                           Field field,
                                           Map<Class<?>, Map<Integer, Map<String, Class<?>>>> genericTypeMap,
                                           int level)
            throws
            Exception {
        String signature = (String) TypeExtension.getGenericSignatureMethod()
                                                 .invoke(field);
        if (signature != null) {
            //字段为擦除类型，尝试获取对应的泛型类型
            Matcher matcher = Pattern.compile("^T(.*?);$",
                                              Pattern.CASE_INSENSITIVE)
                                     .matcher(
                                             signature);
            if (matcher.find() && StringUtils.hasText(matcher.group(1))) {
                String name = matcher.group(1);
                if (genericTypeMap.containsKey(clazz) && genericTypeMap.get(clazz)
                                                                       .containsKey(level)
                        && genericTypeMap.get(clazz)
                                         .get(level)
                                         .containsKey(name))
                    return genericTypeMap.get(clazz)
                                         .get(level)
                                         .get(name);
            }
        }
        return field.getType();
    }

    /**
     * 创建说明类
     *
     * @param clazz          类的类型
     * @param schemaInfo     结构信息
     * @param genericTypeMap 泛型参数集合
     * @param dtoTypeMap     业务模型类型集合
     * @param childClazz     存储所有的子类型，防止递归时死循环
     * @param innerModel     处理内部模型
     * @param level          层级
     * @return 说明类
     */
    private static IOpenApiAny createFor(Class<?> clazz,
                                         SchemaInfo schemaInfo,
                                         Map<Class<?>, Map<Integer, Map<String, Class<?>>>> genericTypeMap,
                                         Map<Class<?>, Tuple2<Integer, String[]>> dtoTypeMap,
                                         List<Class<?>> childClazz,
                                         boolean innerModel,
                                         int level)
            throws
            Exception {
        if (TypeExtension.isLangType(clazz)
                || clazz.equals(Date.class)
                || clazz.equals(BigDecimal.class)) {
            //基础类型
            return createFor(clazz,
                             schemaInfo);
        } else if (clazz.isArray() || Iterable.class.isAssignableFrom(clazz)) {
            //不处理内部模型
            if (!innerModel)
                return new OpenApiArray(new OpenApiObject());

            //集合类型
            if (genericTypeMap.size() > 0) {
                Map<Integer, Map<String, Class<?>>> type1 = CollectionsExtension.firstValue(genericTypeMap);
                assert type1 != null;
                if (type1.containsKey(level) && type1.get(level)
                                                     .size() > 0) {
                    Class<?> type2 = CollectionsExtension.firstValue(type1.get(level));
                    assert type2 != null;
                    return new OpenApiArray(createFor(type2,
                                                      null,
                                                      genericTypeMap,
                                                      dtoTypeMap,
                                                      childClazz,
                                                      true,
                                                      ++level));
                }
            }

            //如果找不到集合元素的类型，则默认使用Object类型
            return new OpenApiArray(createFor(Object.class,
                                              null,
                                              new HashMap<>(),
                                              dtoTypeMap,
                                              childClazz,
                                              true,
                                              ++level));
        } else if (Map.class.isAssignableFrom(clazz)) {
            //映射集合
            return new OpenApiObject("键值对集合");
        } else {
            //其他类型
            OpenApiObject object = new OpenApiObject();

            //不处理内部模型
            if (!innerModel)
                return object;

            //判断是否为树状结构，防止递归死循环
            if (childClazz.contains(clazz))
                innerModel = false;
            else
                childClazz.add(clazz);

            //对象的所有字段
            List<Field> fields = dtoTypeMap.containsKey(clazz)
                                 ? SchemaExtension.getFields(clazz,
                                                             false,
                                                             dtoTypeMap.get(clazz).a,
                                                             Arrays.asList(dtoTypeMap.get(clazz).b))
                                 : clazz.isAnnotationPresent(OpenApiMainTag.class)
                                           || clazz.isAnnotationPresent(OpenApiMainTags.class)
                                           || clazz.isAnnotationPresent(OpenApiSchemaStrictMode.class)
                                   ? SchemaExtension.getFields(clazz,
                                                               false,
                                                               0,
                                                               null)
                                   : SchemaExtension.getFields(clazz,
                                                               false);

            for (Field field : fields) {
                Class<?> fieldClazz = field.getType();
                Type fieldType = field.getGenericType();

                if (fieldType instanceof ParameterizedType) {
                    //字段为泛型类型，则需要分析并获取泛型参数的类型
                    getParameterizedTypeArguments(fieldClazz,
                                                  (ParameterizedType) fieldType,
                                                  genericTypeMap,
                                                  level);
                }

//                String signature = (String) getGenericSignatureMethod().invoke(field);
                if (fieldClazz.equals(Object.class)) {
                    //字段可能是泛型类型
                    fieldClazz = getGenericType(clazz,
                                                field,
                                                genericTypeMap,
                                                level);
                }

                //创建字段的说明类
                IOpenApiAny property;

                property = createFor(fieldClazz,
                                     SchemaExtension.getSchemaInfo(field),
                                     genericTypeMap,
                                     dtoTypeMap,
                                     childClazz,
                                     innerModel,
                                     level);

                //注释
                OpenApiDescription descriptionAttribute = AnnotationUtils.findAnnotation(field,
                                                                                         OpenApiDescription.class);
                if (descriptionAttribute != null) property.addDescription(descriptionAttribute.value());

                object.put(field.getName(),
                           property);
            }

            return object;
        }
    }

    /**
     * 创建接口初始类
     *
     * @param clazz      类型
     * @param schemaInfo 架构信息
     */
    public static IOpenApiAny createFor(Class<?> clazz,
                                        SchemaInfo schemaInfo) {
        if (schemaInfo == null) {
            //获取默认架构信息
            schemaInfo = SchemaExtension.getSchemaInfo(clazz);
        }

        IOpenApiAny any;
        switch (schemaInfo.getType()) {
            case OpenApiSchemaType.boolean_:
                if (schemaInfo.isSpecialValue()) {
                    Tuple2<Boolean, Boolean> cast_boolean = ObjectExtension.tryCast(schemaInfo.getValue(),
                                                                                    OpenApiSchemaValueExample.boolean_,
                                                                                    Boolean.class);
                    any = new OpenApiBoolean(cast_boolean.b);
                } else any = new OpenApiBoolean(OpenApiSchemaValueExample.boolean_);
                break;
            case OpenApiSchemaType.integer:
                switch (schemaInfo.getFormat()) {
                    case OpenApiSchemaFormat.integer_byte:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Byte> cast_byte = ObjectExtension.tryCast(schemaInfo.getValue(),
                                                                                      OpenApiSchemaValueExample.byte_,
                                                                                      Byte.class);
                            any = new OpenApiByte(cast_byte.b);
                        } else any = new OpenApiByte(OpenApiSchemaValueExample.byte_);
                        break;
                    case OpenApiSchemaFormat.integer_int32:
                    default:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Integer> cast_int = ObjectExtension.tryCast(schemaInfo.getValue(),
                                                                                        OpenApiSchemaValueExample.int_,
                                                                                        Integer.class);
                            any = new OpenApiInteger(cast_int.b);
                        } else any = new OpenApiInteger(OpenApiSchemaValueExample.int_);
                        break;
                    case OpenApiSchemaFormat.integer_int64:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Long> cast_long = ObjectExtension.tryCast(schemaInfo.getValue(),
                                                                                      OpenApiSchemaValueExample.long_,
                                                                                      Long.class);
                            any = new OpenApiLong(cast_long.b);
                        } else any = new OpenApiLong(OpenApiSchemaValueExample.long_);
                        break;
                }
                break;
            case OpenApiSchemaType.number:
                switch (schemaInfo.getFormat()) {
                    case OpenApiSchemaFormat.number_float:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Float> cast_float = ObjectExtension.tryCast(schemaInfo.getValue(),
                                                                                        OpenApiSchemaValueExample.float_,
                                                                                        Float.class);
                            any = new OpenApiFloat(cast_float.b);
                        } else any = new OpenApiFloat(OpenApiSchemaValueExample.float_);
                        break;
                    case OpenApiSchemaFormat.number_decimal:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, BigDecimal> cast_decimal = ObjectExtension.tryCast(
                                    schemaInfo.getValue(),
                                    OpenApiSchemaValueExample.decimal,
                                    BigDecimal.class);
                            any = new OpenApiDecimal(cast_decimal.b);
                        } else any = new OpenApiDecimal(OpenApiSchemaValueExample.decimal);
                        break;
                    case OpenApiSchemaFormat.number_double:
                    default:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Double> cast_double = ObjectExtension.tryCast(
                                    schemaInfo.getValue(),
                                    OpenApiSchemaValueExample.double_,
                                    Double.class);
                            any = new OpenApiDouble(cast_double.b);
                        } else any = new OpenApiDouble(OpenApiSchemaValueExample.double_);
                        break;
                }
                break;
            case OpenApiSchemaType.enum_:
                any = getEnumOpenApiAny(clazz,
                                        schemaInfo.isSpecialValue()
                                        ? schemaInfo.getValue()
                                        : null);
                break;
            case OpenApiSchemaType.string:
            default:
                switch (schemaInfo.getFormat()) {
                    case OpenApiSchemaFormat.string_date_original:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Date> cast_date_original = ObjectExtension.tryCast(
                                    schemaInfo.getValue(),
                                    OpenApiSchemaValueExample.date_original,
                                    Date.class);
                            any = new OpenApiDate(cast_date_original.b);
                        } else any = new OpenApiDate(OpenApiSchemaValueExample.date_original);
                        break;
                    case OpenApiSchemaFormat.string_datetime:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Date> cast_datetime = ObjectExtension.tryCast(
                                    schemaInfo.getValue(),
                                    OpenApiSchemaValueExample.date_original,
                                    Date.class);
                            any = new OpenApiString(
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cast_datetime.b));
                        } else any = new OpenApiString(OpenApiSchemaValueExample.datetime);
                        break;
                    case OpenApiSchemaFormat.string_date:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Date> cast_date = ObjectExtension.tryCast(schemaInfo.getValue(),
                                                                                      OpenApiSchemaValueExample.date_original,
                                                                                      Date.class);
                            any = new OpenApiString(new SimpleDateFormat("yyyy-MM-dd").format(cast_date.b));
                        } else any = new OpenApiString(OpenApiSchemaValueExample.date);
                        break;
                    case OpenApiSchemaFormat.string_time:
                        if (schemaInfo.isSpecialValue()) {
                            Tuple2<Boolean, Date> cast_time = ObjectExtension.tryCast(schemaInfo.getValue(),
                                                                                      OpenApiSchemaValueExample.date_original,
                                                                                      Date.class);
                            any = new OpenApiString(new SimpleDateFormat("HH:mm:ss").format(cast_time.b));
                        } else any = new OpenApiString(OpenApiSchemaValueExample.datetime);
                        break;
                    case OpenApiSchemaFormat.string_timespan:
                        any = new OpenApiString(
                                schemaInfo.isSpecialValue()
                                ? schemaInfo.getValue()
                                : OpenApiSchemaValueExample.timespan);
                        break;
                    default:
                        if (schemaInfo.isSpecialValue()) {
                            any = new OpenApiString(schemaInfo.getValue());
                        } else any = new OpenApiString(OpenApiSchemaValueExample.string);
                        break;
                }
                break;
        }

        return any;
    }

    /**
     * 获取枚举的描述信息
     *
     * @param type 枚举类型
     * @return 描述信息
     */
    public static IOpenApiAny getEnumOpenApiAny(Class<
            ?> type) {
        return getEnumOpenApiAny(type,
                                 null);
    }

    /**
     * 获取枚举的描述信息
     *
     * @param clazz        枚举类型
     * @param defaultValue 默认值
     * @return 描述信息
     */
    public static IOpenApiAny getEnumOpenApiAny(Class<?> clazz,
                                                String defaultValue) {
        Map<String, String> nameAndDescriptionDic;
        String typeName = clazz.getTypeName();
        if (CacheExtension.enumNameAndDescriptionCache.exist(typeName))
            nameAndDescriptionDic = CacheExtension.enumNameAndDescriptionCache.get(typeName);
        else {
            nameAndDescriptionDic = new HashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                OpenApiDescription descriptionAttribute = AnnotationUtils.findAnnotation(field,
                                                                                         OpenApiDescription.class);
                if (descriptionAttribute == null) continue;
                String description = String.format("%s (%s)",
                                                   field.getName(),
                                                   descriptionAttribute.value());
                nameAndDescriptionDic.put(field.getName(),
                                          description);
            }
            CacheExtension.enumNameAndDescriptionCache.addOrUpdate(typeName,
                                                                   nameAndDescriptionDic);
        }

        String value = defaultValue == null
                       ? nameAndDescriptionDic.keySet()
                                              .iterator()
                                              .next()
                       : defaultValue;

        List<String> descriptions = new ArrayList<>();
        nameAndDescriptionDic.forEach(
                (name, description) -> descriptions.add(String.format("%s : %s",
                                                                      name,
                                                                      description)));
        IOpenApiAny any = new OpenApiString(value);
        any.addDescription(String.format("值说明: %s",
                                         String.join(",",
                                                     descriptions)));
        return any;
    }

    /**
     * 获取示例
     *
     * @param models       多个模型
     * @param defaultClazz 模型中未指定类型时使用的类型
     * @return 示例
     */
    public static List<Example> getExample(OpenApiModels models,
                                           Class<?> defaultClazz)
            throws
            Exception {
        return getExample(models,
                          defaultClazz,
                          null);
    }

    /**
     * 获取示例
     *
     * @param models       多个模型
     * @param defaultClazz 模型中未指定类型时使用的类型
     * @param defaultType  默认示例类型（将会忽略model注解中的设置）
     * @return 示例
     */
    public static List<Example> getExample(OpenApiModels models,
                                           Class<?> defaultClazz,
                                           ExampleType defaultType)
            throws
            Exception {
        List<Example> examples = new ArrayList<>();
        for (OpenApiModel model : models.value()) {
            examples.add(
                    getExample(model.type()
                                    .equals(Void.class)
                               ? models.defaultType()
                                       .equals(Void.class)
                                 ? defaultClazz
                                 : models.defaultType()
                               : model.type(),
                               model.genericTypes().length == 0
                               ? models.defaultGenericTypes()
                               : model.genericTypes(),
                               model.dtoTypes().length == 0
                               ? models.defaultDtoTypes()
                               : model.dtoTypes(),
                               StringUtils.hasText(model.summary())
                               ? model.summary()
                               : models.defaultSummary(),
                               StringUtils.hasText(model.description())
                               ? model.description()
                               : models.defaultDescription(),
                               defaultType == null
                               ? model.exampleType()
                               : defaultType));
        }
        return examples;
    }

    /**
     * 获取示例
     *
     * @param model        模型
     * @param defaultClazz 模型中未指定类型时使用的类型
     * @return 示例
     */
    public static Example getExample(OpenApiModel model,
                                     Class<?> defaultClazz)
            throws
            Exception {
        return getExample(model,
                          defaultClazz,
                          null);
    }

    /**
     * 获取示例
     *
     * @param model        模型
     * @param defaultClazz 模型中未指定类型时使用的类型
     * @param defaultType  默认示例类型（将会忽略model注解中的设置）
     * @return 示例
     */
    public static Example getExample(OpenApiModel model,
                                     Class<?> defaultClazz,
                                     ExampleType defaultType)
            throws
            Exception {
        return getExample(model.type()
                               .equals(Void.class)
                          ? defaultClazz
                          : model.type(),
                          model.genericTypes(),
                          model.dtoTypes(),
                          model.summary(),
                          model.description(),
                          defaultType == null
                          ? model.exampleType()
                          : defaultType);
    }

    /**
     * 获取示例
     *
     * @param clazz        类型
     * @param genericTypes 泛型类型
     * @param dtoTypes     业务模型类型
     * @param summary      注释
     * @param description  说明
     * @param type         示例类型
     * @return 示例
     */
    public static Example getExample(Class<?> clazz,
                                     OpenApiGenericType[] genericTypes,
                                     OpenApiDtoType[] dtoTypes,
                                     String summary,
                                     String description,
                                     ExampleType type)
            throws
            Exception {
        Map<Class<?>, Map<Integer, Map<String, Class<?>>>> genericTypeMap = new HashMap<>();
        Arrays.stream(genericTypes)
              .forEach(x -> {
                  Class<?> gType = x.type();
                  if (gType.equals(Void.class))
                      gType = clazz;
                  if (!genericTypeMap.containsKey(gType))
                      genericTypeMap.put(gType,
                                         new HashMap<>());

                  //如果泛型类型参数指定了相同的泛型类型，则通过层级来判断泛型类型参数属于类型中的哪一个泛型类型
                  Map<Integer, Map<String, Class<?>>> levelMap = genericTypeMap.get(gType);
                  if (!levelMap.containsKey(x.level()))
                      levelMap.put(x.level(),
                                   new HashMap<>());

                  Map<String, Class<?>> argumentList = levelMap.get(x.level());
                  Arrays.stream(x.arguments())
                        .forEach(y -> argumentList.put(y.name(),
                                                       y.type()));
              });

        Map<Class<?>, Tuple2<Integer, String[]>> dtoTypeMap = new HashMap<>();
        Arrays.stream(dtoTypes)
              .forEach(x -> {
                  Class<?> dtoType = x.type();
                  if (dtoType.equals(Void.class))
                      dtoType = clazz;
                  dtoTypeMap.put(dtoType,
                                 new Tuple2<>(
                                         x.mainTagLevel(),
                                         x.customTags()));
              });

        Example example = getExample(clazz,
                                     genericTypeMap,
                                     dtoTypeMap,
                                     type);
        example.setSummary(summary);
        example.setDescription(description);
        return example;
    }

    /**
     * 获取示例
     *
     * @param clazz          类型
     * @param genericTypeMap 泛型类型
     * @param dtoTypeMap     业务模型类型
     * @param type           示例类型
     * @return 示例
     */
    public static Example getExample(Class<?> clazz,
                                     Map<Class<?>, Map<Integer, Map<String, Class<?>>>> genericTypeMap,
                                     Map<Class<?>, Tuple2<Integer, String[]>> dtoTypeMap,
                                     ExampleType type)
            throws
            Exception {
        Example example = new Example();
        IOpenApiAny any = getOrNullFor(clazz,
                                       genericTypeMap,
                                       dtoTypeMap,
                                       true);
        example.setValue(anyToExample(any,
                                      type));
        return example;
    }

    /**
     * 架构转数据
     *
     * @param any  架构数据
     * @param type 示例类型
     * @return Json格式数据
     */
    public static String anyToExample(IOpenApiAny any,
                                      ExampleType type) {
        switch (type) {
            default:
            case Json:
                return anyToJson(any,
                                 false,
                                 1);
            case JsonWithComments:
                return anyToJson(any,
                                 true,
                                 1);
            case TsClass:
                return anyToTsClass(any,
                                    1);
        }
    }

    /**
     * 转Json格式数据
     *
     * @param any         架构数据
     * @param addComments 添加注释
     * @param deep        当前深度
     * @return Json格式数据
     */
    public static String anyToJson(IOpenApiAny any,
                                   boolean addComments,
                                   int deep) {
        StringBuilder sb = new StringBuilder();

        switch (any.getAnyType()) {
            case Primitive:
                OpenApiPrimitive primitive = (OpenApiPrimitive) any;
                switch (primitive.getPrimitiveType()) {
                    case Long:
                    case String:
                    case Date:
                    case Password:
                    default:
                        sb.append(String.format("\"%s\"",
                                                primitive.getValue()));
                        break;
                    case Integer:
                    case Float:
                    case Double:
                    case Byte:
                    case Boolean:
                        sb.append(primitive.getValue());
                        break;
                    case Decimal:
                        sb.append(primitive.getValue()
                                           .toString());
                        break;
                    case Binary:
                    case DateTime:
                        //TODO
                        sb.append(String.format("\"暂不持支%s数据类型\"",
                                                primitive.getPrimitiveType()
                                                         .toString()));
                        break;
                }
                break;
            case Null:
            default:
                sb.append("null");
                break;
            case Array:
                OpenApiArray array = (OpenApiArray) any;
                sb.append(String.format("[\r\n%s%s\r\n%s]",
                                        getOffset(deep),
                                        anyToJson(array.getAny(),
                                                  addComments,
                                                  deep + 1),
                                        getOffset(deep - 1)));
                break;
            case Object:
                OpenApiObject object = (OpenApiObject) any;
                //添加注释
                if (addComments) sb.append(getSummary(object,
                                                      getOffset(deep)));
                sb.append("{");
                String offset = getOffset(deep);
                int i = object.size();
                for (String fieldName : object.keySet()) {
                    IOpenApiAny fieldAny = object.get(fieldName);
                    //添加注释
                    if (addComments) sb.append(getSummary(fieldAny,
                                                          offset));
                    //添加键值对示例
                    sb.append(String.format("\r\n%s\"%s\": %s%s\r\n",
                                            offset,
                                            fieldName,
                                            anyToJson(fieldAny,
                                                      addComments,
                                                      deep + 1),
                                            i > 1
                                            ? ","
                                            : ""));
                    i--;
                }
                sb.append(String.format("%s}",
                                        getOffset(deep - 1)));
                break;
        }

        return sb.toString();
    }

    /**
     * 转Ts类数据
     *
     * @param any  架构数据
     * @param deep 当前深度
     * @return Ts类数据
     */
    private static String anyToTsClass(IOpenApiAny any,
                                       int deep) {
        //TODO
        return "";
    }

    /**
     * 获取缩进
     *
     * @param deep 层级深度
     * @return 字符串
     */
    private static String getOffset(int deep) {
        return String.join("",
                           CollectionsExtension.fill(new String[Math.max(deep,
                                                                         0)],
                                                     "    "));
    }

    /**
     * 获取注释
     *
     * @param any    说明类
     * @param offset 缩进
     * @return 注释
     */
    private static String getSummary(IOpenApiAny any,
                                     String offset) {
        StringBuilder sb = new StringBuilder();
        if (any.getDescription()
               .size() > 0) {
            //添加注释
            sb.append(String.format("\r\n%s/**",
                                    offset));
            for (String description : any.getDescription()) {
                sb.append(String.format("\r\n%s * %s",
                                        offset,
                                        description));
            }
            sb.append(String.format("\r\n%s */",
                                    offset));
        }
        return sb.toString();
    }
}
