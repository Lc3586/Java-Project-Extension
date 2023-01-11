package project.extension.mybatis.edge.common;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;

/**
 * 断言扩展方法
 *
 * @author LCTR
 * @date 2023-01-11
 */
public class AssertExtension {
    /**
     * 数据必须相等
     *
     * @param data1      数据1
     * @param data2      数据2
     * @param fieldNames 要比较的字段
     */
    public static void assertEquals(Object data1,
                                    Object data2,
                                    String... fieldNames)
            throws
            Throwable {
        Class<?> type = data1.getClass();

        for (String fieldName : fieldNames) {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);

            Assertions.assertNotEquals(null,
                                       field,
                                       String.format("%s中未找到%s字段",
                                                     type.getName(),
                                                     fieldName));

            Object value1 = field.get(data1);
            Object value2 = field.get(data2);

            Assertions.assertEquals(value1,
                                    value2,
                                    String.format("两个%s类型的数据对象中%s字段的值不相等：%s ≠ %s",
                                                  type.getName(),
                                                  fieldName,
                                                  value1,
                                                  value2));

            System.out.printf("\r\n两个%s类型的数据对象中%s字段的值相等：%s = %s\r\n",
                              type.getName(),
                              fieldName,
                              value1,
                              value2);
        }
    }
}
