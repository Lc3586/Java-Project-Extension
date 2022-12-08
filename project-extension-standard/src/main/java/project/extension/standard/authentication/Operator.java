package project.extension.standard.authentication;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Date;

/**
 * 操作者
 * <p>一般为当前登录人</p>
 *
 * @author LCTR
 * @date 2022-12-08
 */
public class Operator {
    /**
     * 用户标识
     */
    @OpenApiDescription("用户标识")
    private String key;

    /**
     * 用户名/账号
     */
    @OpenApiDescription("用户名/账号")
    private String username;

    /**
     * 昵称
     */
    @OpenApiDescription("昵称")
    private String nickname;

    /**
     * 姓名
     */
    @OpenApiDescription("姓名")
    private String name;

    /**
     * 身份验证时间
     */
    @OpenApiDescription("身份验证时间")
    private Date authTime;

    /**
     * 身份验证方法
     */
    @OpenApiDescription("身份验证方法")
    private AuthMethod authMethod;

    /**
     * 过期时间
     */
    @OpenApiDescription("过期时间")
    private Date expireTime;

    /**
     * 主机地址
     */
    @OpenApiDescription("主机地址")
    private String ip;

    /**
     * 定位地址
     */
    @OpenApiDescription("定位地址")
    private String location;

    /**
     * 用户标识
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 用户名/账号
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 昵称
     */
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 姓名
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 身份验证时间
     */
    public Date getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    /**
     * 身份验证方法
     */
    public AuthMethod getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(AuthMethod authMethod) {
        this.authMethod = authMethod;
    }

    /**
     * 过期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 主机地址
     */
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 定位地址
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
