package project.extension.mybatis.edge.core.mapper;

import org.apache.ibatis.session.Configuration;
import project.extension.Identity.SnowFlake;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.standard.exception.ModuleException;

import java.lang.reflect.Field;
import java.util.HashMap;
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
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<String, Boolean>> idMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, Queue<String>> subIdMap = new ConcurrentHashMap<>();

    private static final SnowFlake keyGenerate = new SnowFlake(1,
                                                               1);

    private static final ConcurrentHashMap<String, Field> fieldMap = new ConcurrentHashMap<>();

    private static HashMap getConfigurationHashMapField(String name,
                                                        Configuration configuration) {
        Class<?> type = configuration.getClass();

        try {
            Field field;
            if (!fieldMap.containsKey(name)) {
                field = type.getDeclaredField(name);
                field.setAccessible(true);
                fieldMap.put(name,
                             field);
            } else
                field = fieldMap.get(name);

            return (HashMap) field.get(configuration);
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
        Long currentThreadId = Thread.currentThread()
                                     .getId();

        if (!idMap.containsKey(currentThreadId))
            idMap.put(currentThreadId,
                      new ConcurrentHashMap<>());

        String resultId = null;
        for (String id : idMap.get(currentThreadId)
                              .keySet()) {
            if (idMap.get(currentThreadId)
                     .get(id))
                continue;

            resultId = id;
            idMap.get(currentThreadId)
                 .put(id,
                      true);
        }

        if (resultId == null) {
            resultId = String.format("%s:%s",
                                     currentThreadId,
                                     keyGenerate.nextId2String());

            idMap.get(currentThreadId)
                 .put(resultId,
                      true);
        }

        return resultId;
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
        Long currentThreadId = Thread.currentThread()
                                     .getId();

        idMap.get(currentThreadId)
             .put(id,
                  false);

        if (subIdMap.containsKey(id)) {
            //归还附属id
            while (!subIdMap.get(id)
                            .isEmpty()) {
                returnId(subIdMap.get(id)
                                 .poll(),
                         configuration);
            }
        }

        getConfigurationHashMapField("mappedStatements",
                                     configuration).remove(id);
        getConfigurationHashMapField("caches",
                                     configuration).remove(id);
        getConfigurationHashMapField("resultMaps",
                                     configuration).remove(id);
        getConfigurationHashMapField("parameterMaps",
                                     configuration).remove(id);
        getConfigurationHashMapField("keyGenerators",
                                     configuration).remove(id);
    }
}
