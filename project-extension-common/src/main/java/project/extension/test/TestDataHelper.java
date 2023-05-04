package project.extension.test;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 测试数据帮助类
 *
 * @author LCTR
 * @date 2023-05-04
 */
public class TestDataHelper {
    /**
     * 用于测试的字符值
     */
    private static final char[] charsValue = new char[]
            {
                    'a',
                    'b',
                    'c',
                    'd',
                    'e',
                    'f',
                    'g',
                    'h',
                    'i',
                    'j',
                    'k',
                    'l',
                    'm',
                    'n',
                    'o',
                    'p',
                    'q',
                    'r',
                    's',
                    't',
                    'u',
                    'v',
                    'w',
                    'x',
                    'y',
                    'z',
                    '1',
                    '2',
                    '3',
                    '4',
                    '5',
                    '6',
                    '7',
                    '8',
                    '9',
                    '0',
                    '!',
                    '@',
                    '$',
                    '%',
                    '^',
                    '&',
                    '*',
                    '(',
                    ')',
                    '~',
                    '`',
                    '·',
                    '！',
                    '￥',
                    '…',
                    '（',
                    '）',
                    '_',
                    '+',
                    '-',
                    '=',
                    '。',
                    '.',
                    '?',
                    '？',
                    '¿',
                    '；',
                    '’',
                    '\'',
                    '[',
                    ']',
                    '【',
                    '】',
                    '\\',
                    '|',
                    '/',
                    ',',
                    '，',
                    ':',
                    '：',
                    '{',
                    '}',
                    '▁',
                    '▂',
                    '▃',
                    '▄',
                    '▅',
                    '▆',
                    '▇',
                    '█',
                    '№',
                    '↑',
                    '→',
                    '↘',
                    '↓',
                    '↙',
                    '←',
                    '㊣',
                    '∮',
                    '♂',
                    '♀',
                    '∞',
                    'ㄨ',
                    '╬',
                    '╭',
                    '╮',
                    '╰',
                    '╱',
                    '╲',
                    '＠',
                    'ξ',
                    'ζ',
                    'ω',
                    '々',
                    '√',
                    '①',
                    '¤',
                    '★',
                    '☆',
                    '■',
                    '▓',
                    '☉',
                    '⊙',
                    '●',
                    '〇',
                    '◎',
                    '๑',
                    '♬',
                    '✿',
                    '☂',
                    '☃',
                    '☄',
                    '☒',
                    '☢',
                    '☺',
                    '☻',
                    '♣',
                    '♠',
                    '♦',
                    'ஐ',
                    'ﻬ',
                    '☎',
                    '☏',
                    '♨',
                    '㊝',
                    '♈',
                    '一',
                    '二',
                    '三',
                    '四',
                    '五',
                    '六',
                    '七',
                    '八',
                    '九',
                    '十',
                    '零',
                    '壹',
                    '贰',
                    '叁',
                    '肆',
                    '伍',
                    '陆',
                    '柒',
                    '捌',
                    '玖',
                    '拾',
                    '个',
                    '十',
                    '百',
                    '千',
                    '万',
                    '亿',
                    '兆',
                    '京',
                    '垓',
                    '秭',
                    '穰',
                    '僧',
                    '沟',
                    '涧',
                    '正',
                    '载',
                    '极',
                    '无'};

    /**
     * 生成测试数据
     */
    public static char generateCharacter() {
        return charsValue[new Random().nextInt(charsValue.length - 1)];
    }

    /**
     * 生成测试数据
     */
    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    /**
     * 生成测试数据
     */
    public static String generateString(Integer length) {
        if (length == null || length <= 0)
            length = 100000;

        char[] chars = new char[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            chars[i] = charsValue[random.nextInt(charsValue.length - 1)];
        }
        return new String(chars);
    }

    /**
     * 生成测试数据
     */
    public static boolean generateBoolean() {
        return new Random().nextBoolean();
    }

    /**
     * 生成测试数据
     */
    public static byte generateByte() {
        return new Random().nextBoolean()
               ? Byte.MAX_VALUE
               : Byte.MIN_VALUE;
    }

    /**
     * 生成测试数据
     */
    public static byte[] generateBytes(Integer size) {
        if (size == null || size <= 0)
            size = 100000;

        byte[] bytes = new byte[size];
        Random random = new Random();
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 生成测试数据
     */
    public static short generateShort() {
        return new Random().nextBoolean()
               ? Short.MAX_VALUE
               : Short.MIN_VALUE;
    }

    /**
     * 生成测试数据
     */
    public static int generateInteger() {
        return new Random().nextBoolean()
               ? Integer.MAX_VALUE
               : Integer.MIN_VALUE;
    }

    /**
     * 生成测试数据
     */
    public static long generateLong() {
        return new Random().nextBoolean()
               ? Long.MAX_VALUE
               : Long.MIN_VALUE;
    }

    /**
     * 生成测试数据
     */
    public static float generateFloat(Integer precision,
                                      Integer scale) {
        if (precision == null || precision == 0 || scale == null)
            return new Random().nextBoolean()
                   ? Float.MAX_VALUE
                   : Float.MIN_VALUE;
        else {
            char[] integer = new char[precision - scale];
            Arrays.fill(integer,
                        '9');
            char[] decimal = new char[scale];
            Arrays.fill(decimal,
                        '9');
            return new Float(String.format("%s.%s",
                                           new String(integer),
                                           scale == 0
                                           ? "0"
                                           : new String(decimal)));
        }
    }

    /**
     * 生成测试数据
     */
    public static double generateDouble(Integer precision,
                                        Integer scale) {
        if (precision == null || precision == 0 || scale == null)
            return new Random().nextBoolean()
                   ? Double.MAX_VALUE
                   : Double.MIN_VALUE;
        else {
            char[] integer = new char[precision - scale];
            Arrays.fill(integer,
                        '9');
            char[] decimal = new char[scale];
            Arrays.fill(decimal,
                        '9');
            return new Double(String.format("%s.%s",
                                            new String(integer),
                                            scale == 0
                                            ? "0"
                                            : new String(decimal)));
        }
    }

    /**
     * 生成测试数据
     */
    public static BigDecimal generateBigDecimal(Integer precision,
                                                Integer scale) {
        if (precision == null || precision == 0)
            return new BigDecimal(new Random().nextBoolean()
                                  ? Long.MAX_VALUE
                                  : Long.MIN_VALUE);
        else {
            char[] integer = new char[precision - scale];
            Arrays.fill(integer,
                        '9');
            char[] decimal = new char[scale];
            Arrays.fill(decimal,
                        '9');
            return new BigDecimal(String.format("%s.%s",
                                                new String(integer),
                                                scale == 0
                                                ? "0"
                                                : new String(decimal)));
        }
    }

    /**
     * 生成测试数据
     */
    public static Date generateDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 生成测试数据
     */
    public static Time generateTime() {
        return new Time(new java.sql.Timestamp(System.currentTimeMillis()).getTime());
    }

    /**
     * 生成测试数据
     */
    public static java.sql.Date generateSqlDate() {
        return new java.sql.Date(new java.sql.Timestamp(System.currentTimeMillis()).getTime());
    }
}
