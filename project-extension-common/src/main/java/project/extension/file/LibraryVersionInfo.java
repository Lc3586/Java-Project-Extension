package project.extension.file;

/**
 * 库版本信息
 *
 * @author LCTR
 * @date 2022-04-08
 */
public class LibraryVersionInfo {
    private String name;

    private Integer major;

    private Integer minor;

    private Integer micro;

    private Integer version;

    private String ident;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Integer getMicro() {
        return micro;
    }

    public void setMicro(Integer micro) {
        this.micro = micro;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
}
