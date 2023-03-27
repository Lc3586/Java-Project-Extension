package project.extension.wechat.extension;

import project.extension.standard.exception.ModuleException;
import project.extension.string.StringExtension;
import project.extension.tuple.Tuple2;
import project.extension.wechat.globalization.Strings;

/**
 * 工具集
 *
 * @author LCTR
 * @date 2023-03-27
 */
public class CommonUtils {
    /**
     * 拼接链接地址
     *
     * @param rootUrl 根地址
     * @param paths   其他地址
     * @return 链接地址
     */
    public static String getUrlWithRootUrl(String rootUrl,
                                           String... paths) {
        Tuple2<String, String> result = StringExtension.getSchemeAndHost(rootUrl);
        if (result == null)
            throw new ModuleException(Strings.getDataFormatNonStandard("project.extension.wechat.extension.CommonUtils.getUrl",
                                                                       rootUrl,
                                                                       "http://www.a.com, https://www.a.com"));
        return StringExtension.getUrl(result.a,
                                      result.b,
                                      paths);
    }
}
