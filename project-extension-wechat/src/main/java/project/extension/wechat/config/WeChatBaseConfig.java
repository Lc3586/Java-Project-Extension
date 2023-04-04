package project.extension.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.string.StringExtension;
import project.extension.tuple.Tuple2;
import project.extension.wechat.core.INaiveWeChatServiceProvider;
import project.extension.wechat.core.mp.servlet.WeChatMpEndpointServlet;
import project.extension.wechat.core.mp.servlet.WeChatOAuth2Servlet;
import project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet;
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
@Data
public class WeChatBaseConfig {
    /**
     * 当前服务器的根地址
     */
    private String rootUrl;

    /**
     * 当前服务器的根地址架构
     */
    private String rootUrlScheme;

    /**
     * 当前服务器的根地址主机
     */
    private String rootUrlHost;

    /**
     * 默认的公众号名称
     */
    private String mp;

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
     * 默认启用微信终端服务
     * <p>此选项设为false时，multiMP中的enableMpEndpointServlet选项将无效</p>
     *
     * @默认值 true
     */
    private Boolean enableMpEndpointServlet = true;

    /**
     * 默认启用微信网页授权服务
     * <p>此选项设为false时，multiMP中的enableOAuth2Servlet选项将无效</p>
     *
     * @默认值 true
     */
    private Boolean enableOAuth2Servlet = true;

    /**
     * 默认启用微信收付通通知服务
     * <p>此选项设为false时，multiPay中的enablePayNotifyServlet选项将无效</p>
     *
     * @默认值 true
     */
    private Boolean enablePayNotifyServlet = true;

    /**
     * 多公众号配置
     */
    private Map<String, MpConfig> multiMp;

    /**
     * 多商户号配置
     */
    private Map<String, PayConfig> multiPay;

    public void setRootUrl(String rootUrl) {
        Tuple2<String, String> result = StringExtension.getSchemeAndHost(rootUrl);
        if (result != null) {
            this.rootUrlScheme = result.a;
            this.rootUrlHost = result.b;
        } else
            throw new ModuleException(Strings.getDataFormatNonStandard("project.extension.wechat.rootUrl",
                                                                       rootUrl,
                                                                       "http://www.a.com, https://www.a.com"));

        this.rootUrl = rootUrl;
    }

    /**
     * 是否为多公众号
     */
    public boolean isMultiMp() {
        return getMultiMp() != null && getMultiMp().size() > 1;
    }

    /**
     * 获取所有公众号名称
     */
    public List<String> getAllMp(boolean enabledOnly) {
        List<String> allMP = new ArrayList<>();
        if (this.getMultiMp() != null)
            for (String mp : this.getMultiMp()
                                 .keySet()) {
                if (!enabledOnly
                        || this.getMultiMp()
                               .get(mp)
                               .isEnable())
                    allMP.add(mp);
            }
        return allMP;
    }

    /**
     * 获取默认的公众号配置
     */
    public MpConfig getMpConfig() {
        if (!StringUtils.hasText(this.getMp())) {
            if (this.getMultiMp() == null || this.getMultiMp()
                                                 .size() == 0) {
                //设置默认公众号为master
                this.setMp(INaiveWeChatServiceProvider.DEFAULT_MP);
            } else {
                //设置默认公众号为多个中的第一个
                MpConfig firstMP = CollectionsExtension.firstValue(this.getMultiMp());
                assert firstMP != null;
                this.setMp(firstMP.getName());

                if (!StringUtils.hasText(this.getMp())) {
                    if (this.getMultiMp()
                            .size() > 1)
                        throw new ModuleException(Strings.getConfigMpNameUndefined());

                    //设置默认公众号为master
                    this.setMp(INaiveWeChatServiceProvider.DEFAULT_MP);
                    firstMP.setName(this.getMp());
                }
            }
        }

        return getMpConfig(this.getMp());
    }

    /**
     * 获取指定的公众号配置
     *
     * @param mp 公众号名称
     */
    public MpConfig getMpConfig(String mp)
            throws
            ModuleException {
        if (multiMp == null || multiMp.size() == 0)
            throw new ModuleException(Strings.getConfigMpUndefined(mp));

        AtomicInteger count = new AtomicInteger();
        Optional<String> matchConfig = multiMp.keySet()
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
            throw new ModuleException(Strings.getConfigMpUndefined(mp));

        if (count.get() > 1)
            throw new ModuleException(Strings.getConfigMpRepeat(mp));

        MpConfig config = multiMp.get(matchConfig.get());

        config.setName(matchConfig.get());

        if (config.isEnable() && !StringUtils.hasText(config.getAppId()))
            throw new ModuleException(Strings.getConfigMpOptionUndefined(mp,
                                                                         "appId"));

        if (config.isEnable() && !StringUtils.hasText(config.getAppSecret()))
            throw new ModuleException(Strings.getConfigMpOptionUndefined(mp,
                                                                         "appSecret"));

        if (config.isEnable() && !StringUtils.hasText(config.getToken()))
            throw new ModuleException(Strings.getConfigMpOptionUndefined(mp,
                                                                         "token"));

        if (config.isEnable() && !StringUtils.hasText(config.getAesKey()))
            throw new ModuleException(Strings.getConfigMpOptionUndefined(mp,
                                                                         "aesKey"));

        if (config.isEnable() && this.getEnableMpEndpointServlet() && config.getEnableMpEndpointServlet()) {
            if (!StringUtils.hasText(config.getMpEndpointUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledServlet(WeChatMpEndpointServlet.class.getName(),
                                                                                       "mpEndpointUrl"));
        }

        if (config.isEnable() && (!this.getEnableOAuth2Servlet() || (this.getEnableOAuth2Servlet()
                && !config.getEnableOAuth2Servlet()))) {
            if (!StringUtils.hasText(config.getOAuthBaseUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledServlet(WeChatOAuth2Servlet.class.getName(),
                                                                                       "oAuthBaseUrl"));

            if (!StringUtils.hasText(config.getOAuthUserInfoUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledServlet(WeChatOAuth2Servlet.class.getName(),
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
        if (this.getMultiPay() != null)
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
            throw new ModuleException(Strings.getConfigMpOptionUndefined(pay,
                                                                         "certFilePath"));

        if (config.isEnable() && !StringUtils.hasText(config.getCertPassword()))
            throw new ModuleException(Strings.getConfigMpOptionUndefined(pay,
                                                                         "certPassword"));

        if (config.isEnable() && (!this.getEnablePayNotifyServlet() || (this.getEnablePayNotifyServlet()
                && !config.getEnablePayNotifyServlet()))) {
            if (!StringUtils.hasText(config.getPayNotifyUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledServlet(WeChatPayNotifyServlet.class.getName(),
                                                                                       "payNotifyUrl"));

            if (!StringUtils.hasText(config.getPayNotifyV3Url()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledServlet(WeChatPayNotifyServlet.class.getName(),
                                                                                       "payNotifyV3Url"));

            if (config.getPayNotifyUrl()
                      .equals(config.getPayNotifyV3Url()))
                throw new ModuleException(Strings.getConfigIdentical("payNotifyUrl",
                                                                     "payNotifyV3Url"));

            if (!StringUtils.hasText(config.getPayScoreNotifyV3Url()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledServlet(WeChatPayNotifyServlet.class.getName(),
                                                                                       "payScoreNotifyV3Url"));

            if (!StringUtils.hasText(config.getRefundNotifyUrl()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledServlet(WeChatPayNotifyServlet.class.getName(),
                                                                                       "refundNotifyUrl"));

            if (!StringUtils.hasText(config.getRefundNotifyV3Url()))
                throw new ModuleException(Strings.getConfigRequiredWhenDisabledServlet(WeChatPayNotifyServlet.class.getName(),
                                                                                       "refundNotifyV3Url"));

            if (config.getRefundNotifyUrl()
                      .equals(config.getRefundNotifyV3Url()))
                throw new ModuleException(Strings.getConfigIdentical("refundNotifyUrl",
                                                                     "refundNotifyV3Url"));
        }

        return config;
    }
}
