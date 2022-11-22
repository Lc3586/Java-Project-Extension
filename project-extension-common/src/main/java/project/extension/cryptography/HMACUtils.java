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
     * 生成 HMACSHA256
     *
     * @param text   明文
     * @param secret 密钥
     * @return 密文
     */
    public static String HMACSHA256(String text,
                                    String secret)
            throws
            Exception {
        return HMACSHA256(text,
                          secret,
                          false,
                          StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMACSHA256
     *
     * @param text   明文
     * @param secret 密钥
     * @param upper  密文全大写
     * @return 密文
     */
    public static String HMACSHA256(String text,
                                    String secret,
                                    boolean upper)
            throws
            Exception {
        return HMACSHA256(text,
                          secret,
                          upper,
                          StandardCharsets.UTF_8.name());
    }

    /**
     * 生成 HMACSHA256
     *
     * @param text        明文
     * @param secret      密钥
     * @param charsetName 编码
     * @param upper       密文全大写
     * @return 密文
     */
    public static String HMACSHA256(String text,
                                    String secret,
                                    boolean upper,
                                    String charsetName)
            throws
            Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(charsetName),
                                                     "HmacSHA256");
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
