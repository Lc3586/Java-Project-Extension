package project.extension.collections;

import com.alibaba.fastjson.JSONObject;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 集合工具类
 */
public class MapExtension {
    /**
     * 对象转为键值对集合
     *
     * @param jsonStr JSON字符串
     * @return 键值对集合
     * @throws IllegalAccessException 访问权限异常
     */
    public static Map<String, Object> convertJSONObject2Map(String jsonStr) {
        JSONObject jsonObj = JSONObject.parseObject(jsonStr);
        return convertJSONObject2Map(jsonObj);
    }

    /**
     * 对象转为键值对集合
     *
     * @param jsonObj JSON对象
     * @return 键值对集合
     * @throws IllegalAccessException 访问权限异常
     */
    public static Map<String, Object> convertJSONObject2Map(JSONObject jsonObj) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : jsonObj.keySet()) {

            Object value = jsonObj.get(key);
            map.put(key,
                    value);
        }
        return map;
    }

    /**
     * 对象转为键值对集合
     *
     * @param obj 对象
     * @return 键值对集合
     * @throws IllegalAccessException 访问权限异常
     */
    public static Map<String, Object> convertObject2Map(Object obj)
            throws
            IllegalAccessException {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> cs = obj.getClass();
        for (Field field : cs.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName,
                    value);
        }
        return map;
    }

    /**
     * 键值对集合转为对象
     *
     * @param map 键值对集合
     * @return 对象
     * @throws Exception 访问权限异常
     */
    public static Object convertMap2Object(Map<String, Object> map,
                                           Class<?> type)
            throws
            Exception {
        if (map == null)
            return null;
        Object obj = type.getDeclaredConstructor()
                         .newInstance();
        Field[] fields = obj.getClass()
                            .getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            field.setAccessible(true);

            if (map.containsKey(field.getName())) {
                field.set(obj,
                          map.get(field.getName()));
            }
        }
        return obj;
    }

    /**
     * 键值对集合转为对象
     *
     * @param map 键值对集合
     * @return Json对象
     * @throws Exception 访问权限异常
     */
    public static JSONObject convertMap2JSONObject(Map<String, Object> map) {
        if (map == null)
            return null;
        JSONObject jsonObj = new JSONObject();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            jsonObj.put(key,
                        value);
        }
        return jsonObj;
    }

    /**
     * 键值对集合排序
     *
     * @param map 键值对集合
     * @return 排序后的键值对集合
     * @throws IllegalAccessException 访问权限异常
     */
    public static <T> Map<String, T> mapSortByKey(Map<String, T> map) {
        return mapSortByKey(map,
                            null);
    }

    /**
     * 键值对集合排序
     *
     * @param map 键值对集合
     * @return 排序后的键值对集合
     * @throws IllegalAccessException 访问权限异常
     */
    public static <TKey, TValue> Map<TKey, TValue> mapSortByKey(Map<TKey, TValue> map,
                                                                @Nullable
                                                                        Comparator<TKey> comparator) {
        List<TKey> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList,
                         comparator);

        for (TKey key : keyList) {
            TValue value = map.get(key);
            map.remove(key);
            map.put(key,
                    value);
        }

        return map;
    }
}
