package project.extension.cryptography;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

/***
 * base64加密
 * @author : cuikefeng
 * @date : 2021/12/13 16:44
 **/
public class Base64Utils {
    private Base64Utils() {
        throw new IllegalStateException("Base64Util");
    }

    /***
     * 编码
     **/
    public static String encode(String data) {
        return new Base64().encodeToString(data.getBytes());
    }

    /***
     * 编码返回字符串
     **/
    public static String encodeBase64(byte[] key) {
        return new Base64().encodeToString(key);
    }

    /***
     * 编码返回字符串
     * @param charset 字符集
     **/
    public static String encodeBase64(byte[] key,
                                      Charset charset) {
        return key == null
               ? null
               : new String(new Base64().encode(key),
                            charset);
    }

    /***
     * 解码
     **/
    public static String decode(String data) {
        return new String(new Base64().decode(data));
    }

    /***
     * 解码
     * @param charset 字符集
     **/
    public static String decode(String data,
                                Charset charset) {
        return new String(decodeBase64(data,
                                       charset),
                          charset);
    }

    /***
     * 解码
     **/
    public static byte[] decodeBase64(String data) {
        return new Base64().decode(data);
    }

    /***
     * 解码
     * @param charset 字符集
     **/
    public static byte[] decodeBase64(String data,
                                      Charset charset) {
        return new Base64().decode(data.getBytes(charset));
    }

    /***
     * 解码
     **/
    public static byte[] decodeBase64(byte[] data) {
        return new Base64().decode(data);
    }
}