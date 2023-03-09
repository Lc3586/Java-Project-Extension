package project.extension.standard.authentication;

import project.extension.openapi.annotations.OpenApiDescription;

import java.util.Date;
import java.util.List;

/**
 * 身份验证信息
 * <p>当前登录人信息</p>
 *
 * @author LCTR
 * @date 2022-12-08
 */
public class AuthenticationInfo {
    /**
     * 用户标识
     */
    @OpenApiDescription("用户标识")
    private String key;

    /**
     * 用户类型
     */
    @OpenApiDescription("用户类型")
    private String userType;

    /**
     * 角色类型
     */
    @OpenApiDescription("角色类型")
    private List<String> roleTypes;

    /**
     * 角色名称
     */
    @OpenApiDescription("角色名称")
    private List<String> roleNames;

    /**
     * 账号/用户名
     */
    @OpenApiDescription("账号/用户名")
    private String account;

    /**
     * 昵称
     */
    @OpenApiDescription("昵称")
    private String nickname;

    /**
     * 性别
     */
    @OpenApiDescription("性别")
    private String sex;

    /**
     * 头像
     */
    @OpenApiDescription("头像")
    private String face;

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
     * 用户类型
     */
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * 角色类型
     */
    public List<String> getRoleTypes() {
        return roleTypes;
    }

    public void setRoleTypes(List<String> roleTypes) {
        this.roleTypes = roleTypes;
    }

    /**
     * 角色名称
     */
    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    /**
     * 账号/用户名
     */
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
     * 性别
     */
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 头像
     */
    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
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
