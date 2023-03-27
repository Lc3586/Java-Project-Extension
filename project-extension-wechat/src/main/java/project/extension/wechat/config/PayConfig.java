package project.extension.wechat.config;

/**
 * 商户号配置
 *
 * @author LCTR
 * @date 2023-03-15
 */
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
     * 公众号标识
     */
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 商户号
     */
    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    /**
     * 商户API秘钥
     */
    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    /**
     * 服务商模式下的子商户公众号标识
     */
    public String getSubAppId() {
        return subAppId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
    }

    /**
     * 服务商模式下的子商户号
     */
    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }


    /**
     * p12证书文件的绝对路径或者以classpath:开头的类路径
     */
    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    /**
     * 证书密码
     * <p>一般默认为商户号</p>
     */
    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    /**
     * 微信支付分serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * V3接口秘钥
     */
    public String getApiV3Key() {
        return apiV3Key;
    }

    public void setApiV3Key(String apiV3Key) {
        this.apiV3Key = apiV3Key;
    }

    /**
     * V3证书序列号
     */
    public String getCertSerialNo() {
        return certSerialNo;
    }

    public void setCertSerialNo(String certSerialNo) {
        this.certSerialNo = certSerialNo;
    }

    /**
     * apiclient_key.pem证书文件的绝对路径或者以classpath:开头的类路径
     */
    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    /**
     * apiclient_cert.pem证书文件的绝对路径或者以classpath:开头的类路径
     */
    public String getPrivateCertPath() {
        return privateCertPath;
    }

    public void setPrivateCertPath(String privateCertPath) {
        this.privateCertPath = privateCertPath;
    }

    /**
     * 启用微信收付通通知服务
     *
     * @默认值 true
     */
    public Boolean isEnablePayNotifyServlet() {
        return enablePayNotifyServlet;
    }

    public void setEnablePayNotifyServlet(Boolean enablePayNotifyServlet) {
        this.enablePayNotifyServlet = enablePayNotifyServlet;
    }

    /**
     * 接收收付通通知的URL
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    public String getPayNotifyUrl() {
        return payNotifyUrl;
    }

    public void setPayNotifyUrl(String payNotifyUrl) {
        this.payNotifyUrl = payNotifyUrl;
    }

    /**
     * 接收收付通通知的URL
     * <p>V3版本</p>
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    public String getPayNotifyV3Url() {
        return payNotifyV3Url;
    }

    public void setPayNotifyV3Url(String payNotifyV3Url) {
        this.payNotifyV3Url = payNotifyV3Url;
    }

    /**
     * 接收微信支付分的URL
     * <p>V3版本</p>
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    public String getPayScoreNotifyV3Url() {
        return payScoreNotifyV3Url;
    }

    public void setPayScoreNotifyV3Url(String payScoreNotifyV3Url) {
        this.payScoreNotifyV3Url = payScoreNotifyV3Url;
    }

    /**
     * 接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数。
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public void setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
    }

    /**
     * 接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数。
     * <p>V3版本</p>
     * <p>如果启用了微信收付通通知服务，则无需配置此项，且已有的配置会被自动覆盖</p>
     *
     * @see project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet
     */
    public String getRefundNotifyV3Url() {
        return refundNotifyV3Url;
    }

    public void setRefundNotifyV3Url(String refundNotifyV3Url) {
        this.refundNotifyV3Url = refundNotifyV3Url;
    }
}
