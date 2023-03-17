package project.extension.wechat.configure;

import java.util.LinkedHashMap;

/**
 * 测试公众号配置
 *
 * @author LCTR
 * @date 2023-03-17
 */
public class TestMPConfigure {
    /**
     * 全部的测试数据源
     */
    private static final LinkedHashMap<String, String> multiMPTestMap = new LinkedHashMap<>();

    static {
        multiMPTestMap.put("PERSON",
                           "");
        multiMPTestMap.put("CORP",
                           "");
    }

    /**
     * 获取全部用于测试的公众号名称集合
     */
    public static String[] getMultiTestMPName() {
        return multiMPTestMap.keySet()
                             .toArray(new String[0]);
    }

    /**
     * 获取用于测试的公众号
     *
     * @param name 名称
     */
    public static String getTestMP(String name) {
        return multiMPTestMap.get(name);
    }
}
