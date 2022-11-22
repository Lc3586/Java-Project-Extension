package project.extension.cache;

/**
 * 缓存接口
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author LCTR
 * @date 2022-03-28
 */
public interface ICache<K, V> {
    /**
     * 是否存在
     *
     * @param key 键
     */
    Boolean exist(K key);

    /**
     * 获取
     *
     * @param key 键
     * @return 值
     */
    V get(K key);

    /**
     * 新增或更新
     *
     * @param key   键
     * @param value 值
     */
    void addOrUpdate(K key, V value);

    /**
     * 清空
     */
    void cleanUp();
}
