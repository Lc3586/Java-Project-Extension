package project.extension.console.extension;

import org.springframework.util.StringUtils;
import project.extension.console.dto.ConsoleBackgroundColor;
import project.extension.console.dto.ConsoleFontColor;
import project.extension.console.dto.ConsoleFontStyle;
import project.extension.task.TaskExtension;

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
     * 控制台延时输出内容
     *
     * @param tip     提示
     * @param content 内容
     * @param delay   延时（单位为毫秒，-1表示为随机延时，20到300毫秒之间）
     * @return 异步任务
     */
    public static CompletableFuture<Void> consoleWriteAsync(String tip,
                                                            Object content,
                                                            int delay) {
        return consoleWriteAsync(tip,
                                 content,
                                 ConsoleFontStyle.加粗,
                                 ConsoleFontColor.绿色,
                                 ConsoleBackgroundColor.黑色,
                                 ConsoleFontStyle.重置,
                                 ConsoleFontColor.白色,
                                 ConsoleBackgroundColor.黑色,
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
     * @param delay     延时（单位为毫秒，-1表示为随机延时，20到300毫秒之间）
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
                                 ConsoleBackgroundColor.黑色,
                                 ConsoleFontStyle.重置,
                                 ConsoleFontColor.白色,
                                 ConsoleBackgroundColor.黑色,
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
     * @param delay                  延时（单位为毫秒，-1表示为随机延时，20到300毫秒之间）
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

    /**
     * 控制台延时输出内容
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
                     ConsoleBackgroundColor.黑色,
                     ConsoleFontStyle.重置,
                     ConsoleFontColor.白色,
                     ConsoleBackgroundColor.黑色,
                     true,
                     1);
    }

    /**
     * 控制台延时输出内容
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
                     ConsoleBackgroundColor.黑色,
                     ConsoleFontStyle.重置,
                     ConsoleFontColor.白色,
                     ConsoleBackgroundColor.黑色,
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
        String contentString = content.toString();

        if (newLine)
            output.append("\n");

        if (!StringUtils.hasText(tip))
            output.append(String.format("\033[%d;%d;%dm%s : \033[0m",
                                        tipColor.getIndex(),
                                        tipStyle.getIndex(),
                                        tipBackgroundColor.getIndex(),
                                        tip));

        if (!StringUtils.hasText(contentString))
            output.append(String.format("\033[%d;%d;%dm%s\033[0m",
                                        contentColor.getIndex(),
                                        contentStyle.getIndex(),
                                        contentBackgroundColor.getIndex(),
                                        contentString));

        for (int i = 0; i < blankLine; i++) {
            output.append("\n");
        }

        if (output.length() > 0)
            System.out.print(output);
    }

    /**
     * 控制台读取用户输入的信息
     *
     * @param tip 提示
     * @return 输入内容
     */
    public static String readInput(String tip) {
        return readInput(tip,
                         null,
                         true,
                         -1,
                         false,
                         null);
    }

    /**
     * 控制台读取用户输入的信息
     *
     * @param tip          提示
     * @param defaultValue 默认值
     * @return 输入内容
     */
    public static String readInput(String tip,
                                   String defaultValue) {
        return readInput(tip,
                         defaultValue,
                         true,
                         -1,
                         false,
                         null);
    }

    /**
     * 控制台读取用户输入的信息
     *
     * @param tip          提示
     * @param defaultValue 默认值
     * @param newLine      是否新起一行
     * @param maxLength    最大长度
     * @param autoConfirm  自动确认
     * @param hiddenSymbol 输入时用来隐藏内容的字符
     * @return 输入内容
     */
    public static String readInput(String tip,
                                   String defaultValue,
                                   boolean newLine,
                                   int maxLength,
                                   boolean autoConfirm,
                                   Character hiddenSymbol) {
        consoleWrite(tip,
                     defaultValue,
                     ConsoleFontStyle.加粗,
                     ConsoleFontColor.绿色,
                     ConsoleBackgroundColor.黑色,
                     ConsoleFontStyle.重置,
                     ConsoleFontColor.灰色,
                     ConsoleBackgroundColor.黑色,
                     newLine,
                     0);

        if (line)
            Console.Write("\n");
        if (!string.IsNullOrEmpty(tps))
            Console.Write(tps);
        var input = string.Empty;
        int currentIndex = defaultValue == null
                           ? -1
                           : defaultValue.Length - 1;
        ConsoleKeyInfo key;

        if (!string.IsNullOrEmpty(defaultValue)) {
            input = defaultValue;
            foreach(var
                    o
                    in
                    defaultValue)
            {
                Console.Write(hiddenSymbol.HasValue
                              ? hiddenSymbol.Value
                              : o);
            }
        }

        do {
            key = Console.ReadKey(true);

            if (key.Key == ConsoleKey.Backspace) {
                if (currentIndex == -1)
                    continue;

                var length = input.GetCharLength(currentIndex);
                var lastInput = input.SubstringZero(currentIndex + 1,
                                                    input.Length - 1 - currentIndex);
                input = $ "{input.SubstringZero(0, currentIndex)}{lastInput}";
                Console.Write($
                              "{new string('\u0008', length)}{new string(' ', length)}{new string('\u0008', length)}{(hiddenSymbol.HasValue ? new string(hiddenSymbol.Value, lastInput.Length) : lastInput)}{new string(' ', length)}{new string('\u0008', GetCharLength(lastInput) + length)}");
                currentIndex--;
            } else if (key.Key == ConsoleKey.LeftArrow) {
                if (currentIndex == -1)
                    continue;

                var length = input.GetCharLength(currentIndex);
                Console.Write(new string('\u0008',
                                         length));

                currentIndex--;
            } else if (key.Key == ConsoleKey.RightArrow) {
                if (currentIndex + 1 >= input.Length)
                    continue;

                var length = input.GetCharLength(currentIndex + 1);
                Console.SetCursorPosition(Console.CursorLeft + length,
                                          Console.CursorTop);

                currentIndex++;
            } else if (key.Key == ConsoleKey.Delete) {
                if (currentIndex == -1 || currentIndex + 1 >= input.Length)
                    continue;

                var length = input.GetCharLength(currentIndex + 1);
                var lastInput = input.SubstringZero(currentIndex + 2,
                                                    input.Length - 2 - currentIndex);
                input = $ "{input.SubstringZero(0, currentIndex + 1)}{lastInput}";
                Console.Write($
                              "{(hiddenSymbol.HasValue ? new string(hiddenSymbol.Value, lastInput.Length) : lastInput)}{new string(' ', length)}{new string('\u0008', GetCharLength(lastInput) + length)}");
            } else if (key.Key == ConsoleKey.Enter)
                break;
            else {
                if (maxLength != -1 && currentIndex + 1 >= maxLength)
                    continue;

                var lastInput = input.SubstringZero(currentIndex + 1,
                                                    input.Length - 1 - currentIndex);
                input = $ "{input.SubstringZero(0, currentIndex + 1)}{key.KeyChar}{lastInput}";
                Console.Write($
                              "{(hiddenSymbol.HasValue ? hiddenSymbol.Value : key.KeyChar)}{(hiddenSymbol.HasValue ? new string(hiddenSymbol.Value, lastInput.Length) : lastInput)}{new string('\u0008', GetCharLength(lastInput))}");
                currentIndex++;
            }
        } while (maxLength == -1 || !aotuConfirm || currentIndex + 1 < maxLength);
        if (line)
            Console.Write("\n");
        return input;
    }

    /// <summary>
    /// 控制台读取用户输入的密码
    /// </summary>
    /// <param name="tps">提示</param>
    /// <param name="line">是否整行</param>
    /// <param name="maxLength">最大长度（默认不限制）</param>
    /// <param name="aotuConfirm">自动确认（输入内容达到最大长度时无需再按回车键确认）</param>
    /// <param name="hiddenSymbol">输入时用来隐藏内容的字符</param>
    /// <returns></returns>
    public static string ReadPassword(string tps =null,
                                      bool line =true,
                                      int maxLength =-1,
                                      bool aotuConfirm =false,
                                      char hiddenSymbol ='*') {
        return ReadInput(tps,
                         line,
                         null,
                         maxLength,
                         aotuConfirm,
                         hiddenSymbol);
    }

    /// <summary>
    /// 截取字符串
    /// </summary>
    /// <param name="input">输入内容</param>
    /// <param name="startIndex">起始索引</param>
    /// <param name="length">长度</param>
    /// <returns></returns>
    private static string SubstringZero(this
                                        string input,
                                        int startIndex,
                                        int length) {
        return length == 0 || startIndex >= input.Length
               ? string.Empty
               : input.Substring(startIndex,
                                 length);
    }

    /// <summary>
    /// 获取字符长度
    /// </summary>
    /// <param name="input">输入内容</param>
    /// <param name="index">索引</param>
    /// <returns></returns>
    public static int GetCharLength(this
                                    string input,
                                    int index) {
        return Encoding.ASCII.GetBytes(new char[]{input[index]})[0] == 63
               ? 2
               : 1;
    }

    /// <summary>
    /// 获取字符长度
    /// </summary>
    /// <param name="input">输入内容</param>
    /// <returns></returns>
    public static int GetCharLength(this
                                    string input) {
        return Encoding.ASCII.GetBytes(input)
                             .Sum(c = > c == 63
               ? 2
               : 1);
    }
}
