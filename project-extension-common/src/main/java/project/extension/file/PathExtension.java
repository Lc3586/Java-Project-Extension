package project.extension.file;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件路径拓展方法
 *
 * @author LCTR
 * @date 2022-04-15
 */
public class PathExtension {
    /**
     * 去除文件路径后的拓展名
     *
     * @param path 文件路径
     * @return 文件路径
     */
    public static String getExtension(String path) {
        Matcher matcher = Pattern.compile("^.*\\.(.*?)$")
                                 .matcher(path);
        if (matcher.find() && StringUtils.hasText(matcher.group(1)))
            return String.format(".%s",
                                 matcher.group(1));
        return "";
    }

    /**
     * 去除文件路径后的拓展名
     *
     * @param path 文件路径
     * @return 文件路径
     */
    public static String trimExtension(String path) {
        String extension = getExtension(path);
        return trimExtension(path,
                             extension);
    }

    /**
     * 去除文件路径后的拓展名
     *
     * @param path      文件路径
     * @param extension 文件拓展名
     * @return 文件路径
     */
    public static String trimExtension(String path,
                                       String extension) {
        if (!StringUtils.hasText(extension))
            return path;

        return path.substring(0,
                              path.lastIndexOf(extension));
    }
}
