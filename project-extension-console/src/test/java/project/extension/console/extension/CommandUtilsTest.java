package project.extension.console.extension;

import org.junit.jupiter.api.*;

/**
 * 命令工具测试
 *
 * @author LCTR
 * @date 2023-05-15
 */
@DisplayName("命令工具测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommandUtilsTest {
    /**
     * 测试命令解析功能
     */
    @Test
    @DisplayName("测试命令解析功能")
    @Order(1)
    public void consoleWriteColorful() {
        String[] args = new String[]{"action",
                                     "123abc",
                                     "456789",
                                     "env"};

        String input = String.format("%s \"%s\" \"%s\" %s",
                                     args[0],
                                     args[1],
                                     args[2],
                                     args[3]);

        String[] argsGet = CommandUtils.getArgs(input);

        Assertions.assertEquals(args.length,
                                argsGet.length,
                                "参数个数错误");

        for (int i = 0; i < args.length; i++) {
            Assertions.assertEquals(args[i],
                                    argsGet[i],
                                    "参数值错误");
        }

        System.out.println("测试已通过");
    }
}
