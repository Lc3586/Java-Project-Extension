package project.extension.common.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.extension.string.StringExtension;

/**
 * 字符串拓展方法测试
 *
 * @author LCTR
 * @date 2023-04-21
 */
@DisplayName("字符串拓展方法测试")
public class StringExtensionTest {
    /**
     * 测试十六进制数组与字符串互相转换
     */
    @Test
    @DisplayName("测试十六进制数组与字符串互相转换")
    public void hex() {
        String data = "C2AB356B56D163C92B923681BD84C9FE5AED5397B38D97F05273309833C7E69C";
        System.out.printf("十六进制字符串：%s%n",
                          data);

        byte[] bytes = StringExtension.getHexFromString(data);

        String data2 = StringExtension.toHexString(bytes,
                                                   true);
        System.out.printf("十六进制字符串（转换）：%s%n",
                          data);

        Assertions.assertEquals(data2,
                                data,
                                "转换值和原始值不一致");

        byte[] bytes2 = StringExtension.getHexFromString(data2);

        Assertions.assertArrayEquals(bytes2,
                                     bytes,
                                     "转换值和原始值不一致");

        System.out.println("测试已通过");
    }
}
