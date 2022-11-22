package project.extension.openapi.extention;

import project.extension.cache.ICache;
import project.extension.cache.NaiveCache;
import project.extension.collections.CollectionsExtension;
import com.alibaba.fastjson.serializer.PropertyFilter;
import project.extension.openapi.fastjson.JsonPropertyPreFilter;
import project.extension.openapi.model.IOpenApiAny;

import java.util.*;

/**
 * 缓存
 *
 * @author LCTR
 * @date 2022-03-18
 */
public class CacheExtension {
    /**
     * 类型的Api架构
     */
    public static final ICache<String, IOpenApiAny> openApiObjectCache = new NaiveCache<>(new Properties(),
                                                                                          "openApiObject");

    /**
     * 类型的命名空间
     */
    public static final ICache<String, String> assemblyOfTypeCache = new NaiveCache<>(new Properties(),
                                                                                      "assemblyOfType");

    /**
     * 类型的架构类型集合
     */
    public static final ICache<String, List<String>> typesOfTypeCache = new NaiveCache<>(new Properties(),
                                                                                         "typesOfType");

    /**
     * 类型的架构属性集合
     */
    public static final ICache<String, List<String>> fieldsOfTypeCache = new NaiveCache<>(new Properties(),
                                                                                          "fieldsOfType");

    /**
     * 枚举的字段说明集合
     */
    public static final ICache<String, Map<String, String>> enumNameAndDescriptionCache = new NaiveCache<>(new Properties(),
                                                                                                           "enumNameAndDescription");

    /**
     * 类型的属性预过滤器集合
     */
    public static final ICache<String, JsonPropertyPreFilter> propertyPreFiltersOfTypeCache = new NaiveCache<>(new Properties(),
                                                                                                               "propertyPreFiltersOfType");

    /**
     * 类型的属性过滤器集合
     */
    public static final ICache<String, PropertyFilter> propertyFiltersOfTypeCache = new NaiveCache<>(new Properties(),
                                                                                                     "propertyFiltersOfType");

    /**
     * 获取类型的架构字段集合
     *
     * @param type 类型
     * @return 架构字段集合
     */
    public static Hashtable<String, List<String>> getFieldsOfTypeDic(Class<?> type) {
        return getFieldsOfTypeDic(type,
                                  true);
    }

    /**
     * 获取类型的架构字段集合
     *
     * @param type      类型
     * @param deepClone 深度复制
     * @return 架构字段集合
     */
    public static Hashtable<String, List<String>> getFieldsOfTypeDic(Class<?> type,
                                                                     boolean deepClone) {
        Hashtable<String, List<String>> dic = new Hashtable<>();
        getFieldsOfTypeDic(dic,
                           type.getTypeName(),
                           deepClone);
        return dic;
    }

    /**
     * 获取类型的架构字段集合
     *
     * @param dic      架构属性集合
     * @param typeName 类名
     */
    private static void getFieldsOfTypeDic(Hashtable<String, List<String>> dic,
                                           String typeName) {
        getFieldsOfTypeDic(dic,
                           typeName,
                           true);
    }

    /**
     * 获取类型的架构字段集合
     *
     * @param dic       架构属性集合
     * @param typeName  类名
     * @param deepClone 深度复制
     */
    private static void getFieldsOfTypeDic(Hashtable<String, List<String>> dic,
                                           String typeName,
                                           boolean deepClone) {
        if (fieldsOfTypeCache.exist(typeName))
            dic.put(typeName,
                    deepClone
                    ? CollectionsExtension.deepCopy(fieldsOfTypeCache.get(typeName))
                    : fieldsOfTypeCache.get(typeName));

        if (typesOfTypeCache.exist(typeName)) {
            List<String> list = typesOfTypeCache.get(typeName);
            if (list != null) {
                list.forEach(item -> getFieldsOfTypeDic(dic,
                                                        item));
            }
        }
    }

    /**
     * 设置类型的架构属性集合
     *
     * @param type 类型
     * @param dic  架构属性集合
     */
    public static void setFieldsOfTypeDic(Class<?> type,
                                          Hashtable<String, List<String>> dic) {
        String typeName = type.getTypeName();
        if (!typesOfTypeCache.exist(typeName)) {
            List<String> types = CollectionsExtension.filter(dic.keySet()
                                                                .stream(),
                                                             k -> !k.equals(typeName));
            typesOfTypeCache.addOrUpdate(typeName,
                                         types);
        } else
            typesOfTypeCache.addOrUpdate(typeName,
                                         CollectionsExtension.union(typesOfTypeCache.get(typeName),
                                                                    CollectionsExtension.filter(dic.keySet()
                                                                                                   .stream(),
                                                                                                k -> !k.equals(typeName))));

        for (String key : dic.keySet()) {
            if (!fieldsOfTypeCache.exist(key)) {
                List<String> propertys = CollectionsExtension.deepCopy(dic.get(key));
                fieldsOfTypeCache.addOrUpdate(typeName,
                                              propertys);
            }
        }
    }

    /**
     * 获取类型的属性预过滤器
     *
     * @param type 类型
     * @return 属性预过滤器
     */
    public static JsonPropertyPreFilter getPropertyPreFilterOfType(Class<?> type) {
        String typeName = type.getTypeName();
        if (propertyPreFiltersOfTypeCache.exist(typeName))
            return propertyPreFiltersOfTypeCache.get(typeName);
        return null;
    }

    /**
     * 设置类型的属性预过滤器
     *
     * @param type   类型
     * @param filter 过滤器
     * @return 属性预过滤器
     */
    public static JsonPropertyPreFilter setPropertyPreFilterOfType(Class<?> type,
                                                                   JsonPropertyPreFilter filter) {
        String typeName = type.getTypeName();
        propertyPreFiltersOfTypeCache.addOrUpdate(typeName,
                                                  filter);
        return filter;
    }

    /**
     * 获取类型的属性过滤器
     *
     * @param type 类型
     * @return 属性过滤器
     */
    public static PropertyFilter getPropertyFilterOfType(Class<?> type) {
        String typeName = type.getTypeName();
        if (propertyFiltersOfTypeCache.exist(typeName))
            return propertyFiltersOfTypeCache.get(typeName);
        return null;
    }

    /**
     * 设置类型的属性过滤器
     *
     * @param type   类型
     * @param filter 过滤器
     * @return 属性过滤器
     */
    public static PropertyFilter setPropertyFilterOfType(Class<?> type,
                                                         PropertyFilter filter) {
        String typeName = type.getTypeName();
        propertyFiltersOfTypeCache.addOrUpdate(typeName,
                                               filter);
        return filter;
    }

    /**
     * 清空
     */
    public static void cleanUp() {
        openApiObjectCache.cleanUp();
        assemblyOfTypeCache.cleanUp();
        typesOfTypeCache.cleanUp();
        fieldsOfTypeCache.cleanUp();
        enumNameAndDescriptionCache.cleanUp();
        propertyPreFiltersOfTypeCache.cleanUp();
        propertyFiltersOfTypeCache.cleanUp();
    }
}
