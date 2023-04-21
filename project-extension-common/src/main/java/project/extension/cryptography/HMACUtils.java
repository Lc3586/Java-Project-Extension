package project.extension.cryptography;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/***
 * HMAC加解密工具类
 *
 * @author LCTR
 * @date 2022-06-15
 **/
public class HMACUtils {
    /**
     * 生成 HMAC MD5
     *
     * @param text   明文
     * @param secret 密钥
     * @return 密文
     */
    public static String HMAC_MD5(String text,
                                  byte[] secret)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacMD5",
                    false,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC MD5
     *
     * @param text   明文
     * @param secret 密钥
     * @param upper  密文全大写
     * @return 密文
     */
    public static String HMAC_MD5(String text,
                                  byte[] secret,
                                  boolean upper)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacMD5",
                    upper,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA1
     *
     * @param text   明文
     * @param secret 密钥
     * @return 密文
     */
    public static String HMAC_SHA1(String text,
                                   byte[] secret)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA1",
                    false,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA1
     *
     * @param text   明文
     * @param secret 密钥
     * @param upper  密文全大写
     * @return 密文
     */
    public static String HMAC_SHA1(String text,
                                   byte[] secret,
                                   boolean upper)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA1",
                    upper,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA224
     *
     * @param text   明文
     * @param secret 密钥
     * @return 密文
     */
    public static String HMAC_SHA224(String text,
                                     byte[] secret)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA224",
                    false,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA224
     *
     * @param text   明文
     * @param secret 密钥
     * @param upper  密文全大写
     * @return 密文
     */
    public static String HMAC_SHA224(String text,
                                     byte[] secret,
                                     boolean upper)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA224",
                    upper,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA256
     *
     * @param text   明文
     * @param secret 密钥
     * @return 密文
     */
    public static String HMAC_SHA256(String text,
                                     byte[] secret)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA256",
                    false,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA256
     *
     * @param text   明文
     * @param secret 密钥
     * @param upper  密文全大写
     * @return 密文
     */
    public static String HMAC_SHA256(String text,
                                     byte[] secret,
                                     boolean upper)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA256",
                    upper,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA384
     *
     * @param text   明文
     * @param secret 密钥
     * @return 密文
     */
    public static String HMAC_SHA384(String text,
                                     byte[] secret)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA384",
                    false,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA384
     *
     * @param text   明文
     * @param secret 密钥
     * @param upper  密文全大写
     * @return 密文
     */
    public static String HMAC_SHA384(String text,
                                     byte[] secret,
                                     boolean upper)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA384",
                    upper,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA512
     *
     * @param text   明文
     * @param secret 密钥
     * @return 密文
     */
    public static String HMAC_SHA512(String text,
                                     byte[] secret)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA512",
                    false,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMAC SHA512
     *
     * @param text   明文
     * @param secret 密钥
     * @param upper  密文全大写
     * @return 密文
     */
    public static String HMAC_SHA512(String text,
                                     byte[] secret,
                                     boolean upper)
            throws
            Exception {
        return HMAC(text,
                    secret,
                    "HmacSHA512",
                    upper,
                    StandardCharsets.UTF_8.name());
    }

    /**
     * 生成HMAC指定算法的摘要
     *
     * @param text        明文
     * @param secret      密钥
     * @param algorithm   算法
     * @param charsetName 编码
     * @param upper       密文全大写
     * @return 密文
     */
    public static String HMAC(String text,
                              byte[] secret,
                              String algorithm,
                              boolean upper,
                              String charsetName)
            throws
            Exception {
        Mac sha256_HMAC = Mac.getInstance(algorithm);
        SecretKeySpec secret_key = new SecretKeySpec(secret,
                                                     algorithm);
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(text.getBytes(charsetName));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100),
                      1,
                      3);
        }
        if (upper)
            return sb.toString()
                     .toUpperCase();
        else
            return sb.toString();
    }
}
