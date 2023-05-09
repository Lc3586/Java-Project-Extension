package project.extension.string;

import org.springframework.web.util.UriComponentsBuilder;
import project.extension.tuple.Tuple2;
import sun.security.util.BitArray;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串拓展方法
 *
 * @author LCTR
 * @date 2022-04-02
 */
public class StringExtension {
    private static final Pattern urlPattern = Pattern.compile("^(.*?)://(.*?)$",
                                                              Pattern.CASE_INSENSITIVE);

    private static final Pattern urlQueryParameterPattern = Pattern.compile("^https?://(.*?)\\?(.*?)$",
                                                                            Pattern.CASE_INSENSITIVE);

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
     * @param upper 转大写
     * @return 十六进制字符串
     */
    public static String toHexString(byte[] bytes,
                                     boolean upper) {
        StringBuilder result = new StringBuilder(bytes.length);

        for (byte aByte : bytes) {
            String hexString = Integer.toHexString((aByte & 0xFF) | 0x100);
            result.append(upper
                          ? hexString.toUpperCase(Locale.ROOT)
                          : hexString,
                          1,
                          3);
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

    /**
     * 获取链接地址的架构和主机地址
     *
     * @param url 链接地址
     * @return a: 架构, b: 主机地址
     */
    public static Tuple2<String, String> getSchemeAndHost(String url) {
        Matcher matcher = urlPattern.matcher(url);
        if (matcher.find() && matcher.groupCount() == 2)
            return new Tuple2<>(matcher.group(1),
                                matcher.group(2));
        return null;
    }

    /**
     * 拼接链接地址
     *
     * @param scheme 架构
     * @param host   主机地址
     * @param paths  其他地址
     * @return 链接地址
     */
    public static String getUrl(String scheme,
                                String host,
                                String... paths) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                                                           .scheme(scheme)
                                                           .host(host);

        if (paths != null)
            for (String path : paths) {
                builder.path(path);
            }

        return builder.build()
                      .toString();
    }

    /**
     * 从链接地址中获取参数的值
     *
     * @param url 链接地址
     * @param key 参数名
     * @return 参数值
     */
    public static String getQueryParameter(String url,
                                           String key) {
        Matcher matcher = urlQueryParameterPattern.matcher(url);
        if (matcher.find() && matcher.groupCount() == 2) {
            String queryString = matcher.group(2);
            String[] parameters = queryString.split("&");
            for (String parameter : parameters) {
                if (parameter.startsWith(key)) {
                    if (parameter.contains("="))
                        return parameter.split("=")[1];
                    else
                        return null;
                }
            }
        }

        return null;
    }

    /**
     * 截取字符串
     *
     * @param input      输入内容
     * @param startIndex 起始索引
     * @param length     长度
     */
    public static String substringZero(String input,
                                       int startIndex,
                                       int length) {
        return length == 0 || startIndex >= input.length()
               ? ""
               : input.substring(startIndex,
                                 length);
    }

    /**
     * 创建字符串
     *
     * @param character 字符
     * @param length    长度
     */
    public static String newString(char character,
                                   int length) {
        char[] chars = new char[length];
        Arrays.fill(chars,
                    character);
        return new String(chars);
    }

    /**
     * 创建字符串
     *
     * @param value  字符串
     * @param length 长度
     */
    public static String newString(String value,
                                   int length) {
        String[] values = new String[length];
        Arrays.fill(values,
                    value);
        return String.join("",
                           values);
    }
}
