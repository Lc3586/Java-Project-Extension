package project.extension.mybatis.edge.core.mapper;

import org.apache.ibatis.session.Configuration;
import project.extension.Identity.SnowFlake;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.standard.exception.ModuleException;

import java.lang.reflect.Field;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * MappedStatement id管理类
 *
 * @author LCTR
 * @date 2023-05-31
 */
@SuppressWarnings("rawtypes")
public class MappedStatementIdManager {
    /**
     * 当前使用量
     */
    public static int used = 0;

    /**
     * 附属id映射表
     */
    private static final ConcurrentHashMap<String, Queue<String>> subIdMap = new ConcurrentHashMap<>();

    private static final SnowFlake keyGenerate = new SnowFlake(1,
                                                               1);

    private static final ConcurrentHashMap<String, ConcurrentHashMap> configMap = new ConcurrentHashMap<>();

    /**
     * 获取Configuration种存储指定数据的对象
     *
     * @param name          字段名
     * @param configuration 配置
     */
    private static ConcurrentHashMap getConfigurationMapField(String name,
                                                              Configuration configuration) {
        Class<?> type = configuration.getClass();

        try {
            if (!configMap.containsKey(name)) {
                Field field = type.getDeclaredField(name);
                field.setAccessible(true);
                configMap.put(name,
                              (ConcurrentHashMap) field.get(configuration));
            }

            return configMap.get(name);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getGetObjectFieldValueFailed(type.getTypeName(),
                                                                           name),
                                      ex);
        }
    }

    /**
     * 申请一个id
     */
    public static synchronized String applyId() {
        used++;
        return keyGenerate.nextId2String();
    }

    /**
     * 申请附属id
     *
     * @param id 主id
     */
    public static synchronized String applySubId(String id) {
        String subId = applyId();

        if (!subIdMap.containsKey(id))
            subIdMap.put(id,
                         new ConcurrentLinkedDeque<>());

        subIdMap.get(id)
                .add(subId);

        return subId;
    }

    /**
     * 归还id
     * <p>同时会一并归还相关的附属id</p>
     */
    public static synchronized void returnId(String id,
                                             Configuration configuration) {
        used--;

        if (subIdMap.containsKey(id)) {
            //归还附属id
            while (!subIdMap.get(id)
                            .isEmpty()) {
                returnId(subIdMap.get(id)
                                 .poll(),
                         configuration);
            }

            subIdMap.remove(id);
        }

        getConfigurationMapField("mappedStatements",
                                 configuration).remove(id);
        getConfigurationMapField("caches",
                                 configuration).remove(id);
        getConfigurationMapField("resultMaps",
                                 configuration).remove(id);
        getConfigurationMapField("parameterMaps",
                                 configuration).remove(id);
        getConfigurationMapField("keyGenerators",
                                 configuration).remove(id);
    }
}
