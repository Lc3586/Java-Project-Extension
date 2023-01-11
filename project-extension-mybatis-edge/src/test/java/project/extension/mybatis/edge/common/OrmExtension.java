package project.extension.mybatis.edge.common;

import org.junit.jupiter.api.Assertions;
import project.extension.mybatis.edge.entity.CommonQuickInput;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Orm扩展方法
 *
 * @author LCTR
 * @date 2023-01-11
 */
public class OrmExtension {
    /**
     * 清理数据
     *
     * @param data 数据
     */
    public static <T> void clean(Class<T> type,
                                 T... data)
            throws
            Exception {
        int rowsDelete = OrmInjection.masterNaiveSql.batchDelete(type,
                                                                 Arrays.asList(data))
                                                    .executeAffrows();

        Assertions.assertEquals(data.length,
                                rowsDelete,
                                "清理数据失败");

        Field idField = type.getDeclaredField("id");

        idField.setAccessible(true);

        for (T item : data) {
            System.out.printf("\r\n已清理数据，Id：%s\r\n",
                              idField.get(item));
        }
    }
}
