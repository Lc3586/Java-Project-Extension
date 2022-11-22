package project.extension.file;

/**
 * 程序版本信息
 *
 * @author LCTR
 * @date 2022-04-08
 */
public class ProgramVersionInfo {
    private String version;

    private String copyright;

    private String compiler_Ident;

    private String configuration;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCompiler_Ident() {
        return compiler_Ident;
    }

    public void setCompiler_Ident(String compiler_Ident) {
        this.compiler_Ident = compiler_Ident;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
