package project.extension.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import project.extension.action.IAction2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * JSON工具
 *
 * @author LCTR
 * @date 2023-06-02
 */
public class JSONUtils {
    static {
        JSONUtils.serializeConfig = new SerializeConfig();
        JSONUtils.objectMapper = new ObjectMapper();

        JSONUtils.readerMap = new ConcurrentHashMap<>();
        JSONUtils.typeReaderMap = new ConcurrentHashMap<>();
        JSONUtils.writerMap = new ConcurrentHashMap<>();
        JSONUtils.typeWriterMap = new ConcurrentHashMap<>();
    }

    private static SerializeConfig serializeConfig;

    private static ObjectMapper objectMapper;

    private static ConcurrentMap<Class<?>, ObjectReader> readerMap;

    private static ConcurrentMap<TypeReference<?>, ObjectReader> typeReaderMap;

    private static ConcurrentMap<Class<?>, ObjectWriter> writerMap;

    private static ConcurrentMap<TypeReference<?>, ObjectWriter> typeWriterMap;

    private static synchronized ObjectReader getReader(Class<?> type) {
        if (!readerMap.containsKey(type))
            readerMap.put(type,
                          JSONUtils.objectMapper.readerFor(type));
        return readerMap.get(type);
    }

    private static synchronized ObjectReader getReader(TypeReference<?> type) {
        if (!typeReaderMap.containsKey(type))
            typeReaderMap.put(type,
                              JSONUtils.objectMapper.readerFor(type));
        return typeReaderMap.get(type);
    }

    private static synchronized ObjectWriter getWriter(Class<?> type) {
        if (!writerMap.containsKey(type))
            writerMap.put(type,
                          JSONUtils.objectMapper.writerFor(type));
        return writerMap.get(type);
    }

    private static synchronized ObjectWriter getWriter(TypeReference<?> type) {
        if (!typeWriterMap.containsKey(type))
            typeWriterMap.put(type,
                              JSONUtils.objectMapper.writerFor(type));
        return typeWriterMap.get(type);
    }

    /**
     * 设置
     */
    public static void setup(IAction2<SerializeConfig, ObjectMapper> setup) {
        setup.invoke(JSONUtils.serializeConfig,
                     JSONUtils.objectMapper);
    }

    /**
     *
     */
    public static ObjectMapper getObjectMapper() {
        return JSONUtils.objectMapper;
    }

    /**
     * 序列化
     */
    public static String toJsonString(Object obj,
                                      Class<?> type) {
        try {
            if (obj == null)
                return null;
            return getWriter(type).writeValueAsString(obj);
        } catch (Exception ignore) {
            return JSON.toJSONString(obj,
                                     JSONUtils.serializeConfig);
        }
    }

    /**
     * 序列化
     */
    public static String toJsonString(Object obj,
                                      TypeReference<?> type) {
        try {
            if (obj == null)
                return null;
            return getWriter(type).writeValueAsString(obj);
        } catch (Exception ignore) {
            return JSON.toJSONString(obj,
                                     JSONUtils.serializeConfig);
        }
    }

    /**
     * 序列化
     */
    public static String toJsonString(Object obj) {
        try {
            if (obj == null)
                return null;
            return JSONUtils.objectMapper.writeValueAsString(obj);
        } catch (Exception ignore) {
            return JSON.toJSONString(obj,
                                     JSONUtils.serializeConfig);
        }
    }

    /**
     * 反序列化
     */
    public static <T> T parseObject(String json,
                                    Class<T> type) {
        try {
            if (json == null)
                return null;
            return getReader(type).readValue(json);
        } catch (Exception ignore) {
            return JSON.parseObject(json,
                                    type);
        }
    }

    /**
     * 反序列化
     */
    public static <T> T parseObject(String json,
                                    TypeReference<T> type) {
        try {
            if (json == null)
                return null;
            return getReader(type).readValue(json);
        } catch (Exception ignore) {
            return JSON.parseObject(json,
                                    type.getType());
        }
    }

    /**
     * 反序列化
     */
    public static JsonNode parseJsonObject(String json) {
        try {
            if (json == null)
                return null;
            return JSONUtils.objectMapper.readTree(json);
        } catch (Exception ignore) {
            return JSON.parseObject(json,
                                    JsonNode.class);
        }
    }

    /**
     * 反序列化
     */
    public static <T> T parseObject(JsonNode node,
                                    Class<T> type) {
        try {
            if (node == null)
                return null;
            return JSONUtils.objectMapper.convertValue(node,
                                                       type);
        } catch (Exception ignore) {
            return JSON.parseObject(node.toString(),
                                    type);
        }
    }

    /**
     * 反序列化
     */
    public static <T> T parseObject(JsonNode node,
                                    TypeReference<T> type) {
        try {
            if (node == null)
                return null;
            return JSONUtils.objectMapper.convertValue(node,
                                                       type);
        } catch (Exception ignore) {
            return JSON.parseObject(node.toString(),
                                    type.getType());
        }
    }

    /**
     * 创建数组
     */
    public static ObjectNode createJsonObject() {
        return JSONUtils.objectMapper.createObjectNode();
    }

    /**
     * 创建数组
     */
    public static ArrayNode createJsonArray() {
        return JSONUtils.objectMapper.createArrayNode();
    }
}
