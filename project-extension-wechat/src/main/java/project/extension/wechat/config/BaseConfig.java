package project.extension.wechat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.wechat.core.INaiveWeChatServiceProvider;
import project.extension.wechat.core.mp.handler.WeChatOAuthMiddleware;
import project.extension.wechat.core.pay.handler.WeChatPayNotifyMiddleware;
import project.extension.wechat.globalization.Strings;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基础配置
 *
 * @author LCTR
 * @date 2023-03-15
 */
@Primary
@Component
@ConfigurationProperties("project.extension.wechat")
public class BaseConfig {
    /**
     * 当前服务器的根地址
     */
    private String rootUrl;

    /**
     * 默认的公众号名称
     */
    private String mP;

    /**
     * 默认的商户号名称
     */
    private String pay;

    /**
     * 编码格式
     *
     * @默认值 UTF-8
     */
    private String encoding = StandardCharsets.UTF_8.name();

    /**
     * 默认的p12证书地址
     */
    private String certFilePath;

    /**
     * 默认的证书密码
     * <p>一般默认为商户号</p>
     */
    private String certPassword;

    /**
     * 默认的pem公钥文件地址
     */
    private String privateKeyPath;

    /**
     * 默认启用微信验证签名验证中间件
     * <p>此选项设为false时，multiMP中的enableSignatureVerificationMiddleware选项将无效</p>
     *
     * @默认值 true
     */
    private Boolean enableSignatureVerificationMiddleware = true;

    /**
     * 默认启用微信网页授权中间件
     * <p>此选项设为false时，multiMP中的enableOAuthMiddleware选项将无效</p>
     *
     * @默认值 true
     */
    private Boolean enableOAuthMiddleware = true;

    /**
     * 默认启用微信收付通通知中间件
     * <p>此选项设为false时，multiPay中的enablePayNotifyMiddleware选项将无效</p>
     *
     * @默认值 true
     */
    private Boolean enablePayNotifyMiddleware = true;

    /**
     * 多公众号配置
     */
    private Map<String, MPConfig> multiMP;

    /**
     * 多商户号配置
     */
    private Map<String, PayConfig> multiPay;

    /**
     * 当前服务器的根地址
     */
    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    /**
     * 默认的公众号名称
     */
    public String getMP() {
        return mP;
    }

    public void setMP(String mP) {
        this.mP = mP;
    }

    /**
     * 默认的商户号名称
     */
    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    /**
     * 编码格式
     *
     * @默认值 UTF-8
     */
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 默认的p12证书地址
     */
    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    /**
     * 默认的证书密码
     * <p>一般默认为商户号</p>
     */
    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    /**
     * 默认的pem公钥文件地址
     */
    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    /**
     * 默认启用微信令牌验证中间件
     * <p>此选项设为false时，multiMP中的enableSignatureVerificationMiddleware选项将无效</p>
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
     * 默认启用微信网页授权中间件
     * <p>此选项设为false时，multiMP中的enableOAuthMiddleware选项将无效</p>
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
     * 默认启用微信收付通通知中间件
     * <p>此选项设为false时，multiPay中的enablePayNotifyMiddleware选项将无效</p>
     *
     * @默认值 true
     */
    public Boolean isEnablePayNotifyMiddleware() {
        return enablePayNotifyMiddleware;
    }

    public void setEnablePayNotifyMiddleware(Boolean enablePayNotifyMiddleware) {
        this.enablePayNotifyMiddleware = enablePayNotifyMiddleware;
    }

    /**
     * 多公众号配置
     */
    public Map<String, MPConfig> getMultiMP() {
        return multiMP;
    }

    public void setMultiMP(Map<String, MPConfig> multiMP) {
        this.multiMP = multiMP;
    }

    /**
     * 多商户号配置
     */
    public Map<String, PayConfig> getMultiPay() {
        return multiPay;
    }

    public void setMultiPay(Map<String, PayConfig> multiPay) {
        this.multiPay = multiPay;
    }

    /**
     * 是否为多公众号
     */
    public boolean isMultiMP() {
        return getMultiMP() != null && getMultiMP().size() > 1;
    }

    /**
     * 获取所有公众号名称
     */
    public List<String> getAllMP(boolean enabledOnly) {
        List<String> allMP = new ArrayList<>();
        for (String mp : this.getMultiMP()
                             .keySet()) {
            if (!enabledOnly
                    || this.getMultiMP()
                           .get(mp)
                           .isEnable())
                allMP.add(mp);
        }
        return allMP;
    }

    /**
     * 获取默认的公众号配置
     */
    public MPConfig getMPConfig() {
        if (!StringUtils.hasText(this.getMP())) {
            if (this.getMultiMP() == null || this.getMultiMP()
                                                 .size() == 0) {
                //设置默认公众号为master
                this.setMP(INaiveWeChatServiceProvider.DEFAULT_MP);
            } else {
                //设置默认公众号为多个中的第一个
                MPConfig firstMP = CollectionsExtension.firstValue(this.getMultiMP());
                assert firstMP != null;
                this.setMP(firstMP.getName());

                if (!StringUtils.hasText(this.getMP())) {
                    if (this.getMultiMP()
                            .size() > 1)
                        throw new ModuleException(Strings.getConfigMPNameUndefined());

                    //设置默认公众号为master
                    this.setMP(INaiveWeChatServiceProvider.DEFAULT_MP);
                    firstMP.setName(this.getMP());
                }
            }
        }

        return getMPConfig(this.getMP());
    }

    /**
     * 获取指定的公众号配置
     *
     * @param mp 公众号名称
     */
    public MPConfig getMPConfig(String mp)
            throws
            ModuleException {
        if (multiMP == null || multiMP.size() == 0)
            throw new ModuleException(Strings.getConfigMPUndefined(mp));

        AtomicInteger count = new AtomicInteger();
        Optional<String> matchConfig = multiMP.keySet()
                                              .stream()
                                              .filter(x -> {
                                                  if (mp.equals(x)) {
                                                      count.getAndIncrement();
                                                      return true;
                                                  } else
                                                      return false;
                                              })
                                              .findAny();
        if (!matchConfig.isPresent())
            throw new ModuleException(Strings.getConfigMPUndefined(mp));

        if (count.get() > 1)
            throw new ModuleException(Strings.getConfigMPRepeat(mp));

        MPConfig config = multiMP.get(matchConfig.get());

        config.setName(matchConfig.get());

        if (config.isEnable() && !StringUtils.hasText(config.getAppId()))
            throw new ModuleException(Strings.getConfigMPOptionUndefined(mp,
                                                                         "appId"));

        if (config.isEnable() && !StringUtils.hasText(config.getAppSecret()))
            throw new ModuleException(Strings.getConfigMPOptionUndefined(mp,
                                                                         "appSecret"));

        if (config.isEnable() && !StringUtils.hasText(config.getToken()))
            throw new ModuleException(Strings.getConfigMPOptionUndefined(mp,
                                                                         "token"));

        if (config.isEnable() && !StringUtils.hasText(config.getAesKey()))
            throw new ModuleException(Strings.getConfigMPOptionUndefined(mp,
                                                                         "aesKey"));

        if (config.isEnable() && !StringUtils.hasText(config.getTokenVerificationUrl()))
            throw new ModuleException(Strings.getConfigMPOptionUndefined(mp,
                                                                         "tokenVerificationUrl"));

        if (config.isEnable() && (!this.isEnableOAuthMiddleware() || (this.isEnableOAuthMiddleware()
                && !config.isEnableOAuthMiddleware()))) {
            if (!StringUtils.hasText(config.getOAuthBaseUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledMiddleware(WeChatOAuthMiddleware.class.getName(),
                                                                                          "oAuthBaseUrl"));

            if (!StringUtils.hasText(config.getOAuthUserInfoUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledMiddleware(WeChatOAuthMiddleware.class.getName(),
                                                                                          "oAuthUserInfoUrl"));
        }

        return config;
    }

    /**
     * 是否为多商户号
     */
    public boolean isMultiPay() {
        return getMultiPay() != null && getMultiPay().size() > 1;
    }

    /**
     * 获取所有商户号名称
     */
    public List<String> getAllPay(boolean enabledOnly) {
        List<String> allPay = new ArrayList<>();
        for (String mp : this.getMultiPay()
                             .keySet()) {
            if (!enabledOnly
                    || this.getMultiPay()
                           .get(mp)
                           .isEnable())
                allPay.add(mp);
        }
        return allPay;
    }

    /**
     * 获取默认的商户号配置
     */
    public PayConfig getPayConfig() {
        if (!StringUtils.hasText(this.getPay())) {
            if (this.getMultiPay() == null || this.getMultiPay()
                                                  .size() == 0) {
                //设置默认商户号为master
                this.setPay(INaiveWeChatServiceProvider.DEFAULT_PAY);
            } else {
                //设置默认商户号为多个中的第一个
                PayConfig firstPay = CollectionsExtension.firstValue(this.getMultiPay());
                assert firstPay != null;
                this.setPay(firstPay.getName());

                if (!StringUtils.hasText(this.getPay())) {
                    if (this.getMultiPay()
                            .size() > 1)
                        throw new ModuleException(Strings.getConfigPayNameUndefined());

                    //设置默认商户号为master
                    this.setPay(INaiveWeChatServiceProvider.DEFAULT_PAY);
                    firstPay.setName(this.getPay());
                }
            }
        }

        return getPayConfig(this.getPay());
    }

    /**
     * 获取指定的商户号配置
     *
     * @param pay 商户号名称
     */
    public PayConfig getPayConfig(String pay)
            throws
            ModuleException {
        if (multiPay == null || multiPay.size() == 0)
            throw new ModuleException(Strings.getConfigPayUndefined(pay));

        AtomicInteger count = new AtomicInteger();
        Optional<String> matchConfig = multiPay.keySet()
                                               .stream()
                                               .filter(x -> {
                                                   if (pay.equals(x)) {
                                                       count.getAndIncrement();
                                                       return true;
                                                   } else
                                                       return false;
                                               })
                                               .findAny();
        if (!matchConfig.isPresent())
            throw new ModuleException(Strings.getConfigPayUndefined(pay));

        if (count.get() > 1)
            throw new ModuleException(Strings.getConfigPayRepeat(pay));

        PayConfig config = multiPay.get(matchConfig.get());

        config.setName(matchConfig.get());

        if (config.isEnable() && !StringUtils.hasText(config.getMchId()))
            throw new ModuleException(Strings.getConfigPayOptionUndefined(pay,
                                                                          "mchId"));

        if (config.isEnable() && !StringUtils.hasText(config.getMchKey()))
            throw new ModuleException(Strings.getConfigPayOptionUndefined(pay,
                                                                          "key"));

        if (config.isEnable() && !StringUtils.hasText(config.getCertFilePath()))
            throw new ModuleException(Strings.getConfigMPOptionUndefined(pay,
                                                                         "certFilePath"));

        if (config.isEnable() && !StringUtils.hasText(config.getCertPassword()))
            throw new ModuleException(Strings.getConfigMPOptionUndefined(pay,
                                                                         "certPassword"));

        if (config.isEnable() && (!this.isEnablePayNotifyMiddleware() || (this.isEnablePayNotifyMiddleware()
                && !config.isEnablePayNotifyMiddleware()))) {
            if (!StringUtils.hasText(config.getPayNotifyUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledMiddleware(WeChatPayNotifyMiddleware.class.getName(),
                                                                                          "payNotifyUrl"));

            if (!StringUtils.hasText(config.getPayNotifyV3Url()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledMiddleware(WeChatPayNotifyMiddleware.class.getName(),
                                                                                          "payNotifyV3Url"));

            if (config.getPayNotifyUrl()
                      .equals(config.getPayNotifyV3Url()))
                throw new ModuleException(Strings.getConfigIdentical("payNotifyUrl",
                                                                     "payNotifyV3Url"));

            if (!StringUtils.hasText(config.getPayScoreNotifyV3Url()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledMiddleware(WeChatPayNotifyMiddleware.class.getName(),
                                                                                          "payScoreNotifyV3Url"));

            if (!StringUtils.hasText(config.getRefundNotifyUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledMiddleware(WeChatPayNotifyMiddleware.class.getName(),
                                                                                          "refundNotifyUrl"));

            if (!StringUtils.hasText(config.getRefundNotifyV3Url()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledMiddleware(WeChatPayNotifyMiddleware.class.getName(),
                                                                                          "refundNotifyV3Url"));

            if (config.getRefundNotifyUrl()
                      .equals(config.getRefundNotifyV3Url()))
                throw new ModuleException(Strings.getConfigIdentical("refundNotifyUrl",
                                                                     "refundNotifyV3Url"));
        }

        return config;
    }
}
