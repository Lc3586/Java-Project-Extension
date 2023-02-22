package project.extension.string;

import sun.security.util.BitArray;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;

/**
 * 字符串拓展方法
 *
 * @author LCTR
 * @date 2022-04-02
 */
public class StringExtension {
    /**
     * 判断是否相等并忽略大小写
     *
     * @param str1 字符串1
     * @param str2 字符串2
     */
    public static boolean ignoreCaseEquals(String str1,
                                           String str2) {
        return str1 == null
               ? str2 == null
               : str1.equalsIgnoreCase(str2);
    }

    /**
     * 进行MD5校验
     *
     * @param input 输入内容
     * @return md5校验值
     * @deprecated 此为不安全的哈希算法，建议使用sha256()方法
     */
    @Deprecated
    public static String md5(String input,
                             Charset charset)
            throws
            Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(input.getBytes(charset));
        return DatatypeConverter.printHexBinary(messageDigest.digest())
                                .toLowerCase(Locale.ROOT);
    }

    /**
     * 进行SHA-256校验
     *
     * @param input 输入内容
     * @return SHA-256校验值
     */
    public static String sha256(String input,
                                Charset charset)
            throws
            Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(input.getBytes(charset));
        return DatatypeConverter.printHexBinary(messageDigest.digest())
                                .toLowerCase(Locale.ROOT);
    }

    /**
     * 移除字符串尾部和指定内容相同的数据
     *
     * @param input 输入
     * @param var   指定内容
     * @return 输出
     */
    public static String trimEnd(String input,
                                 String var) {
        while (input.endsWith(var))
            input = input.substring(0,
                                    input.length() - var.length());
        return input;
    }

    /**
     * 移除字符串头部和指定内容相同的数据
     *
     * @param input 输入
     * @param var   指定内容
     * @return 输出
     */
    public static String trimStart(String input,
                                   String var) {
        while (input.startsWith(var))
            input = input.substring(var.length());
        return input;
    }

    /**
     * 将帕斯卡命名字符串转换为下划线分隔字符串
     *
     * @param value 值
     * @return 转换后的值
     */
    public static String pascalCaseToUnderScore(String value) {
        StringBuilder result = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i > 0 && Character.isUpperCase(chars[i]))
                result.append('_');
            result.append(chars[i]);
        }
        return result.toString();
    }

    /**
     * 将转换为下划线分隔字符串的帕斯卡命名的字符串还原
     *
     * @param value 值
     * @return 转换后的值
     */
    public static String underscoreToPascalCase(String value) {
        StringBuilder result = new StringBuilder();
        Arrays.stream(value.split("_"))
              .forEach(x -> {
                  result.append(x.substring(0,
                                            1)
                                 .toUpperCase(Locale.ROOT));
                  result.append(x.substring(1)
                                 .toLowerCase(Locale.ROOT));
              });
        return result.toString();
    }

    /**
     * 首字母转大写
     *
     * @param value 值
     * @return 转换后的值
     */
    public static String firstChar2UpperCase(String value) {
        return value.substring(0,
                               1)
                    .toUpperCase(Locale.ROOT) + value.substring(1);
    }

    /**
     * 首字母转小写
     *
     * @param value 值
     * @return 转换后的值
     */
    public static String firstChar2LowerCase(String value) {
        return value.substring(0,
                               1)
                    .toLowerCase(Locale.ROOT) + value.substring(1);
    }

    /**
     * 取首字母（包括下划线）
     *
     * @param value 值
     * @return 转换后的值
     */
    public static String justFirstChar(String value) {
        StringBuilder result = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0 || chars[i] == '_' || Character.isUpperCase(chars[i]))
                result.append(chars[i]);
        }
        return result.toString();
    }

    /**
     * 判断是否为中文字符
     *
     * @param _char 字符
     */
    public static boolean isChinese(char _char) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(_char);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 转为十六进制字符串
     *
     * @param bytes 数据
     * @return 十六进制字符串
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length);

        for (byte aByte : bytes) {
            String item = Integer.toHexString(0xFF & aByte);
            if (item.length() < 2)
                result.append(0);
            result.append(item.toUpperCase());
        }

        return result.toString();
    }

    /**
     * 获取十六进制数据
     *
     * @param data 十六进制字符串
     * @return 十六进制数据
     */
    public static byte[] getHexFromString(String data) {
        int length = (data.length() / 2);
        byte[] result = new byte[length];
        char[] chars = data.toCharArray();
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            result[i] = (byte) (getHexFromChar(chars[pos]) << 4 | getHexFromChar(chars[pos + 1]));
        }
        return result;
    }

    /**
     * 获取十六进制数据
     *
     * @param c 字符
     * @return 十六进制数据
     */
    public static byte getHexFromChar(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 将BitArray转为1010101010这样的二进制字符串
     */
    public static String ToBitString(BitArray bitArray) {
        char[] chars = new char[bitArray.length()];
        for (int a = 0; a < chars.length; a++)
            chars[a] = bitArray.get(a)
                       ? '1'
                       : '0';
        return new String(chars);
    }

    /**
     * 将1010101010这样的二进制字符串转换成BitArray
     *
     * @param bitString 二进制字符串
     */
    public static BitArray ToBitArray(String bitString) {
        if (bitString == null) return null;
        char[] chars = bitString.toCharArray();
        BitArray ret = new BitArray(chars.length);
        for (int a = 0; a < chars.length; a++)
            ret.set(a,
                    chars[a] == '1');
        return ret;
    }
}
