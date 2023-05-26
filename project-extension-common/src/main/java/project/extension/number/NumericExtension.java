package project.extension.number;

import project.extension.tuple.Tuple2;

import java.math.BigDecimal;

/**
 * 数字类型数据拓展方法
 *
 * @author LCTR
 * @date 2022-06-08
 */
public class NumericExtension {
    /**
     * 是否为数字
     *
     * @param value 值
     */
    public static boolean isNumeric(Object value) {
        return isNumeric(value.getClass());
    }

    /**
     * 是否为数字
     * <p>参阅列表是会被认为是数字的数据类型</p>
     *
     * @param type 类型
     * @see Byte
     * @see Short
     * @see Integer
     * @see Double
     * @see Float
     * @see BigDecimal
     */
    public static boolean isNumeric(Class<?> type) {
        return type
                .equals(Byte.class)
                || type
                .equals(byte.class)
                || type
                .equals(Short.class)
                || type
                .equals(short.class)
                || type
                .equals(Integer.class)
                || type
                .equals(int.class)
                || type
                .equals(Long.class)
                || type
                .equals(long.class)
                || type
                .equals(Double.class)
                || type
                .equals(double.class)
                || type
                .equals(Float.class)
                || type
                .equals(float.class)
                || type
                .equals(BigDecimal.class);
    }

    /**
     * 尝试转换类型
     *
     * @param value 值
     * @return <是否成功, 转换后的值>
     */
    public static Tuple2<Boolean, Integer> tryParseInt(String value) {
        try {
            return new Tuple2<>(true,
                                Integer.parseInt(value));
        } catch (Exception ignore) {
            return new Tuple2<>(false,
                                null);
        }
    }

    /**
     * Long转byte数组
     *
     * @param value
     * @return
     */
    public static byte[] long2Bytes(long value) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            bytes[i] = (byte) ((value >> offset) & 0xff);
        }
        return bytes;
    }

    /**
     * byte数组转Long
     *
     * @param bytes
     * @return
     */
    public static long bytes2Long(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value <<= 8;
            value |= (bytes[i] & 0xff);
        }
        return value;
    }

    /**
     * 尝试转换类型
     *
     * @param value 值
     * @return <是否成功, 转换后的值>
     */
    public static Tuple2<Boolean, Long> tryParseLong(String value) {
        try {
            return new Tuple2<>(true,
                                Long.parseLong(value));
        } catch (Exception ignore) {
            return new Tuple2<>(false,
                                null);
        }
    }
}
