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
//        String os_arch = System.getProperty("os.arch");
        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
        String realArch = arch != null && arch.endsWith("64")
                                  || wow64Arch != null && wow64Arch.endsWith("64")
                          ? "64"
                          : "32";
        return OSPlatform.toEnum(os_name,
                                 realArch);
    }
}
