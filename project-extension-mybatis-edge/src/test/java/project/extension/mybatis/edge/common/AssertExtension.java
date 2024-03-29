package project.extension.mybatis.edge.common;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

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
     * @param printInfo  输出信息
     * @param fieldNames 要比较的字段
     */
    public static void assertEquals(Object data1,
                                    Object data2,
                                    boolean printInfo,
                                    String... fieldNames)
            throws
            Exception {
        Class<?> type = data1.getClass();

        Field[] fields;

        if (fieldNames == null || fieldNames.length == 0)
            fields = type.getDeclaredFields();
        else {
            fields = new Field[fieldNames.length];
            for (int i = 0; i < fieldNames.length; i++) {
                Field field = type.getDeclaredField(fieldNames[i]);
                fields[i] = field;
            }
        }

        for (Field field : fields) {
            field.setAccessible(true);

            Assertions.assertNotEquals(null,
                                       field,
                                       String.format("%s中未找到%s字段",
                                                     type.getName(),
                                                     field.getName()));

            Object value1 = field.get(data1);
            Object value2 = field.get(data2);

            Class<?> fieldType = field.getType();
            if (fieldType.equals(java.sql.Date.class)
                    || fieldType.equals(java.sql.Time.class)
                    || fieldType.equals(Date.class)) {
                value1 = value1.toString();
                value2 = value2.toString();
            } else if (fieldType.equals(byte[].class)) {
                value1 = new String(Base64.getEncoder()
                                          .encode((byte[]) value1),
                                    StandardCharsets.UTF_8);
                value2 = new String(Base64.getEncoder()
                                          .encode((byte[]) value2),
                                    StandardCharsets.UTF_8);
            }

            Assertions.assertEquals(value1,
                                    value2,
                                    String.format("两个%s类型的数据对象中%s字段的值不相等：%s ≠ %s",
                                                  type.getName(),
                                                  field.getName(),
                                                  value1,
                                                  value2));

            if (!printInfo)
                return;

            String value1Output = value1 == null
                                  ? ""
                                  : value1.toString();
            String value2Output = value2 == null
                                  ? ""
                                  : value2.toString();

            if (value1Output.length() > 100)
                value1Output = value1Output.substring(0,
                                                      100) + String.format("等共计%s个字符",
                                                                           value1Output.length() - 100);
            if (value2Output.length() > 100)
                value2Output = value2Output.substring(0,
                                                      100) + String.format("等共计%s个字符",
                                                                           value2Output.length() - 100);

            System.out.printf("\r\n两个%s类型的数据对象中%s字段的值相等：%s = %s\r\n",
                              type.getName(),
                              field.getName(),
                              value1Output,
                              value2Output);
        }
    }
}
