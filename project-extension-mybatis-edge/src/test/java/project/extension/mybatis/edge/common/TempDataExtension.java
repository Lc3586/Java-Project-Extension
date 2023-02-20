package project.extension.mybatis.edge.common;

import org.junit.jupiter.api.Assertions;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;

import java.util.*;

/**
 * 临时数据扩展方法
 *
 * @author LCTR
 * @date 2023-01-11
 */
public class TempDataExtension {
    /**
     * 各数据源的临时数据
     */
    private static final Map<String, Map<Class, Map<Object, Object>>> tempData = new HashMap<>();

    /**
     * 当前开启事务的线程
     */
    private static final Map<Long, String> threadTransactionMap = new HashMap<>();

    /**
     * 获取数据
     *
     * @param name  数据源名称
     * @param type  实体类型
     * @param first 获取首条数据
     * @param key   数据标识
     * @param <T>   实体类型
     * @return 数据
     */
    private static <T> T getData(String name,
                                 Class<T> type,
                                 Boolean first,
                                 Object key) {
        Assertions.assertTrue(tempData.containsKey(name),
                              String.format("%s数据源下没有数据",
                                            name));

        Map<Class, Map<Object, Object>> dataOfType = tempData.get(name);
        Assertions.assertTrue(dataOfType.containsKey(type),
                              String.format("%s数据源下没有%s类型的数据",
                                            name,
                                            type.getName()));

        Map<Object, Object> dataOfKey = dataOfType.get(type);

        Object data;

        if (first)
            data = CollectionsExtension.firstValue(dataOfKey);
        else
            data = dataOfKey.get(key);

        Assertions.assertNotNull(data,
                                 String.format("%s数据源下没有%s类型的key为%s的数据",
                                               name,
                                               type.getName(),
                                               key));

        return (T) data;
    }

    /**
     * 添加数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     * @param key  数据标识
     * @param data 数据
     */
    public static void putData(String name,
                               Class type,
                               Object key,
                               Object data) {
        if (!tempData.containsKey(name))
            tempData.put(name,
                         new HashMap<>());

        Map<Class, Map<Object, Object>> dataOfType = tempData.get(name);
        if (!dataOfType.containsKey(type))
            dataOfType.put(type,
                           new HashMap<>());

        dataOfType.get(type)
                  .put(key,
                       data);
    }

    /**
     * 获取第一条数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     */
    public static <T> T getFirstData(String name,
                                     Class<T> type) {
        return getData(name,
                       type,
                       true,
                       null);
    }

    /**
     * 获取数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     * @param key  数据标识
     */
    public static <T> T getData(String name,
                                Class<T> type,
                                Object key) {
        return getData(name,
                       type,
                       false,
                       key);
    }

    /**
     * 移除数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     * @param key  数据标识
     */
    public static void removeData(String name,
                                  Class type,
                                  Object key) {
        if (!tempData.containsKey(name))
            return;

        Map<Class, Map<Object, Object>> dataOfType = tempData.get(name);
        if (!dataOfType.containsKey(type))
            return;

        dataOfType.get(type)
                  .remove(key);
    }

    /**
     * 清理数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     * @param key  数据标识
     */
    public static void cleanData(String name,
                                 Class type,
                                 Object key) {
        Object data = getData(name,
                              type,
                              false,
                              key);

        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        int rowsDelete = naiveSql.delete(type,
                                         data)
                                 .executeAffrows();

        Assertions.assertNotEquals(-1,
                                   rowsDelete,
                                   "清理数据失败");

        removeData(name,
                   type,
                   key);

        System.out.printf("\r\n已清理数据，key：%s\r\n",
                          key);
    }

    /**
     * 清理全部数据
     */
    public static void clearUp() {
        for (String name : TestDataSourceConfigure.getMultiTestDataSourceName()) {
            if (!tempData.containsKey(name))
                return;

            Map<Class, Map<Object, Object>> dataOfType = tempData.get(name);

            if (dataOfType.size() == 0)
                return;

            INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

            for (Class type : dataOfType.keySet()) {
                Map<Object, Object> dataOfKey = dataOfType.get(type);

                if (dataOfKey.size() == 0)
                    return;

                List<Object> data = new ArrayList<>(dataOfKey.values());

                int rowsDelete = naiveSql.batchDelete(type,
                                                      data)
                                         .executeAffrows();

                Assertions.assertEquals(data.size(),
                                        rowsDelete,
                                        "清理数据失败");

                for (Object key : dataOfKey.keySet()) {
                    System.out.printf("\r\n已清理数据，key：%s\r\n",
                                      key);
                }
            }
        }
    }

    /**
     * 新增事务
     *
     * @param threadId 线程标识
     * @param name     数据源名称
     */
    public static void putThreadTransaction(Long threadId,
                                            String name) {
        threadTransactionMap.put(threadId,
                                 name);
    }

    /**
     * 获取事务对应的数据源名称
     *
     * @param threadId 线程标识
     * @return 数据源名称
     */
    public static String getThreadTransaction(Long threadId) {
        return threadTransactionMap.get(threadId);
    }
}
