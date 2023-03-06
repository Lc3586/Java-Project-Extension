package project.extension.cryptography;

import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.util.StringUtils;
import project.extension.ioc.IOCExtension;

import java.util.regex.Pattern;

/**
 * Jasypt加解密工具
 *
 * @author LCTR
 * @date 2022-11-03
 */
public class JasyptUtils {
    /**
     * 获取密码器
     *
     * @param password 密码
     * @return 密码器
     */
    private static BasicTextEncryptor getBasicTextEncryptor(String password) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(password);
        return encryptor;
    }

    /**
     * 获取密码
     * <p>1、从启动命令中获取：-Djasypt.encryptor.password=密码</p>
     * <p>2、从系统配置中获取：jasypt.encryptor.password=密码</p>
     * <p>3、从环境变量中获取：JAVA_JASYPT_PASSWORD=密码</p>
     *
     * @return 密码
     */
    public static String getPassword() {
        String property = System.getProperty("jasypt.encryptor.password");
        if (StringUtils.hasText(property))
            return property;

        if (IOCExtension.applicationContext != null) {
            String envProperty = IOCExtension.applicationContext.getEnvironment()
                                                                .getProperty("jasypt.encryptor.password");
            if (StringUtils.hasText(envProperty))
                return envProperty;
        }

        return getPasswordFromEnv("JAVA_JASYPT_PASSWORD");
    }

    /**
     * 从环境变量中获取密码
     *
     * @return 密码
     */
    public static String getPasswordFromEnv(String env) {
        return System.getenv(env);
    }

    /**
     * 加密
     *
     * @param plainTextData 明文
     * @param password      密码
     * @return 密文
     */
    public static String encrypt(String plainTextData,
                                 String password) {
        return getBasicTextEncryptor(password).encrypt(plainTextData);
    }

    /**
     * 解密
     *
     * @param cipherData 密文
     * @param password   密码
     * @return 明文
     */
    public static String decrypt(String cipherData,
                                 String password)
            throws
            Exception {
        String cipherDataFull = cipherData;
        if (!Pattern.compile("ENC\\((.*?)\\)")
                    .matcher(cipherDataFull)
                    .find())
            cipherDataFull = String.format("ENC(%s)",
                                           cipherData);

        if (!PropertyValueEncryptionUtils.isEncryptedValue(cipherDataFull))
            throw new Exception("给定的cipherData不是密文");

        return PropertyValueEncryptionUtils.decrypt(cipherDataFull,
                                                    getBasicTextEncryptor(password));
    }
}
