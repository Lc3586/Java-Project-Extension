package project.extension.cryptography;

import project.extension.exception.CommonException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5校验方法
 *
 * @author LCTR
 * @date 2022-04-15
 */
public class MD5Utils {
    private static byte[] md5(String s)
            throws
            CommonException {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new CommonException(ex.getMessage(),
                                      ex);
        }
        algorithm.reset();
        algorithm.update(s.getBytes(StandardCharsets.UTF_8));
        return algorithm.digest();
    }

    private static String toHex(byte[] hash) {
        if (hash == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff,
                                     16));
        }
        return buf.toString();
    }

    public static String hash(String s) {
        return new String(toHex(md5(s)).getBytes(StandardCharsets.UTF_8),
                          StandardCharsets.UTF_8);
    }
}
