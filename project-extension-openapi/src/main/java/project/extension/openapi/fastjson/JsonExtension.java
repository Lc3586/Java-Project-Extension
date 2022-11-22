package project.extension.openapi.fastjson;

import com.alibaba.fastjson.serializer.SerializerFeature;
import project.extension.collections.CollectionsExtension;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import project.extension.openapi.extention.CacheExtension;

import java.util.*;

/**
 * Json相关拓展方法
 *
 * @author LCTR
 * @date 2022-03-18
 */
public class JsonExtension {
    /**
     * 获取属性预过滤器
     *
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @param ignoreProperties    忽略的属性
     * @return 属性预过滤器
     */
    private static JsonPropertyPreFilter getPropertyPreFilter(Class<?> openApiSchemaType,
                                                              Hashtable<String, List<String>> exceptionProperties,
                                                              Hashtable<String, List<String>> ignoreProperties) {
        JsonPropertyPreFilter filter;
        if (exceptionProperties == null && ignoreProperties == null) {
            filter = CacheExtension.getPropertyPreFilterOfType(openApiSchemaType);
            if (filter == null) {
                filter = new JsonPropertyPreFilter(openApiSchemaType);
                CacheExtension.setPropertyPreFilterOfType(openApiSchemaType,
                                                          filter);
            }
        } else
            filter = new JsonPropertyPreFilter(openApiSchemaType,
                                               exceptionProperties,
                                               ignoreProperties);
        return filter;
    }

    /**
     * 将对象序列化成Json字符串
     *
     * @param obj                 需要序列化的对象
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @return Json序列化字符串
     */
    public static String toOpenApiJson(Object obj,
                                       Class<?> openApiSchemaType,
                                       String... exceptionProperties) {
        return toOpenApiJson(obj,
                             openApiSchemaType,
                             exceptionProperties,
                             null);
    }

    /**
     * 将对象序列化成Json字符串
     *
     * @param obj               需要序列化的对象
     * @param openApiSchemaType 指定接口架构类型
     * @param ignoreProperties  忽略的属性
     * @return Json序列化字符串
     */
    public static String toOpenApiJsonIgnore(Object obj,
                                             Class<?> openApiSchemaType,
                                             String... ignoreProperties) {
        return toOpenApiJson(obj,
                             openApiSchemaType,
                             null,
                             ignoreProperties);
    }

    /**
     * 将对象序列化成Json字符串
     *
     * @param obj                 需要序列化的对象
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @param ignoreProperties    忽略的属性
     * @return Json序列化字符串
     */
    public static String toOpenApiJson(Object obj,
                                       Class<?> openApiSchemaType,
                                       String[] exceptionProperties,
                                       String[] ignoreProperties) {
        return toOpenApiJson(obj,
                             openApiSchemaType,
                             CollectionsExtension.anyPlus(exceptionProperties)
                             ? new Hashtable<String, List<String>>() {
                                 {
                                     put(openApiSchemaType.getTypeName(),
                                         new ArrayList<>(Arrays.asList(exceptionProperties)));
                                 }
                             }
                             : null,
                             CollectionsExtension.anyPlus(ignoreProperties)
                             ? new Hashtable<String, List<String>>() {
                                 {
                                     put(openApiSchemaType.getTypeName(),
                                         new ArrayList<>(Arrays.asList(ignoreProperties)));
                                 }
                             }
                             : null);
    }

    /**
     * 将对象序列化成Json字符串
     *
     * @param obj                 需要序列化的对象
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @return 序列化字符串
     */
    public static String toOpenApiJson(Object obj,
                                       Class<?> openApiSchemaType,
                                       Hashtable<String, List<String>> exceptionProperties) {
        return toOpenApiJson(obj,
                             openApiSchemaType,
                             exceptionProperties,
                             null);
    }

    /**
     * 将对象序列化成Json字符串
     *
     * @param obj               需要序列化的对象
     * @param openApiSchemaType 指定接口架构类型
     * @param ignoreProperties  忽略的属性
     * @return 序列化字符串
     */
    public static String toOpenApiJsonIgnore(Object obj,
                                             Class<?> openApiSchemaType,
                                             Hashtable<String, List<String>> ignoreProperties) {
        return toOpenApiJson(obj,
                             openApiSchemaType,
                             null,
                             ignoreProperties);
    }

    /**
     * 将对象序列化成Json字符串
     * <p>如果在要例外输出的属性和忽略的属性中同时指定了同一个属性，那么最终不会输出该属性</p>
     *
     * @param obj                 需要序列化的对象
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @param ignoreProperties    忽略的属性
     * @return Json序列化字符串
     */
    public static String toOpenApiJson(Object obj,
                                       Class<?> openApiSchemaType,
                                       Hashtable<String, List<String>> exceptionProperties,
                                       Hashtable<String, List<String>> ignoreProperties) {
        return JSON.toJSONString(
                obj,
                getPropertyPreFilter(
                        openApiSchemaType,
                        exceptionProperties,
                        ignoreProperties),
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 将Json字符串反序列化成对象
     *
     * @param json                需要反序列化的Json字符串
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @return Json反序列化对象
     */
    public static Object toOpenApiObject(String json,
                                         Class<?> openApiSchemaType,
                                         String... exceptionProperties) {
        return toOpenApiObject(json,
                               openApiSchemaType,
                               exceptionProperties,
                               null);
    }

    /**
     * 将Json字符串反序列化成对象
     *
     * @param json              需要反序列化的Json字符串
     * @param openApiSchemaType 指定接口架构类型
     * @param ignoreProperties  忽略的属性
     * @return Json反序列化对象
     */
    public static Object toOpenApiObjectIgnore(String json,
                                               Class<?> openApiSchemaType,
                                               String... ignoreProperties) {
        return toOpenApiObject(json,
                               openApiSchemaType,
                               null,
                               ignoreProperties);
    }

    /**
     * 将Json字符串反序列化成对象
     *
     * @param json                需要反序列化的Json字符串
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @param ignoreProperties    忽略的属性
     * @return Json反序列化对象
     */
    public static Object toOpenApiObject(String json,
                                         Class<?> openApiSchemaType,
                                         String[] exceptionProperties,
                                         String[] ignoreProperties) {
        return toOpenApiObject(json,
                               openApiSchemaType,
                               CollectionsExtension.anyPlus(exceptionProperties)
                               ? new Hashtable<String, List<String>>() {
                                   {
                                       put(openApiSchemaType.getTypeName(),
                                           new ArrayList<>(Arrays.asList(exceptionProperties)));
                                   }
                               }
                               : null,
                               CollectionsExtension.anyPlus(ignoreProperties)
                               ? new Hashtable<String, List<String>>() {
                                   {
                                       put(openApiSchemaType.getTypeName(),
                                           new ArrayList<>(Arrays.asList(ignoreProperties)));
                                   }
                               }
                               : null);
    }

    /**
     * 将Json字符串反序列化成对象
     *
     * @param json                需要反序列化的Json字符串
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @return Json反序列化对象
     */
    public static Object toOpenApiObject(String json,
                                         Class<?> openApiSchemaType,
                                         Hashtable<String, List<String>> exceptionProperties) {
        return toOpenApiObject(json,
                               openApiSchemaType,
                               exceptionProperties,
                               null);
    }

    /**
     * 将Json字符串反序列化成对象
     *
     * @param json              需要反序列化的Json字符串
     * @param openApiSchemaType 指定接口架构类型
     * @param ignoreProperties  忽略的属性
     * @return Json反序列化对象
     */
    public static Object toOpenApiObjectIgnore(String json,
                                               Class<?> openApiSchemaType,
                                               Hashtable<String, List<String>> ignoreProperties) {
        return toOpenApiObject(json,
                               openApiSchemaType,
                               null,
                               ignoreProperties);
    }

    /**
     * 将Json字符串反序列化成对象
     * <p>如果在要例外输出的属性和忽略的属性中同时指定了同一个属性，那么最终不会输出该属性</p>
     *
     * @param json                需要反序列化的Json字符串
     * @param openApiSchemaType   指定接口架构类型
     * @param exceptionProperties 要例外输出的属性
     * @param ignoreProperties    忽略的属性
     * @return Json反序列化对象
     */
    public static Object toOpenApiObject(String json,
                                         Class<?> openApiSchemaType,
                                         Hashtable<String, List<String>> exceptionProperties,
                                         Hashtable<String, List<String>> ignoreProperties) {
        //TODO Json反序列化过滤掉不需要的字段
        return JSONObject.parseObject(json,
                                      openApiSchemaType);
    }

//    /**
//     * 将对象序列化成Json字符串
//     *
//     * @param obj                 需要序列化的对象
//     * @param exceptionProperties 要例外输出的属性
//     * @param <TOpenApiSchema>    接口架构类型
//     * @return Json序列化字符串
//     */
//    public static <TOpenApiSchema> String toOpenApiJson(TOpenApiSchema obj, String... exceptionProperties) {
//        return toOpenApiJson(obj, exceptionProperties, null);
//    }
//
//    /**
//     * 将对象序列化成Json字符串
//     *
//     * @param obj              需要序列化的对象
//     * @param ignoreProperties 忽略的属性
//     * @param <TOpenApiSchema> 接口架构类型
//     * @return Json序列化字符串
//     */
//    public static <TOpenApiSchema> String ToOpenApiJsonIgnore(TOpenApiSchema obj, String... ignoreProperties) {
//        return toOpenApiJson(obj, null, ignoreProperties);
//    }
//
//    /**
//     * 将对象序列化成Json字符串
//     *
//     * @param obj                 需要序列化的对象
//     * @param exceptionProperties 要例外输出的属性
//     * @param ignoreProperties    忽略的属性
//     * @param <TOpenApiSchema>    接口架构类型
//     * @return Json序列化字符串
//     */
//    public static <TOpenApiSchema> String toOpenApiJson(TOpenApiSchema obj, String[] exceptionProperties, String[] ignoreProperties) {
//        return toOpenApiJson(obj, CollectionsExtension.anyPlus(exceptionProperties) ? new Hashtable<String, List<String>>() {
//            {
//                put(obj.getClass().getTypeName(), Arrays.asList(exceptionProperties));
//            }
//        } : null, CollectionsExtension.anyPlus(ignoreProperties) ? new Hashtable<String, List<String>>() {
//            {
//                put(obj.getClass().getTypeName(), Arrays.asList(ignoreProperties));
//            }
//        } : null);
//    }
//
//    /**
//     * 将对象序列化成Json字符串
//     *
//     * @param obj                 需要序列化的对象
//     * @param exceptionProperties 要例外输出的属性
//     * @param <TOpenApiSchema>    接口架构类型
//     * @return Json序列化字符串
//     */
//    public static <TOpenApiSchema> String toOpenApiJson(TOpenApiSchema obj, Hashtable<String, List<String>> exceptionProperties) {
//        return toOpenApiJson(obj, exceptionProperties, null);
//    }
//
//    /**
//     * 将对象序列化成Json字符串
//     *
//     * @param obj              需要序列化的对象
//     * @param ignoreProperties 忽略的属性
//     * @param <TOpenApiSchema> 接口架构类型
//     * @return Json序列化字符串
//     */
//    public static <TOpenApiSchema> String ToOpenApiJsonIgnore(TOpenApiSchema obj, Hashtable<String, List<String>> ignoreProperties) {
//        return toOpenApiJson(obj, null, ignoreProperties);
//    }
//
//    /**
//     * 将对象序列化成Json字符串
//     * <p>如果在要例外输出的属性和忽略的属性中同时指定了同一个属性，那么最终不会输出该属性</p>
//     *
//     * @param obj                 需要序列化的对象
//     * @param exceptionProperties 要例外输出的属性
//     * @param ignoreProperties    忽略的属性
//     * @param <TOpenApiSchema>    接口架构类型
//     * @return Json序列化字符串
//     */
//    public static <TOpenApiSchema> String toOpenApiJson(TOpenApiSchema obj, Hashtable<String, List<String>> exceptionProperties, Hashtable<String, List<String>> ignoreProperties) {
//        return JSON.toJSONString(obj, getPropertyPreFilter(obj.getClass(), exceptionProperties, ignoreProperties));
//    }

//    /**
//     * 将Json字符串反序列化成对象
//     *
//     * @param json                需要反序列化的Json字符串
//     * @param openApiSchemaType   指定接口架构类型
//     * @param exceptionProperties 要例外输出的属性
//     * @param <TOpenApiSchema>    接口架构类型
//     * @return Json反序列化对象
//     */
//    public static <TOpenApiSchema> TOpenApiSchema toOpenApiObject(String json, Class<TOpenApiSchema> openApiSchemaType, String... exceptionProperties) {
//        return toOpenApiObject(json, openApiSchemaType, exceptionProperties, null);
//    }
//
//    /**
//     * 将Json字符串反序列化成对象
//     *
//     * @param json              需要反序列化的Json字符串
//     * @param openApiSchemaType 指定接口架构类型
//     * @param ignoreProperties  忽略的属性
//     * @param <TOpenApiSchema>  接口架构类型
//     * @return Json反序列化对象
//     */
//    public static <TOpenApiSchema> TOpenApiSchema toOpenApiObjectIgnore(String json, Class<TOpenApiSchema> openApiSchemaType, String... ignoreProperties) {
//        return toOpenApiObject(json, openApiSchemaType, null, ignoreProperties);
//    }
//
//    /**
//     * 将Json字符串反序列化成对象
//     *
//     * @param json                需要反序列化的Json字符串
//     * @param openApiSchemaType   指定接口架构类型
//     * @param exceptionProperties 要例外输出的属性
//     * @param ignoreProperties    忽略的属性
//     * @param <TOpenApiSchema>    接口架构类型
//     * @return Json反序列化对象
//     */
//    public static <TOpenApiSchema> TOpenApiSchema toOpenApiObject(String json, Class<TOpenApiSchema> openApiSchemaType, String[] exceptionProperties, String[] ignoreProperties) {
//        return toOpenApiObject(json, openApiSchemaType, CollectionsExtension.anyPlus(exceptionProperties) ? new Hashtable<String, List<String>>() {
//            {
//                put(openApiSchemaType.getTypeName(), Arrays.asList(exceptionProperties));
//            }
//        } : null, CollectionsExtension.anyPlus(ignoreProperties) ? new Hashtable<String, List<String>>() {
//            {
//                put(openApiSchemaType.getTypeName(), Arrays.asList(ignoreProperties));
//            }
//        } : null);
//    }
//
//    /**
//     * 将Json字符串反序列化成对象
//     *
//     * @param json                需要反序列化的Json字符串
//     * @param openApiSchemaType   指定接口架构类型
//     * @param exceptionProperties 要例外输出的属性
//     * @param <TOpenApiSchema>    接口架构类型
//     * @return Json反序列化对象
//     */
//    public static <TOpenApiSchema> TOpenApiSchema toOpenApiObject(String json, Class<TOpenApiSchema> openApiSchemaType, Hashtable<String, List<String>> exceptionProperties) {
//        return toOpenApiObject(json, openApiSchemaType, exceptionProperties, null);
//    }
//
//    /**
//     * 将Json字符串反序列化成对象
//     *
//     * @param json              需要反序列化的Json字符串
//     * @param openApiSchemaType 指定接口架构类型
//     * @param ignoreProperties  忽略的属性
//     * @param <TOpenApiSchema>  接口架构类型
//     * @return Json反序列化对象
//     */
//    public static <TOpenApiSchema> TOpenApiSchema toOpenApiObjectIgnore(String json, Class<TOpenApiSchema> openApiSchemaType, Hashtable<String, List<String>> ignoreProperties) {
//        return toOpenApiObject(json, openApiSchemaType, null, ignoreProperties);
//    }
//
//    /**
//     * 将Json字符串反序列化成对象
//     * <p>如果在要例外输出的属性和忽略的属性中同时指定了同一个属性，那么最终不会输出该属性</p>
//     *
//     * @param json                需要反序列化的Json字符串
//     * @param openApiSchemaType   指定接口架构类型
//     * @param exceptionProperties 要例外输出的属性
//     * @param ignoreProperties    忽略的属性
//     * @param <TOpenApiSchema>    接口架构类型
//     * @return Json反序列化对象
//     */
//    public static <TOpenApiSchema> TOpenApiSchema toOpenApiObject(String json, Class<TOpenApiSchema> openApiSchemaType, Hashtable<String, List<String>> exceptionProperties, Hashtable<String, List<String>> ignoreProperties) {
//        //TODO Json反序列化过滤掉不需要的字段
//        return JSONObject.parseObject(json, openApiSchemaType);
//    }
}
