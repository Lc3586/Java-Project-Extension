package project.extension.console.extension;

import org.springframework.util.StringUtils;
import project.extension.console.dto.ConsoleBackgroundColor;
import project.extension.console.dto.ConsoleFontColor;
import project.extension.console.dto.ConsoleFontStyle;
import project.extension.console.dto.ConsoleKeyInfo;
import project.extension.string.StringExtension;
import project.extension.task.TaskExtension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * 控制台工具
 *
 * @author LCTR
 * @date 2023-04-27
 */
public class ConsoleUtils {
    /**
     * 获取终端回显ANSI转义序列
     *
     * @param style           字体样式
     * @param color           字体颜色
     * @param backgroundColor 背景颜色
     */
    public static String getEchoE(ConsoleFontStyle style,
                                  ConsoleFontColor color,
                                  ConsoleBackgroundColor backgroundColor) {
        StringBuilder sb = new StringBuilder("\033[");

        boolean flag = false;

        if (!style.equals(ConsoleFontStyle.默认)) {
            sb.append(style.getIndex());
            flag = true;
        }

        if (!color.equals(ConsoleFontColor.默认)) {
            if (flag)
                sb.append(";");
            else
                flag = true;
            sb.append(color.getIndex());
        }

        if (!backgroundColor.equals(ConsoleBackgroundColor.默认)) {
            if (flag)
                sb.append(";");
            else
                flag = true;
            sb.append(backgroundColor.getIndex());
        }

        if (flag)
            sb.append("m");
        else
            sb.append("0m");

        return sb.toString();
    }

    /**
     * 控制台输出内容
     *
     * @param tip     提示
     * @param content 内容
     */
    public static void consoleWrite(String tip,
                                    Object content) {
        consoleWrite(tip,
                     content,
                     ConsoleFontStyle.加粗,
                     ConsoleFontColor.绿色,
                     ConsoleBackgroundColor.默认,
                     ConsoleFontStyle.默认,
                     ConsoleFontColor.白色,
                     ConsoleBackgroundColor.默认,
                     true,
                     1);
    }

    /**
     * 控制台输出内容
     *
     * @param tip       提示
     * @param content   内容
     * @param newLine   是否新起一行
     * @param blankLine 空行数
     */
    public static void consoleWrite(String tip,
                                    Object content,
                                    boolean newLine,
                                    int blankLine) {
        consoleWrite(tip,
                     content,
                     ConsoleFontStyle.加粗,
                     ConsoleFontColor.绿色,
                     ConsoleBackgroundColor.默认,
                     ConsoleFontStyle.默认,
                     ConsoleFontColor.白色,
                     ConsoleBackgroundColor.默认,
                     newLine,
                     blankLine);
    }

    /**
     * 控制台输出内容
     *
     * @param tip                    提示
     * @param content                内容
     * @param tipStyle               提示样式
     * @param tipColor               提示颜色
     * @param tipBackgroundColor     提示背景颜色
     * @param contentStyle           内容样式
     * @param contentColor           内容颜色
     * @param contentBackgroundColor 内容背景颜色
     * @param newLine                是否新起一行
     * @param blankLine              空行数
     */
    public static void consoleWrite(String tip,
                                    Object content,
                                    ConsoleFontStyle tipStyle,
                                    ConsoleFontColor tipColor,
                                    ConsoleBackgroundColor tipBackgroundColor,
                                    ConsoleFontStyle contentStyle,
                                    ConsoleFontColor contentColor,
                                    ConsoleBackgroundColor contentBackgroundColor,
                                    boolean newLine,
                                    int blankLine) {
        StringBuilder output = new StringBuilder();
        String contentString = content == null
                               ? null
                               : content.toString();

        if (newLine)
            output.append("\n");

        if (StringUtils.hasText(tip)) {
            output.append(String.format("%s%s : \033[0m",
                                        getEchoE(tipStyle,
                                                 tipColor,
                                                 tipBackgroundColor),
                                        tip));
        }

        if (StringUtils.hasText(contentString))
            output.append(String.format("%s%s\033[0m",
                                        getEchoE(contentStyle,
                                                 contentColor,
                                                 contentBackgroundColor),
                                        contentString));

        for (int i = 0; i < blankLine; i++) {
            output.append("\n");
        }

        if (output.length() > 0)
            System.out.print(output);
    }

    /**
     * 控制台延时输出内容
     *
     * @param tip     提示
     * @param content 内容
     * @param delay   单个字符输出的延时时间（单位为毫秒，-1表示为随机延时，20到300毫秒之间）
     * @return 异步任务
     */
    public static CompletableFuture<Void> consoleWriteAsync(String tip,
                                                            Object content,
                                                            int delay) {
        return consoleWriteAsync(tip,
                                 content,
                                 ConsoleFontStyle.加粗,
                                 ConsoleFontColor.绿色,
                                 ConsoleBackgroundColor.默认,
                                 ConsoleFontStyle.默认,
                                 ConsoleFontColor.白色,
                                 ConsoleBackgroundColor.默认,
                                 true,
                                 1,
                                 delay);
    }

    /**
     * 控制台延时输出内容
     *
     * @param tip       提示
     * @param content   内容
     * @param newLine   是否新起一行
     * @param blankLine 空行数
     * @param delay     单个字符输出的延时时间（单位为毫秒，-1表示为随机延时，20到300毫秒之间）
     * @return 异步任务
     */
    public static CompletableFuture<Void> consoleWriteAsync(String tip,
                                                            Object content,
                                                            boolean newLine,
                                                            int blankLine,
                                                            int delay) {
        return consoleWriteAsync(tip,
                                 content,
                                 ConsoleFontStyle.加粗,
                                 ConsoleFontColor.绿色,
                                 ConsoleBackgroundColor.默认,
                                 ConsoleFontStyle.默认,
                                 ConsoleFontColor.白色,
                                 ConsoleBackgroundColor.默认,
                                 newLine,
                                 blankLine,
                                 delay);
    }

    /**
     * 控制台延时输出内容
     *
     * @param tip                    提示
     * @param content                内容
     * @param tipStyle               提示样式
     * @param tipColor               提示颜色
     * @param tipBackgroundColor     提示背景颜色
     * @param contentStyle           内容样式
     * @param contentColor           内容颜色
     * @param contentBackgroundColor 内容背景颜色
     * @param newLine                是否新起一行
     * @param blankLine              空行数
     * @param delay                  单个字符输出的延时时间（单位为毫秒，-1表示为随机延时，20到300毫秒之间）
     * @return 异步任务
     */
    public static CompletableFuture<Void> consoleWriteAsync(String tip,
                                                            Object content,
                                                            ConsoleFontStyle tipStyle,
                                                            ConsoleFontColor tipColor,
                                                            ConsoleBackgroundColor tipBackgroundColor,
                                                            ConsoleFontStyle contentStyle,
                                                            ConsoleFontColor contentColor,
                                                            ConsoleBackgroundColor contentBackgroundColor,
                                                            boolean newLine,
                                                            int blankLine,
                                                            int delay) {
        return CompletableFuture.runAsync(() -> {
                                              if (delay <= 0 && delay != -1) {
                                                  consoleWrite(tip,
                                                               content,
                                                               tipStyle,
                                                               tipColor,
                                                               tipBackgroundColor,
                                                               contentStyle,
                                                               contentColor,
                                                               contentBackgroundColor,
                                                               newLine,
                                                               blankLine);
                                              } else {
                                                  consoleWrite(tip,
                                                               null,
                                                               tipStyle,
                                                               tipColor,
                                                               tipBackgroundColor,
                                                               contentStyle,
                                                               contentColor,
                                                               contentBackgroundColor,
                                                               newLine,
                                                               0);

                                                  Random random = delay == -1
                                                                  ? new Random()
                                                                  : null;

                                                  String contentString = content.toString();

                                                  for (char item : contentString.toCharArray()) {
                                                      consoleWrite(null,
                                                                   item,
                                                                   tipStyle,
                                                                   tipColor,
                                                                   tipBackgroundColor,
                                                                   contentStyle,
                                                                   contentColor,
                                                                   contentBackgroundColor,
                                                                   false,
                                                                   0);

                                                      TaskExtension.delay(random == null
                                                                          ? delay
                                                                          : random.nextInt());
                                                  }

                                                  consoleWrite(null,
                                                               null,
                                                               tipStyle,
                                                               tipColor,
                                                               tipBackgroundColor,
                                                               contentStyle,
                                                               contentColor,
                                                               contentBackgroundColor,
                                                               false,
                                                               blankLine);
                                              }
                                          },
                                          Executors.newSingleThreadExecutor());
    }

    //TODO 暂时无法实时获取控制台输入内容（用户按下任意按键后不需要按回车）
//    /**
//     * 控制台读取用户输入的信息
//     *
//     * @param tip 提示
//     * @return 输入内容
//     */
//    public static String readInput(String tip) {
//        return readInput(tip,
//                         null,
//                         true,
//                         -1,
//                         false,
//                         null);
//    }
//
//    /**
//     * 控制台读取用户输入的信息
//     *
//     * @param tip          提示
//     * @param defaultValue 默认值
//     * @return 输入内容
//     */
//    public static String readInput(String tip,
//                                   String defaultValue) {
//        return readInput(tip,
//                         defaultValue,
//                         true,
//                         -1,
//                         false,
//                         null);
//    }
//
//    /**
//     * 控制台读取用户输入的信息
//     *
//     * @param tip          提示
//     * @param defaultValue 默认值
//     * @param newLine      是否新起一行
//     * @param maxLength    最大长度
//     * @param autoConfirm  自动确认
//     * @param hiddenSymbol 输入时用来隐藏内容的字符
//     * @return 输入内容
//     */
//    public static String readInput(String tip,
//                                   String defaultValue,
//                                   boolean newLine,
//                                   int maxLength,
//                                   boolean autoConfirm,
//                                   Character hiddenSymbol) {
//        String input = "";
//        StringBuilder defaultValue4Show = new StringBuilder();
//
//        if (StringUtils.hasText(defaultValue)) {
//            input = defaultValue;
//            for (char o : defaultValue.toCharArray()) {
//                defaultValue4Show.append(hiddenSymbol != null
//                                         ? hiddenSymbol
//                                         : o);
//            }
//        }
//
//        consoleWrite(tip,
//                     defaultValue4Show.toString(),
//                     ConsoleFontStyle.加粗,
//                     ConsoleFontColor.绿色,
//                     ConsoleBackgroundColor.默认,
//                     ConsoleFontStyle.默认,
//                     ConsoleFontColor.灰色,
//                     ConsoleBackgroundColor.默认,
//                     newLine,
//                     0);
//
//        int currentIndex = defaultValue == null
//                           ? -1
//                           : defaultValue.length() - 1;
//
//        eachRead:
//        do {
//            try {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//                int read = reader.read();
//                ConsoleKeyInfo key = ConsoleKeyInfo.toEnum(read);
//                int length;
//                String lastInput;
//
//                switch (key) {
//                    case Backspace:
//                        if (currentIndex == -1)
//                            continue;
//
//                        length = getLength(input,
//                                           currentIndex);
//                        lastInput = StringExtension.substringZero(input,
//                                                                  currentIndex + 1,
//                                                                  input.length() - 1 - currentIndex);
//                        input = String.format("%s%s",
//                                              StringExtension.substringZero(input,
//                                                                            0,
//                                                                            currentIndex),
//                                              lastInput);
//
//                        consoleWrite(null,
//                                     String.format("%s%s%s%s%s%s",
//                                                   StringExtension.newString('\u0008',
//                                                                             length),
//                                                   StringExtension.newString(' ',
//                                                                             length),
//                                                   StringExtension.newString('\u0008',
//                                                                             length),
//                                                   (hiddenSymbol != null
//                                                    ? StringExtension.newString(hiddenSymbol,
//                                                                                lastInput.length())
//                                                    : lastInput),
//                                                   StringExtension.newString(' ',
//                                                                             length),
//                                                   StringExtension.newString('\u0008',
//                                                                             getLength(lastInput) + length)),
//                                     ConsoleFontStyle.加粗,
//                                     ConsoleFontColor.绿色,
//                                     ConsoleBackgroundColor.默认,
//                                     ConsoleFontStyle.默认,
//                                     ConsoleFontColor.白色,
//                                     ConsoleBackgroundColor.默认,
//                                     false,
//                                     0);
//
//                        currentIndex--;
//                        break;
//                    case LeftArrow:
//                        if (currentIndex == -1)
//                            continue;
//
//                        length = getLength(input,
//                                           currentIndex);
//                        consoleWrite(null,
//                                     StringExtension.newString('\u0008',
//                                                               length),
//                                     ConsoleFontStyle.加粗,
//                                     ConsoleFontColor.绿色,
//                                     ConsoleBackgroundColor.默认,
//                                     ConsoleFontStyle.默认,
//                                     ConsoleFontColor.白色,
//                                     ConsoleBackgroundColor.默认,
//                                     false,
//                                     0);
//
//                        currentIndex--;
//                        break;
//                    case RightArrow:
//                        if (currentIndex + 1 >= input.length())
//                            continue;
//
//                        length = getLength(input,
//                                           currentIndex + 1);
//
//                        consoleWrite(null,
//                                     String.format("\033[%d",
//                                                   length),
//                                     ConsoleFontStyle.加粗,
//                                     ConsoleFontColor.绿色,
//                                     ConsoleBackgroundColor.默认,
//                                     ConsoleFontStyle.默认,
//                                     ConsoleFontColor.白色,
//                                     ConsoleBackgroundColor.默认,
//                                     false,
//                                     0);
//
//                        currentIndex++;
//                        break;
//                    case Delete:
//                        if (currentIndex == -1 || currentIndex + 1 >= input.length())
//                            continue;
//
//                        length = getLength(input,
//                                           currentIndex + 1);
//                        lastInput = StringExtension.substringZero(input,
//                                                                  currentIndex + 2,
//                                                                  input.length() - 2 - currentIndex);
//
//                        input = String.format("%s%s",
//                                              StringExtension.substringZero(input,
//                                                                            0,
//                                                                            currentIndex + 1),
//                                              lastInput);
//                        consoleWrite(null,
//                                     String.format("%s%s%s",
//                                                   (hiddenSymbol != null
//                                                    ? StringExtension.newString(hiddenSymbol,
//                                                                                lastInput.length())
//                                                    : lastInput),
//                                                   StringExtension.newString(' ',
//                                                                             length),
//                                                   StringExtension.newString('\u0008',
//                                                                             getLength(lastInput) + length)),
//                                     ConsoleFontStyle.加粗,
//                                     ConsoleFontColor.绿色,
//                                     ConsoleBackgroundColor.默认,
//                                     ConsoleFontStyle.默认,
//                                     ConsoleFontColor.白色,
//                                     ConsoleBackgroundColor.默认,
//                                     false,
//                                     0);
//                        break;
//                    case Other:
//                        if (maxLength != -1 && currentIndex + 1 >= maxLength)
//                            continue;
//
//                        lastInput = StringExtension.substringZero(input,
//                                                                  currentIndex + 1,
//                                                                  input.length() - 1 - currentIndex);
//
//                        input = String.format("%s%s%s",
//                                              StringExtension.substringZero(input,
//                                                                            0,
//                                                                            currentIndex + 1),
//                                              (char) read,
//                                              lastInput);
//                        consoleWrite(null,
//                                     String.format("%s%s%s",
//                                                   (hiddenSymbol != null
//                                                    ? hiddenSymbol
//                                                    : (char) read),
//                                                   (hiddenSymbol != null
//                                                    ? StringExtension.newString(hiddenSymbol,
//                                                                                lastInput.length())
//                                                    : lastInput),
//                                                   StringExtension.newString('\u0008',
//                                                                             getLength(lastInput))),
//                                     ConsoleFontStyle.加粗,
//                                     ConsoleFontColor.绿色,
//                                     ConsoleBackgroundColor.默认,
//                                     ConsoleFontStyle.默认,
//                                     ConsoleFontColor.白色,
//                                     ConsoleBackgroundColor.默认,
//                                     false,
//                                     0);
//                        currentIndex++;
//                        break;
//                    case Enter:
//                        //EOF
//                        break eachRead;
//                }
//            } catch (Exception ignore) {
//
//            }
//        } while (maxLength == -1 || !autoConfirm || currentIndex + 1 < maxLength);
//
//        consoleWrite(null,
//                     null,
//                     ConsoleFontStyle.加粗,
//                     ConsoleFontColor.绿色,
//                     ConsoleBackgroundColor.默认,
//                     ConsoleFontStyle.默认,
//                     ConsoleFontColor.白色,
//                     ConsoleBackgroundColor.默认,
//                     newLine,
//                     0);
//
//        return input;
//    }
//
//    /**
//     * 控制台读取用户输入的密码
//     *
//     * @param tip 提示
//     * @return 用户输入的密码
//     */
//    public static String readPassword(String tip) {
//        return readInput(tip,
//                         null,
//                         true,
//                         -1,
//                         false,
//                         '*');
//    }
//
//    /**
//     * 控制台读取用户输入的密码
//     *
//     * @param tip          提示
//     * @param newLine      是否新起一行
//     * @param maxLength    最大长度
//     * @param autoConfirm  自动确认
//     * @param hiddenSymbol 输入时用来隐藏内容的字符
//     * @return 用户输入的密码
//     */
//    public static String readPassword(String tip,
//                                      boolean newLine,
//                                      int maxLength,
//                                      boolean autoConfirm,
//                                      char hiddenSymbol) {
//        return readInput(tip,
//                         null,
//                         newLine,
//                         maxLength,
//                         autoConfirm,
//                         hiddenSymbol);
//    }
//
//    /**
//     * 获取字符在控制台中的长度
//     *
//     * @param input 输入内容
//     * @param index 索引
//     */
//    public static int getLength(String input,
//                                int index) {
//        return ((byte) input.toCharArray()[index]) == 63
//               ? 2
//               : 1;
//    }
//
//    /**
//     * 获取字符在控制台中的长度
//     *
//     * @param input 输入内容
//     */
//    public static int getLength(String input) {
//        int result = 0;
//        for (int i = 0; i < input.length(); i++) {
//            result += getLength(input,
//                                i);
//        }
//        return result;
//    }
}
