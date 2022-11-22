package project.extension.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 初级缓存
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author LCTR
 * @date 2022-03-28
 */
public class NaiveCache<K, V> implements ICache<K, V> {
    private final Cache<K, V> CACHE;

    /**
     * @param properties 配置
     * @param prefix     前缀
     */
    public NaiveCache(Properties properties, String prefix) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        String maximumSize = properties.getProperty(prefix + ".maximumSize");
        if (StringUtils.hasText(maximumSize)) {
            cacheBuilder.maximumSize(Long.parseLong(maximumSize));
        } else {
            cacheBuilder.maximumSize(1000);
        }
        String expireAfterAccess = properties.getProperty(prefix + ".expireAfterAccess");
        if (StringUtils.hasText(expireAfterAccess)) {
            cacheBuilder.expireAfterAccess(Long.parseLong(expireAfterAccess), TimeUnit.MILLISECONDS);
        }
        String expireAfterWrite = properties.getProperty(prefix + ".expireAfterWrite");
        if (StringUtils.hasText(expireAfterWrite)) {
            cacheBuilder.expireAfterWrite(Long.parseLong(expireAfterWrite), TimeUnit.MILLISECONDS);
        }
        String initialCapacity = properties.getProperty(prefix + ".initialCapacity");
        if (StringUtils.hasText(initialCapacity)) {
            cacheBuilder.initialCapacity(Integer.parseInt(initialCapacity));
        }
        CACHE = cacheBuilder.build();
    }

    @Override
    public Boolean exist(K key) {
        return CACHE.asMap().containsKey(key);
    }

    @Override
    public V get(K key) {
        return CACHE.getIfPresent(key);
    }

    @Override
    public void addOrUpdate(K key, V value) {
        CACHE.put(key, value);
    }

    @Override
    public void cleanUp() {
        CACHE.cleanUp();
    }
}