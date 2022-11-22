package project.extension.common.cryptography;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.extension.cryptography.RSAUtils;

/**
 * RSA工具类测试
 *
 * @author LCTR
 * @date 2022-11-03
 */
@DisplayName("RSA工具类测试")
public class RSAUtilsTest {
    /**
     * 测试临时文件根目录
     */
    private final String testTempFileDir = String.format("test-temp\\%s",
                                                         RSAUtilsTest.class.getSimpleName());

    /**
     * 加密解密
     */
    @Test
    @DisplayName("加密解密")
    public void encryptAndDecrypt()
            throws
            Throwable {
        RSAUtils.genKeyPair(testTempFileDir);

        System.out.println(String.format("公钥：%s",
                                         RSAUtils.loadPublicKeyByFile(testTempFileDir)));

        System.out.println(String.format("私钥：%s",
                                         RSAUtils.loadPrivateKeyByFile(testTempFileDir)));

        String text = "1234567890";
        System.out.println(String.format("明文：%s",
                                         text));
        String data = RSAUtils.encryptWithPrivateKeyToString(text,
                                                             RSAUtils.loadPrivateKeyByFile(testTempFileDir));
        System.out.println(String.format("密文：%s",
                                         data));
        String textDe = RSAUtils.decryptWithPublicKeyToString(data,
                                                              RSAUtils.loadPublicKeyByFile(testTempFileDir));
        System.out.println(String.format("解密：%s",
                                         textDe));
        Assertions.assertNotSame(text,
                                 textDe,
                                 "解密数据和原始明文数据不一致");

        System.out.println("测试已通过");
    }
}
