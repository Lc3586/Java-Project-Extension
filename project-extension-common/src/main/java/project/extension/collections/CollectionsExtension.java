package project.extension.collections;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import project.extension.tuple.Tuple2;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合拓展方法
 *
 * @author LCTR
 * @date 2022-03-18
 */
public class CollectionsExtension {
    /**
     * 集合是否有存在元素
     *
     * @param array 集合数据
     * @param <T>   元素类型
     * @return 判断结果
     */
    public static <T> boolean anyPlus(T[] array) {
        return array != null && array.length != 0 && anyPlus(Arrays.asList(array),
                                                             null);
    }

    /**
     * 集合是否有存在元素
     *
     * @param array     集合数据
     * @param predicate 条件表达式
     * @param <T>       元素类型
     * @return 判断结果
     */
    public static <T> boolean anyPlus(T[] array,
                                      Predicate<? super T> predicate) {
        return array != null && array.length != 0 && anyPlus(Arrays.asList(array),
                                                             predicate);
    }

    /**
     * 集合是否有存在元素
     *
     * @param list 集合数据
     * @param <T>  元素类型
     * @return 判断结果
     */
    public static <T> boolean anyPlus(Collection<T> list) {
        return anyPlus(list,
                       null);
    }

    /**
     * 集合是否有存在元素
     *
     * @param list      集合数据
     * @param predicate 条件表达式
     * @param <T>       元素类型
     * @return 判断结果
     */
    public static <T> boolean anyPlus(Collection<T> list,
                                      Predicate<? super T> predicate) {
        if (list == null || list.size() == 0)
            return false;

        if (predicate == null)
            return true;

        return list.stream()
                   .anyMatch(predicate);
    }

    /**
     * 集合是否存在符合要求的元素
     *
     * @param list      集合数据
     * @param predicate 条件表达式
     * @param <T>       元素类型
     * @return 判断结果
     */
    public static <T> boolean allPlus(Collection<T> list,
                                      Predicate<? super T> predicate) {
        if (list == null || list.size() == 0)
            return false;

        if (predicate == null)
            return true;

        return list.stream()
                   .allMatch(predicate);
    }

    /**
     * 集合是否有存在元素
     *
     * @param map 集合数据
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 判断结果
     */
    public static <K, V> boolean anyPlus(Map<K, V> map) {
        return anyPlus(map,
                       null);
    }

    /**
     * 集合是否有存在元素
     *
     * @param map          集合数据
     * @param predicateKey 条件表达式
     * @param <K>          键类型
     * @param <V>          值类型
     * @return 判断结果
     */
    public static <K, V> boolean anyPlus(Map<K, V> map,
                                         Predicate<? super K> predicateKey) {
        if (map == null || map.size() == 0)
            return false;

        if (predicateKey == null)
            return true;

        return filter(map,
                      predicateKey).size() > 0;
    }

    /**
     * 集合的第一个元素
     * <p>元素为null或为空时返回null</p>
     *
     * @param map 集合数据
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 第一个元素或null
     */
    public static <K, V> Map.Entry<K, V> first(Map<K, V> map) {
        if (map == null || map.size() == 0)
            return null;

        return map.entrySet()
                  .iterator()
                  .next();
    }

    /**
     * 集合的第一个元素的键
     * <p>元素为null或为空时返回null</p>
     *
     * @param map 集合数据
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 第一个元素的键或null
     */
    public static <K, V> K firstKey(Map<K, V> map) {
        Map.Entry<K, V> first = first(map);
        if (first == null)
            return null;
        else
            return first.getKey();
    }

    /**
     * 集合的第一个元素的值
     * <p>元素为null或为空时返回null</p>
     *
     * @param map 集合数据
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 第一个元素的值或null
     */
    public static <K, V> V firstValue(Map<K, V> map) {
        Map.Entry<K, V> first = first(map);
        if (first == null)
            return null;
        else
            return first.getValue();
    }

    /**
     * 深度拷贝
     *
     * @param source 元数据
     * @param <T>    元素类型
     * @return 拷贝的数据
     */
    public static <T> List<T> deepCopy(List<T> source) {
        ArrayList<T> result = new ArrayList<>(source.size());
        source.forEach(x -> result.add(null));
        Collections.copy(result,
                         source);
        return result;
    }

    /**
     * 过滤数据
     *
     * @param list      集合
     * @param predicate 过滤表达式
     * @param <T>       元素类型
     * @return 过滤的数据
     */
    public static <T> List<T> filter(List<T> list,
                                     Predicate<? super T> predicate) {
        return filter(list.stream(),
                      predicate);
    }

    /**
     * 过滤数据
     *
     * @param stream    数据流
     * @param predicate 过滤表达式
     * @param <T>       元素类型
     * @return 过滤的数据
     */
    public static <T> List<T> filter(Stream<T> stream,
                                     Predicate<? super T> predicate) {
        return stream.filter(predicate)
                     .collect(Collectors.toList());
    }

    /**
     * 过滤数据
     *
     * @param map          集合
     * @param predicateKey 过滤键表达式
     * @param <K>          键类型
     * @param <V>          值类型
     * @return 过滤的数据
     */
    public static <K, V> List<K> filter(Map<K, V> map,
                                        Predicate<? super K> predicateKey) {
        return filter(map.keySet()
                         .stream(),
                      predicateKey);
    }

    /**
     * 过滤数据
     *
     * @param dic          集合
     * @param predicateKey 过滤键表达式
     * @param <K>          键类型
     * @param <V>          值类型
     * @return 过滤的数据
     */
    public static <K, V> List<K> filter(Hashtable<K, V> dic,
                                        Predicate<? super K> predicateKey) {
        return filter(dic.keySet()
                         .stream(),
                      predicateKey);
    }

    /**
     * 新增或更新
     *
     * @param map                集合
     * @param key                键
     * @param value              值
     * @param updateValueFactory 集合中已存在该键值对时执行的方法，参数为集合中的键值对，此方法应返回要更新的值
     * @param <K>                键的类型
     * @param <V>                值的类型
     */
    @CanIgnoreReturnValue
    public static <K, V> Map<K, V> addOrUpdate(Map<K, V> map,
                                               K key,
                                               V value,
                                               Function<Tuple2<K, V>, V> updateValueFactory) {
        if (map.containsKey(key)) {
            map.put(key,
                    updateValueFactory.apply(new Tuple2<>(key,
                                                          map.get(key))));
        } else {
            map.put(key,
                    value);
        }
        return map;
    }

    /**
     * 新增或更新
     *
     * @param dic                集合
     * @param key                键
     * @param value              值
     * @param updateValueFactory 集合中已存在该键值对时执行的方法，参数为集合中的键值对，此方法应返回要更新的值
     * @param <K>                键的类型
     * @param <V>                值的类型
     */
    @CanIgnoreReturnValue
    public static <K, V> Hashtable<K, V> addOrUpdate(Hashtable<K, V> dic,
                                                     K key,
                                                     V value,
                                                     Function<Tuple2<K, V>, V> updateValueFactory) {
        if (dic.containsKey(key)) {
            dic.put(key,
                    updateValueFactory.apply(new Tuple2<>(key,
                                                          dic.get(key))));
        } else {
            dic.put(key,
                    value);
        }
        return dic;
    }

    /**
     * 获取两个集合的并集
     *
     * @param first  第一个集合
     * @param second 第二个集合
     * @param <T>    元素类型
     * @return 两个集合的并集
     */
    public static <T> List<T> union(Collection<T> first,
                                    Collection<T> second) {
        return concat(first,
                      second,
                      true);
    }

    /**
     * 获取两个集合的并集
     * <p>关于返回Object数组得原因: 由于java的泛型是用擦除法实现的，所以运行时无法获取泛型的类型信息，也就无法创建泛型数组。</p>
     *
     * @param first  第一个集合
     * @param second 第二个集合
     * @param <T>    元素类型
     * @return 两个集合的并集
     */
    public static <T> Object[] union(T[] first,
                                     T[] second) {
        return new ArrayList<>(unionAndToList(first,
                                              second)).toArray();
    }

    /**
     * 获取两个集合的并集
     *
     * @param first  第一个集合
     * @param second 第二个集合
     * @param <T>    元素类型
     * @return 两个集合的并集
     */
    public static <T> List<T> unionAndToList(T[] first,
                                             T[] second) {
        return concatArrayToList(first,
                                 second,
                                 true);
    }

    /**
     * 合并两个集合
     *
     * @param first    第一个集合
     * @param second   第二个集合
     * @param distinct 去重
     * @param <T>      元素类型
     * @return 两个集合的并集
     */
    public static <T> List<T> concat(Collection<T> first,
                                     Collection<T> second,
                                     boolean distinct) {
        List<T> result = first.parallelStream()
                              .collect(Collectors.toList());
        result.addAll(second.parallelStream()
                            .collect(Collectors.toList()));
        if (distinct)
            return result.stream()
                         .distinct()
                         .collect(Collectors.toList());
        return result;
    }

    /**
     * 获取两个集合的并集
     * <p>关于返回Object数组得原因: 由于java的泛型是用擦除法实现的，所以运行时无法获取泛型的类型信息，也就无法创建泛型数组。</p>
     *
     * @param first  第一个集合
     * @param second 第二个集合
     * @param <T>    元素类型
     * @return 两个集合的并集
     */
    public static <T> Object[] concatArray(T[] first,
                                           T[] second,
                                           boolean distinct) {
        return new ArrayList<>(concatArrayToList(first,
                                                 second,
                                                 distinct)).toArray();
    }

    /**
     * 获取两个集合的并集
     *
     * @param first    第一个集合
     * @param second   第二个集合
     * @param distinct 去重
     * @param <T>      元素类型
     * @return 两个集合的并集
     */
    public static <T> List<T> concatArrayToList(T[] first,
                                                T[] second,
                                                boolean distinct) {
        List<T> result = new ArrayList<>(Arrays.asList(first));
        result.addAll(new ArrayList<>(Arrays.asList(second)));
        if (distinct)
            return result.stream()
                         .distinct()
                         .collect(Collectors.toList());
        return result;
    }

    /**
     * 获取两个集合的交集
     *
     * @param first  第一个集合
     * @param second 第二个集合
     * @param <T>    元素类型
     * @return 两个集合的交集
     */
    public static <T> List<T> intersection(List<T> first,
                                           List<T> second) {
        return first.stream()
                    .filter(second::contains)
                    .collect(Collectors.toList());
    }

    /**
     * 获取两个集合的差集
     *
     * @param first  第一个集合
     * @param second 第二个集合
     * @param <T>    元素类型
     * @return 两个集合的差集
     */
    public static <T> List<T> except(List<T> first,
                                     List<T> second) {
        return first.stream()
                    .filter(x -> !second.contains(x))
                    .collect(Collectors.toList());
    }

    /**
     * 获取两个集合互相的差集的并集
     *
     * @param first  第一个集合
     * @param second 第二个集合
     * @param <T>    元素类型
     * @return 两个集合互相的差集的并集
     */
    public static <T> List<T> difference(List<T> first,
                                         List<T> second) {
        return concat(except(first,
                             second),
                      except(second,
                             first),
                      false);
    }

    /**
     * 获取值
     *
     * @param map 集合
     * @param key 键
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return a: 是否存在，b: 值，不存在时返回null
     */
    public static <K, V> Tuple2<Boolean, V> tryGet(Map<K, V> map,
                                                   K key) {
        return tryGet(map,
                      key,
                      null);
    }

    /**
     * 获取值
     *
     * @param map          集合
     * @param key          键
     * @param defaultValue 不存在时返回的默认值
     * @param <K>          键的类型
     * @param <V>          值的类型
     * @return a: 是否存在，b: 值
     */
    public static <K, V> Tuple2<Boolean, V> tryGet(Map<K, V> map,
                                                   K key,
                                                   V defaultValue) {
        if (map.containsKey(key))
            return new Tuple2<>(true,
                                map.get(key));
        else
            return new Tuple2<>(false,
                                defaultValue);
    }

    /**
     * 删除哈希表中的键在此集合中的数据
     *
     * @param map  哈希表
     * @param keys 键集合
     * @param <K>  键类型
     * @param <V>  值类型
     * @return 哈希表
     */
    @CanIgnoreReturnValue
    public static <K, V> Map<K, V> tryRemove(Map<K, V> map,
                                             Collection<K> keys) {
        keys.forEach(map::remove);
        return map;
    }

    /**
     * 删除哈希表中的值在此集合中的数据
     *
     * @param map    哈希表
     * @param values 值集合
     * @param <K>    键类型
     * @param <V>    值类型
     * @return 哈希表
     */
    @CanIgnoreReturnValue
    public static <K, V> Map<K, V> tryRemoveIfValueIn(Map<K, V> map,
                                                      Collection<V> values) {
        List<K> removeKeys = new ArrayList<>();
        for (K key : map.keySet()) {
            if (values.contains(map.get(key)))
                removeKeys.add(key);
        }
        removeKeys.forEach(map::remove);
        return map;
    }

    /**
     * 删除哈希表中的键不在此集合中的数据
     *
     * @param map  哈希表
     * @param keys 键集合
     * @param <K>  键类型
     * @param <V>  值类型
     * @return 哈希表
     */
    @CanIgnoreReturnValue
    public static <K, V> Map<K, V> tryRemoveIfKeyNotIn(Map<K, V> map,
                                                       Collection<K> keys) {
        List<K> removeKeys = new ArrayList<>();
        for (K key : map.keySet()) {
            if (!keys.contains(key))
                removeKeys.add(key);
        }
        removeKeys.forEach(map::remove);
        return map;
    }

    /**
     * 删除哈希表中的值不在此集合中的数据
     *
     * @param map    哈希表
     * @param values 值集合
     * @param <K>    键类型
     * @param <V>    值类型
     * @return 哈希表
     */
    @CanIgnoreReturnValue
    public static <K, V> Map<K, V> tryRemoveIfValueNotIn(Map<K, V> map,
                                                         Collection<V> values) {
        List<K> removeKeys = new ArrayList<>();
        for (K key : map.keySet()) {
            if (!values.contains(map.get(key)))
                removeKeys.add(key);
        }
        removeKeys.forEach(map::remove);
        return map;
    }

    /**
     * 填充内容
     *
     * @param list  集合
     * @param value 值
     * @param <T>   数据类型
     * @return 填充后的集合
     */
    @CanIgnoreReturnValue
    public static <T> List<T> fill(List<T> list,
                                   T value) {
        Collections.fill(list,
                         value);
        return list;
    }


    /**
     * 填充内容
     *
     * @param array 数组
     * @param value 值
     * @param <T>   数据类型
     * @return 填充后的数组
     */
    @CanIgnoreReturnValue
    public static <T> T[] fill(T[] array,
                               T value) {
        Arrays.fill(array,
                    value);
        return array;
    }

    /**
     * 集合中的首个元素
     *
     * @param list 集合
     * @param <T>  元素类型
     * @return 元素，不存在时为null
     */
    public static <T> T firstOrDefault(List<T> list) {
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }
}
