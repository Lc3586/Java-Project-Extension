package project.extension.mybatis.extention;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSession;
import project.extension.cache.ICache;
import project.extension.cache.NaiveCache;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * 缓存拓展方法
 *
 * @author LCTR
 * @date 2022-03-26
 */
public class CacheExtension {
    /**
     * sql语句缓存
     */
    private static final ICache<String, String> sqlCache = new NaiveCache<>(new Properties(), "sql");

    /**
     * MappedStatement缓存
     */
    private static final ICache<String, MappedStatement> msCache = new NaiveCache<>(new Properties(), "ms");

    /**
     * Mapper参数缓存
     */
    private static final ICache<String, Object> mapperParametersCache = new NaiveCache<>(new Properties(), "mapperParameters");

    /**
     * 业务模型对应列名集合缓存
     */
    private static final ICache<String, Collection<String>> dtoTypeColumnsCache = new NaiveCache<>(new Properties(), "dtoTypeColumns");

    /**
     * 业务模型对应列名集合缓存
     */
    private static final ICache<String, Map<String, String>> fieldWithColumnsCache = new NaiveCache<>(new Properties(), "fieldWithColumns");

    /**
     * 线程关联的事务
     */
    private static final ICache<Long, SqlSession> threadTransactionCache = new NaiveCache<>(new Properties(), "threadTransaction");

    /**
     * 获取Sql语句
     *
     * @param key 键
     * @return Sql语句
     */
    public static String getSql(String key) {
        return sqlCache.get(key);
    }

    /**
     * 设置Sql语句
     *
     * @param key 键
     * @param sql Sql语句
     */
    public static void setSql(String key, String sql) {
        sqlCache.addOrUpdate(key, sql);
    }

    /**
     * 获取Sql语句
     *
     * @param key 键
     * @return MappedStatement
     */
    public static MappedStatement getMS(String key) {
        return msCache.get(key);
    }

    /**
     * 设置Sql语句
     *
     * @param key 键
     * @param ms  MappedStatement
     */
    public static void setMS(String key, MappedStatement ms) {
        msCache.addOrUpdate(key, ms);
    }

    /**
     * 获取Mapper参数映射
     *
     * @param key               键
     * @param executorParameter 参数
     * @return 值
     */
    public static Object getMapperParameters(String key, String executorParameter) {
        return mapperParametersCache.get(String.format("%s>%s", key, executorParameter));
    }

    /**
     * 设置Mapper参数映射
     *
     * @param key               键
     * @param executorParameter 参数
     * @param value             值
     */
    public static void setMapperParameters(String key, String executorParameter, Object value) {
        mapperParametersCache.addOrUpdate(String.format("%s>%s", key, executorParameter), value);
    }

    /**
     * 获取业务模型对应列名集合
     *
     * @param key 键
     * @return 列名集合
     */
    public static Collection<String> getDtoTypeColumns(String key) {
        return dtoTypeColumnsCache.get(key);
    }

    /**
     * 设置业务模型对应列名集合
     *
     * @param key     键
     * @param columns 列名集合
     */
    public static void setDtoTypeColumns(String key, Collection<String> columns) {
        dtoTypeColumnsCache.addOrUpdate(key, columns);
    }

    /**
     * 获取业务模型对应字段+列名集合
     *
     * @param key 键
     * @return 字段+列名集合
     */
    public static Map<String, String> getFieldWithColumns(String key) {
        return fieldWithColumnsCache.get(key);
    }

    /**
     * 设置业务模型对应字段+列名集合
     *
     * @param key              键
     * @param fieldWithColumns 字段+列名集合
     */
    public static void setFieldWithColumns(String key, Map<String, String> fieldWithColumns) {
        fieldWithColumnsCache.addOrUpdate(key, fieldWithColumns);
    }

    /**
     * 获取线程是否存在Sql会话
     *
     * @param key 键
     */
    public static boolean existThreadTransaction(Long key) {
        return threadTransactionCache.exist(key) && threadTransactionCache.get(key) != null;
    }

    /**
     * 获取线程对应Sql会话
     *
     * @param key 键
     * @return Sql会话
     */
    public static SqlSession getThreadTransaction(Long key) {
        return threadTransactionCache.get(key);
    }

    /**
     * 设置线程对应Sql会话
     *
     * @param key        键
     * @param sqlSession Sql会话
     */
    public static void setThreadTransaction(Long key, SqlSession sqlSession) {
        threadTransactionCache.addOrUpdate(key, sqlSession);
    }

    /**
     * 清空
     */
    public static void cleanUp() {
        sqlCache.cleanUp();
        msCache.cleanUp();
    }
}
