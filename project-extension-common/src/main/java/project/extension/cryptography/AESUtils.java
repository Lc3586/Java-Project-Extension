package project.extension.cryptography;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * @version V1.0 *
 * @desc AES 加密工具类
 **/
public class AESUtils {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    //默认的加密算法

    /***
     * AES 加密操作
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     **/
    public static String encrypt(String content, String password) {
        return encrypt(content, password, Charset.defaultCharset());
    }

    /***
     * AES 加密操作
     * @param content 待加密内容
     * @param password 加密密码
     * @param charset 字符集
     * @return 返回Base64转码后的加密数据
     **/
    public static String encrypt(String content, String password, Charset charset) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 创建密码器
            byte[] byteContent = content.getBytes(charset);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password, charset));
            // 初始 化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);
            // 加密
            return Base64Utils.encodeBase64(result, charset);
            //通过Base64转码返回
        } catch (Exception ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /***
     * AES 解密操作
     * @param content 密文
     * @param password 密码
     * @return 明文
     **/
    public static String decrypt(String content, String password) throws Exception {
        return decrypt(content, password, Charset.defaultCharset());
    }

    /***
     * AES 解密操作
     * @param content 密文
     * @param password 密码
     * @param charset 字符集
     * @return 明文
     **/
    public static String decrypt(String content, String password, Charset charset) throws Exception {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password, charset));
            //执行操作
            byte[] result = cipher.doFinal(Base64Utils.decodeBase64(content, charset));
            return new String(result, charset);
        } catch (Exception ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    /***
     * 生成加密秘钥
     * @param password 密码
     * @return 密钥
     **/
    private static SecretKeySpec getSecretKey(String password) {
        return getSecretKey(password, Charset.defaultCharset());
    }

    /***
     * 生成加密秘钥
     * @param password 密码
     * @param charset 字符集
     * @return 密钥
     **/
    private static SecretKeySpec getSecretKey(String password, Charset charset) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            //原始代码
//          kg.init(128, new SecureRandom(password.getBytes()));
            //修改后的
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes(charset));
            kg.init(128, secureRandom);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
            // 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}