package project.extension.wechat.config;

import lombok.Data;

/**
 * 商户号配置
 *
 * @author LCTR
 * @date 2023-03-15
 */
@Data
public class PayConfig {
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
     * 公众号标识
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户API秘钥
     */
    private String mchKey;

    /**
     * 服务商模式下的子商户公众号标识
     */
    private String subAppId;

    /**
     * 服务商模式下的子商户号
     */
    private String subMchId;

    /**
     * p12证书文件的绝对路径或者以classpath:开头的类路径
     */
    private String certFilePath;

    /**
     * 证书密码
     * <p>一般默认为商户号</p>
     */
    private String certPassword;

    /**
     * 微信支付分serviceId
     */
    private String serviceId;

    /**
     * V3接口秘钥
     */
    private String apiV3Key;

    /**
     * V3证书序列号
     */
    private String certSerialNo;

    /**
     * apiclient_key.pem证书文件的绝对路径或者以classpath:开头的类路径
     */
    private String privateKeyPath;

    /**
     * apiclient_cert.pem证书文件的绝对路径或者以classpath:开头的类路径
     */
    private String privateCertPath;

    /**
     * 启用微信收付通通知服务
     *
     * @默认值 true
     */
    private Boolean enablePayNotifyServlet = true;

    /**
     * 接收收付通通知的URL
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    private String payNotifyUrl;

    /**
     * 接收收付通通知的URL
     * <p>V3版本</p>
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    private String payNotifyV3Url;

    /**
     * 接收微信支付分的URL
     * <p>V3版本</p>
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    private String payScoreNotifyV3Url;

    /**
     * 接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数。
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    private String refundNotifyUrl;

    /**
     * 接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数。
     * <p>V3版本</p>
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    private String refundNotifyV3Url;
}
