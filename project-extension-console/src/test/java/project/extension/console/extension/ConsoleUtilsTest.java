package project.extension.console.extension;

import org.junit.jupiter.api.*;
import project.extension.console.dto.ConsoleBackgroundColor;
import project.extension.console.dto.ConsoleFontColor;
import project.extension.console.dto.ConsoleFontStyle;
import project.extension.string.StringExtension;
import project.extension.task.TaskExtension;
import project.extension.test.TestDataHelper;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * 控制台工具测试
 *
 * @author LCTR
 * @date 2023-05-04
 */
@DisplayName("控制台工具测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConsoleUtilsTest {
    /**
     * 测试控制台输出彩色内容
     */
    @Test
    @DisplayName("测试控制台输出彩色内容")
    @Order(1)
    public void consoleWriteColorful() {
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.加粗,
                                                ConsoleFontColor.默认,
                                                ConsoleBackgroundColor.默认),
                          "字体加粗");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.加粗,
                                                ConsoleFontColor.青色,
                                                ConsoleBackgroundColor.默认),
                          "青色字体加粗");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.加粗,
                                                ConsoleFontColor.青色,
                                                ConsoleBackgroundColor.黑色),
                          "黑色背景青色字体加粗");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.默认,
                                                ConsoleFontColor.绿色,
                                                ConsoleBackgroundColor.默认),
                          "绿色字体");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.默认,
                                                ConsoleFontColor.绿色,
                                                ConsoleBackgroundColor.黑色),
                          "黑色背景绿色字体");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.减弱,
                                                ConsoleFontColor.默认,
                                                ConsoleBackgroundColor.默认),
                          "字体减弱");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.斜体,
                                                ConsoleFontColor.默认,
                                                ConsoleBackgroundColor.默认),
                          "字体斜体");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.下划线,
                                                ConsoleFontColor.默认,
                                                ConsoleBackgroundColor.默认),
                          "字体下划线");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.慢速闪烁,
                                                ConsoleFontColor.默认,
                                                ConsoleBackgroundColor.默认),
                          "字体慢速闪烁");
        System.out.printf("%s%s : \033[0m\n",
                          ConsoleUtils.getEchoE(ConsoleFontStyle.快速闪烁,
                                                ConsoleFontColor.默认,
                                                ConsoleBackgroundColor.默认),
                          "字体快速闪烁");
    }

    /**
     * 测试控制台输出内容
     */
    @Test
    @DisplayName("测试控制台输出内容")
    @Order(2)
    public void consoleWrite() {
        //原始输出对象
        PrintStream oldPrintStream = System.out;

        //替换控制台输出对象
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        String tip = "测试";
        String content = TestDataHelper.generateString(100);
        String input = String.format("\n\u001B[1;32m%s : \u001B[0m\u001B[97m%s\u001B[0m\n",
                                     tip,
                                     content);
        oldPrintStream.println(input);

        ConsoleUtils.consoleWrite(tip,
                                  content);

        String output = bos.toString();
        oldPrintStream.println(output);

        Assertions.assertEquals(input,
                                output,
                                "读取值和原始值不一致");

        System.setOut(oldPrintStream);
        System.out.println("测试已通过");
    }

    /**
     * 测试控制台延时输出内容
     */
    @Test
    @DisplayName("测试控制台延时输出内容")
    @Order(3)
    public void consoleWriteAsync() {
        //原始输出对象
        PrintStream oldPrintStream = System.out;

        //替换控制台输出对象
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        String tip = "测试";
        String content = TestDataHelper.generateString(100);
        StringBuilder input = new StringBuilder(String.format("\n\u001B[1;32m%s : \u001B[0m",
                                                              tip));
        for (char _char : content.toCharArray()) {
            input.append(String.format("\033[97m%s\033[0m",
                                       _char));
        }
        input.append("\n");
        oldPrintStream.println(input);

        ConsoleUtils.consoleWriteAsync(tip,
                                       content,
                                       10);

        String output;
        int time = 0;
        while (true) {
            output = bos.toString();
            oldPrintStream.println(output);

            if (input.toString()
                     .equals(output))
                break;

            TaskExtension.delay(100);
            time += 100;
        }

        System.setOut(oldPrintStream);

        System.out.printf("用时%sms\n",
                          time);

        if (time < 1500)
            Assertions.fail("内容被提前输出");
        else if (time > 1500)
            Assertions.fail("内容输出超时");

        System.out.println("测试已通过");
    }

    /**
     * 测试控制台输入内容
     */
    @Test
    @DisplayName("测试控制台输入内容")
    @Order(4)
    public void readInput()
            throws
            IOException {
        System.out.print("6666一二");
        TaskExtension.delay(1000);
//        System.out.print(StringExtension.newString('\u0008',
//                                                   1));
//        TaskExtension.delay(1000);
//        System.out.print("\033[2D");
//        TaskExtension.delay(1000);
//        System.out.print(StringExtension.newString('\u0008',
//                                                   1));
        System.out.print("*");
        TaskExtension.delay(1000);
        System.out.print(StringExtension.newString(' ',
                                                   1));
        TaskExtension.delay(1000);
        System.out.print(StringExtension.newString('\u0008',
                                                   1));
        TaskExtension.delay(1000);

        char[] password = System.console()
                                .readPassword();

//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            String read = scanner. ();
//            System.out.println(read);
//
//            if (false)
//                break;
//        }

        BufferedInputStream inputStream = new BufferedInputStream(System.in);
        while (true) {
            char read = (char) inputStream.read();
            System.out.println(read);

            if (false)
                break;
        }

        InputStreamReader reader = new InputStreamReader(System.in);
        while (true) {
            char read = (char) reader.read();
            System.out.println(read);

            if (false)
                break;
        }

        //原始输出对象
        PrintStream oldPrintStream = System.out;

        //替换控制台输出对象
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        String tip = "请输入内容";
        String content = TestDataHelper.generateString(100);
        String input = String.format("\n\u001B[1;32m%s : \u001B[0m\u001B[37m%s\u001B[0m",
                                     tip,
                                     content);
        oldPrintStream.println(input);

        CompletableFuture.runAsync(() -> {
            TaskExtension.delay(100);

            String output = bos.toString();
            oldPrintStream.println(output);

            if (!input.equals(output))
                Assertions.fail("提示语错误");

            System.out.print(content);
            System.out.print("\n");
        });

//        String receive = ConsoleUtils.readInput(tip,
//                                                content);
//        Assertions.assertEquals(input,
//                                receive,
//                                "读取数据错误");

        System.setOut(oldPrintStream);
        System.out.println("测试已通过");
    }
}
