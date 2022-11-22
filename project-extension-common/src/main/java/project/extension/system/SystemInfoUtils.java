package project.extension.system;

/**
 * 系统信息工具类
 */
public class SystemInfoUtils {
    /**
     * 当前操作系统平台
     *
     * @return 操作系统平台
     */
    public static OSPlatform currentOS() {
        String os_name = System.getProperty("os.name");
        return OSPlatform.toEnum(os_name);
    }
}
