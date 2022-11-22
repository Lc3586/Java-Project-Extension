package project.extension.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5校验方法
 *
 * @author LCTR
 * @date 2022-04-15
 */
public class MD5Utils {
    private static byte[] md5(String s) throws Exception {
        MessageDigest algorithm;
        algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(s.getBytes(StandardCharsets.UTF_8));
        byte[] messageDigest = algorithm.digest();
        return messageDigest;
    }

    private static final String toHex(byte[] hash) {
        if (hash == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s) throws Exception {
        return new String(toHex(md5(s)).getBytes(StandardCharsets.UTF_8),
                          StandardCharsets.UTF_8);
    }
}
