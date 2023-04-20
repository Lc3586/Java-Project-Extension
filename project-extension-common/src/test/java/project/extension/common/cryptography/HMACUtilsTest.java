package project.extension.common.cryptography;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.extension.cryptography.HMACUtils;
import project.extension.cryptography.JasyptUtils;

/**
 * HMAC工具类测试
 *
 * @author LCTR
 * @date 2023-04-20
 */
@DisplayName("HMAC工具类测试")
public class HMACUtilsTest {
    /**
     * 密码
     */
    private static final String password = "1234567890";

    /**
     * 测试MD5摘要
     */
    @Test
    @DisplayName("测试MD5摘要")
    public void HMAC_MD5()
            throws
            Throwable {
        System.out.printf("密码：%s%n",
                          password);

        String text = "123456789123456789123456789123456789";
        System.out.printf("数据：%s%n",
                          text);

        //预期值
        String data = "9778675eee5ca56ecec0ce2a853c6a70";

        //实际值
        String data1 = HMACUtils.HMAC_MD5(text,
                                          password);
        System.out.printf("摘要：%s%n",
                          data1);

        Assertions.assertEquals(data,
                                data1,
                                "实际值和预期值不一致");

        System.out.println("测试已通过");
    }

    /**
     * 测试SHA1摘要
     */
    @Test
    @DisplayName("测试SHA1摘要")
    public void HMAC_SHA1()
            throws
            Throwable {
        System.out.printf("密码：%s%n",
                          password);

        String text = "123456789123456789123456789123456789";
        System.out.printf("数据：%s%n",
                          text);

        //预期值
        String data = "81cf1f1583b87a392721ab24cac24c5191656a02";

        //实际值
        String data1 = HMACUtils.HMAC_SHA1(text,
                                           password);
        System.out.printf("摘要：%s%n",
                          data1);

        Assertions.assertEquals(data,
                                data1,
                                "实际值和预期值不一致");

        System.out.println("测试已通过");
    }

    /**
     * 测试SHA224摘要
     */
    @Test
    @DisplayName("测试SHA224摘要")
    public void HMAC_SHA224()
            throws
            Throwable {
        System.out.printf("密码：%s%n",
                          password);

        String text = "123456789123456789123456789123456789";
        System.out.printf("数据：%s%n",
                          text);

        //预期值
        String data = "ccf2abc4545240c8fe4ea929933d939b7691ebef6b29ba6314e438e7";

        //实际值
        String data1 = HMACUtils.HMAC_SHA224(text,
                                             password);
        System.out.printf("摘要：%s%n",
                          data1);

        Assertions.assertEquals(data,
                                data1,
                                "实际值和预期值不一致");

        System.out.println("测试已通过");
    }

    /**
     * 测试SHA256摘要
     */
    @Test
    @DisplayName("测试SHA256摘要")
    public void HMAC_SHA256()
            throws
            Throwable {
        System.out.printf("密码：%s%n",
                          password);

        String text = "123456789123456789123456789123456789";
        System.out.printf("数据：%s%n",
                          text);

        //预期值
        String data = "9a586faba49cb3985afe5bb80411666db1cfe0f797182a8feb9a5c8a37011157";

        //实际值
        String data1 = HMACUtils.HMAC_SHA256(text,
                                             password);
        System.out.printf("摘要：%s%n",
                          data1);

        Assertions.assertEquals(data,
                                data1,
                                "实际值和预期值不一致");

        System.out.println("测试已通过");
    }

    /**
     * 测试SHA384摘要
     */
    @Test
    @DisplayName("测试SHA384摘要")
    public void HMAC_SHA384()
            throws
            Throwable {
        System.out.printf("密码：%s%n",
                          password);

        String text = "123456789123456789123456789123456789";
        System.out.printf("数据：%s%n",
                          text);

        //预期值
        String data = "8e9c5173c3386db9060b163e35ef244c6ef55f878568c76c130f2ef3fced06e91a1d537c544da8ac97dad7a9a27c12b0";

        //实际值
        String data1 = HMACUtils.HMAC_SHA384(text,
                                             password);
        System.out.printf("摘要：%s%n",
                          data1);

        Assertions.assertEquals(data,
                                data1,
                                "实际值和预期值不一致");

        System.out.println("测试已通过");
    }

    /**
     * 测试SHA512摘要
     */
    @Test
    @DisplayName("测试SHA512摘要")
    public void HMAC_SHA512()
            throws
            Throwable {
        System.out.printf("密码：%s%n",
                          password);

        String text = "123456789123456789123456789123456789";
        System.out.printf("数据：%s%n",
                          text);

        //预期值
        String data = "f3951766e38b79043cbc92cc5c862f1482d1e9f91bef799427cedf6c0f2137c3d2cba7ba9b238e0bddd50ac53a7ad7f02f76cc67add8ba729146683372cb8f5f";

        //实际值
        String data1 = HMACUtils.HMAC_SHA512(text,
                                             password);
        System.out.printf("摘要：%s%n",
                          data1);

        Assertions.assertEquals(data,
                                data1,
                                "实际值和预期值不一致");

        System.out.println("测试已通过");
    }
}
