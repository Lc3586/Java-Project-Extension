package project.extension.wechat.config;

import lombok.Data;

/**
 * 公众号配置
 *
 * @author LCTR
 * @date 2023-03-15
 */
@Data
public class MpConfig {
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
     * 启用微信终端服务
     *
     * @默认值 true
     */
    private Boolean enableMpEndpointServlet = true;

    /**
     * 微信客户端终端的接口
     * <p>如果启用了微信终端服务，则必须配置此项</p>
     *
     * @see project.extension.wechat.core.mp.servlet.WeChatMpEndpointServlet
     */
    private String mpEndpointUrl;

    /**
     * 语言
     *
     * @默认值 zh_CN
     */
    private String language = "zh_CN";

    /**
     * 启用微信网页授权服务
     *
     * @默认值 true
     */
    private Boolean enableOAuth2Servlet = true;

    /**
     * 基础授权接口
     * <p>scope为snsapi_base</p>
     * <p>如果启用了微信网页授权服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.mp.servlet.WeChatOAuth2Servlet
     */
    private String oAuthBaseUrl;

    /**
     * 用户信息授权接口
     * <p>scope为snsapi_userinfo</p>
     * <p>如果启用了微信网页授权服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.mp.servlet.WeChatOAuth2Servlet
     */
    private String oAuthUserInfoUrl;
}
