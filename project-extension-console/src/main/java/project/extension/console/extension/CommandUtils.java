package project.extension.console.extension;

import project.extension.string.StringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 命令工具
 *
 * @author LCTR
 * @date 2023-05-15
 */
public class CommandUtils {
    /**
     * 获取参数集合
     * <p>示例：</p>
     * <p>输入：action "123abc" "456789" env</p>
     * <p>返回：["action", "123abc", "456789", "env"]</p>
     *
     * @param input 输入
     * @return 参数集合
     */
    public static String[] getArgs(String input) {
        String regExp = "\"(\\\\\"|[^\"])*?\"|[^ ]+";
        Pattern pattern = Pattern.compile(regExp,
                                          Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input.trim());
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(StringExtension.trimBoth(matcher.group(),
                                                 "\""));
        }
        return matches.toArray(new String[]{});
    }
}
