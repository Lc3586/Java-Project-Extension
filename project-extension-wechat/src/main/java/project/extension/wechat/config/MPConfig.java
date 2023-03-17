package project.extension.wechat.config;

import java.util.List;

/**
 * 公众号配置
 *
 * @author LCTR
 * @date 2023-03-15
 */
public class MPConfig {
    /**
     * 名称
     */
    private String name;

    /**
     * 启用
     *
     * @默认值 true
     */
    private boolean enable = true;

    /**
     * 公众账号标识
     */
    private String appId;

    /**
     * 公众账号密钥
     */
    private String appSecret;

    /**
     * 令牌
     */
    private String token;

    /**
     * AES加密密钥
     */
    private String aesKey;

    /**
     * 启用微信验证签名验证中间件
     *
     * @默认值 true
     */
    private Boolean enableSignatureVerificationMiddleware = true;

    /**
     * 需要进行签名验证的接口
     *
     * @see project.extension.wechat.core.mp.handler.WeChatSignatureVerificationMiddleware
     */
    private List<String> signatureVerificationUrls;

    /**
     * 令牌验证接口
     *
     * @see project.extension.wechat.core.mp.handler.WeChatSignatureVerificationMiddleware
     */
    private String tokenVerificationUrl;

    /**
     * 语言
     *
     * @默认值 zh_CN
     */
    private String language = "zh_CN";

    /**
     * 启用微信网页授权中间件
     *
     * @默认值 true
     */
    private Boolean enableOAuthMiddleware = true;

    /**
     * 基础授权接口
     * <p>scope为snsapi_base</p>
     * <p>如果启用了微信网页授权中间件，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.mp.handler.WeChatOAuthMiddleware
     */
    private String oAuthBaseUrl;

    /**
     * 用户信息授权接口
     * <p>scope为snsapi_userinfo</p>
     * <p>如果启用了微信网页授权中间件，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.mp.handler.WeChatOAuthMiddleware
     */
    private String oAuthUserInfoUrl;

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 启用
     *
     * @默认值 true
     */
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 公众账号标识
     */
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 公众账号密钥
     */
    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * 令牌
     */
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * AES加密密钥
     */
    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    /**
     * 启用微信验证签名验证中间件
     *
     * @默认值 true
     */
    public Boolean isEnableSignatureVerificationMiddleware() {
        return enableSignatureVerificationMiddleware;
    }

    public void setEnableSignatureVerificationMiddleware(Boolean enableSignatureVerificationMiddleware) {
        this.enableSignatureVerificationMiddleware = enableSignatureVerificationMiddleware;
    }

    /**
     * 需要进行签名验证的接口
     *
     * @see project.extension.wechat.core.mp.handler.WeChatSignatureVerificationMiddleware
     */
    public List<String> getSignatureVerificationUrls() {
        return signatureVerificationUrls;
    }

    public void setSignatureVerificationUrls(List<String> signatureVerificationUrls) {
        this.signatureVerificationUrls = signatureVerificationUrls;
    }

    /**
     * 令牌验证接口
     *
     * @see project.extension.wechat.core.mp.handler.WeChatSignatureVerificationMiddleware
     */
    public String getTokenVerificationUrl() {
        return tokenVerificationUrl;
    }

    public void setTokenVerificationUrl(String tokenVerificationUrl) {
        this.tokenVerificationUrl = tokenVerificationUrl;
    }

    /**
     * 语言
     *
     * @默认值 zh_CN
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 启用微信网页授权中间件
     *
     * @默认值 true
     */
    public Boolean isEnableOAuthMiddleware() {
        return enableOAuthMiddleware;
    }

    public void setEnableOAuthMiddleware(Boolean enableOAuthMiddleware) {
        this.enableOAuthMiddleware = enableOAuthMiddleware;
    }

    /**
     * 基础授权接口
     * <p>scope为snsapi_base</p>
     * <p>如果启用了微信网页授权中间件，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.mp.handler.WeChatOAuthMiddleware
     */
    public String getOAuthBaseUrl() {
        return oAuthBaseUrl;
    }

    public void setOAuthBaseUrl(String oAuthBaseUrl) {
        this.oAuthBaseUrl = oAuthBaseUrl;
    }

    /**
     * 用户信息授权接口
     * <p>scope为snsapi_userinfo</p>
     * <p>如果启用了微信网页授权中间件，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.mp.handler.WeChatOAuthMiddleware
     */
    public String getOAuthUserInfoUrl() {
        return oAuthUserInfoUrl;
    }

    public void setOAuthUserInfoUrl(String oAuthUserInfoUrl) {
        this.oAuthUserInfoUrl = oAuthUserInfoUrl;
    }
}
