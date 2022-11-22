package project.extension.common.cryptography;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.extension.cryptography.JasyptUtils;

/**
 * Jasypt工具类测试
 *
 * @author LCTR
 * @date 2022-11-03
 */
@DisplayName("Jasypt工具类测试")
public class JasyptUtilsTest {
    /**
     * 加密解密
     */
    @Test
    @DisplayName("加密解密")
    public void encryptAndDecrypt()
            throws
            Throwable {
        String password = JasyptUtils.getPassword();
        System.out.println(String.format("密码：%s",
                                         password));
        Assertions.assertNotEquals(null,
                                   password,
                                   "未获取到密码，请检查配置");

        String text = "123456";
        System.out.println(String.format("明文：%s",
                                         text));

        String data1 = JasyptUtils.encrypt(text,
                                           password);
        System.out.println(String.format("密文1：%s",
                                         data1));

        String textDe1 = JasyptUtils.decrypt(data1,
                                             password);
        System.out.println(String.format("解密1：%s",
                                         textDe1));
        Assertions.assertEquals(text,
                                textDe1,
                                "解密数据和原始明文数据不一致");

        String data2 = String.format("ENC(%s)",
                                     data1);
        System.out.println(String.format("密文2：%s",
                                         data2));

        String textDe2 = JasyptUtils.decrypt(data1,
                                             password);
        System.out.println(String.format("解密2：%s",
                                         textDe2));
        Assertions.assertEquals(text,
                                textDe2,
                                "解密数据和原始明文数据不一致");

        System.out.println("测试已通过");
    }
}
