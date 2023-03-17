package project.extension.wechat.core;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import project.extension.ioc.IOCExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.wechat.config.BaseConfig;
import project.extension.wechat.config.MPConfig;
import project.extension.wechat.config.PayConfig;
import project.extension.wechat.core.mp.handler.WeChatOAuthMiddleware;
import project.extension.wechat.core.mp.handler.WeChatSignatureVerificationMiddleware;
import project.extension.wechat.core.mp.standard.IWeChatMPService;
import project.extension.wechat.core.mp.normal.WeChatMPService;
import project.extension.wechat.core.pay.handler.WeChatPayNotifyMiddleware;
import project.extension.wechat.core.pay.standard.IWeChatPayService;
import project.extension.wechat.core.pay.normal.WeChatPayService;
import project.extension.wechat.globalization.Strings;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信服务构造器实现类
 *
 * @author LCTR
 * @date 2023-03-15
 */
@SuppressWarnings("SpringDependsOnUnresolvedBeanInspection")
@Configuration
@EnableConfigurationProperties({BaseConfig.class})
@DependsOn("iocExtension")
public class NaiveWeChatServiceProvider
        implements INaiveWeChatServiceProvider {
    public NaiveWeChatServiceProvider(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    private final BaseConfig baseConfig;

    private final Map<String, IWeChatMPService> mpServiceMap = new HashMap<>();

    private final Map<String, IWeChatPayService> payServiceMap = new HashMap<>();

    private DefaultListableBeanFactory beanFactory;

    /**
     * 容器工厂
     */
    private DefaultListableBeanFactory getBeanFactory() {
        if (beanFactory == null)
            beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) IOCExtension.applicationContext).getBeanFactory();
        return beanFactory;
    }

    /**
     * 加载并注册微信公众号服务
     *
     * @param mp 公众号名称
     */
    private void loadAndRegisterMPService(String mp) {
        MPConfig mpConfig = baseConfig.getMPConfig(mp);
        if (!mpConfig.isEnable())
            return;

        IWeChatMPService weChatMPService;
        try {
            weChatMPService = new WeChatMPService(mpConfig);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getCreateWeChatMPServiceFailed(mpConfig.getName()),
                                      ex);
        }

        mpServiceMap.put(mpConfig.getName(),
                         weChatMPService);

        //是否为默认的公众号
        boolean _default = mpConfig.getName()
                                   .equals(defaultMP());

        //设置中间件
        if (baseConfig.isEnableSignatureVerificationMiddleware() && mpConfig.isEnableSignatureVerificationMiddleware())
            WeChatSignatureVerificationMiddleware.setup(mpConfig,
                                                        weChatMPService);
        if (baseConfig.isEnableOAuthMiddleware() && mpConfig.isEnableOAuthMiddleware())
            WeChatOAuthMiddleware.setup(mpConfig,
                                        weChatMPService);

        //注册
        if (_default)
            getBeanFactory().registerSingleton(
                    getMPServiceBeanName(null),
                    weChatMPService);
        getBeanFactory().registerSingleton(
                getMPServiceBeanName(mpConfig.getName()),
                weChatMPService);
    }

    /**
     * 加载并注册微信支付服务
     *
     * @param pay 商户号名称
     */
    private void loadAndRegisterPayService(String pay) {
        PayConfig payConfig = baseConfig.getPayConfig(pay);
        if (!payConfig.isEnable())
            return;

        IWeChatPayService weChatPayService;
        try {
            weChatPayService = new WeChatPayService(baseConfig,
                                                    payConfig);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getCreateWeChatMPServiceFailed(payConfig.getName()),
                                      ex);
        }

        payServiceMap.put(payConfig.getName(),
                          weChatPayService);

        //是否为默认的商户号
        boolean _default = payConfig.getName()
                                    .equals(defaultMP());

        //设置中间件
        if (baseConfig.isEnablePayNotifyMiddleware() && payConfig.isEnablePayNotifyMiddleware())
            WeChatPayNotifyMiddleware.setup(payConfig,
                                            weChatPayService);

        //注册
        if (_default)
            getBeanFactory().registerSingleton(
                    getPayServiceBeanName(null),
                    weChatPayService);

        getBeanFactory().registerSingleton(
                getPayServiceBeanName(payConfig.getName()),
                weChatPayService);
    }

    /**
     * 获取微信公众号服务在Spring IOC容器中的名称
     *
     * @param mp 公众号名称
     */
    public static String getMPServiceBeanName(
            @Nullable
                    String mp) {
        return mp == null
               ? INaiveWeChatServiceProvider.DEFAULT_MP_SERVICE_IOC_NAME
               : String.format("%s%s",
                               INaiveWeChatServiceProvider.MP_SERVICE_IOC_PREFIX,
                               mp);
    }

    /**
     * 获取微信支付服务在Spring IOC容器中的名称
     *
     * @param mp 公众号名称
     */
    public static String getPayServiceBeanName(
            @Nullable
                    String mp) {
        return mp == null
               ? INaiveWeChatServiceProvider.DEFAULT_PAY_SERVICE_IOC_NAME
               : String.format("%s%s",
                               INaiveWeChatServiceProvider.PAY_SERVICE_IOC_PREFIX,
                               mp);
    }

    /**
     * 获取中间件在Spring IOC容器中的名称
     *
     * @param middlewareType 中间件类型
     */
    public static String getMiddlewareBeanName(
            Class<?> middlewareType) {
        return String.format("%s%s",
                             INaiveWeChatServiceProvider.MIDDLEWARE_IOC_PREFIX,
                             middlewareType.getSimpleName());
    }

    @Override
    public String defaultMP() {
        return this.baseConfig.getMP();
    }

    @Override
    public boolean isMPExists(String mp) {
        return this.baseConfig.getMultiMP()
                              .containsKey(mp);
    }

    @Override
    public boolean isMPEnable(String mp) {
        return this.baseConfig.getMultiMP()
                              .get(mp)
                              .isEnable();
    }

    @Override
    public List<String> allMP(boolean enabledOnly) {
        return this.baseConfig.getAllMP(enabledOnly);
    }

    @Override
    public Map<String, IWeChatMPService> loadAllWeChatMPService() {
        if (mpServiceMap.size() == 0) {
            //注册中间件
            if (baseConfig.isEnableSignatureVerificationMiddleware()) {
                WeChatSignatureVerificationMiddleware signatureVerificationMiddleware = new WeChatSignatureVerificationMiddleware();
                getBeanFactory().registerSingleton(getMiddlewareBeanName(WeChatSignatureVerificationMiddleware.class),
                                                   signatureVerificationMiddleware);
            }
            if (baseConfig.isEnablePayNotifyMiddleware()) {
                WeChatOAuthMiddleware oAuthMiddleware = new WeChatOAuthMiddleware(baseConfig);
                getBeanFactory().registerSingleton(getMiddlewareBeanName(WeChatOAuthMiddleware.class),
                                                   oAuthMiddleware);
            }
            for (String mp : allMP(true)) {
                loadAndRegisterMPService(mp);
            }
        }

        return mpServiceMap;
    }

    @Override
    public IWeChatMPService getDefaultWeChatMPService() {
        return getWeChatMPService(defaultMP());
    }

    @Override
    public IWeChatMPService getWeChatMPService(String mp) {
        if (!isMPExists(mp))
            throw new ModuleException(Strings.getConfigMPUndefined(mp));

        if (!isMPEnable(mp))
            throw new ModuleException(Strings.getConfigMPNotActive(mp));

        return loadAllWeChatMPService().get(mp);
    }


    @Override
    public String defaultPay() {
        return this.baseConfig.getPay();
    }

    @Override
    public boolean isPayExists(String pay) {
        return this.baseConfig.getMultiPay()
                              .containsKey(pay);
    }

    @Override
    public boolean isPayEnable(String pay) {
        return this.baseConfig.getMultiPay()
                              .get(pay)
                              .isEnable();
    }

    @Override
    public List<String> allPay(boolean enabledOnly) {
        return this.baseConfig.getAllPay(enabledOnly);
    }

    @Override
    public Map<String, IWeChatPayService> loadAllWeChatPayService() {
        if (payServiceMap.size() == 0) {
            //注册中间件
            if (baseConfig.isEnablePayNotifyMiddleware()) {
                WeChatPayNotifyMiddleware payNotifyMiddleware = new WeChatPayNotifyMiddleware(baseConfig);
                getBeanFactory().registerSingleton(getMiddlewareBeanName(WeChatPayNotifyMiddleware.class),
                                                   payNotifyMiddleware);
            }

            for (String pay : allPay(true)) {
                loadAndRegisterPayService(pay);
            }
        }

        return payServiceMap;
    }

    @Override
    public IWeChatPayService getDefaultWeChatPayService() {
        return getWeChatPayService(defaultPay());
    }

    @Override
    public IWeChatPayService getWeChatPayService(String pay) {
        if (!isPayExists(pay))
            throw new ModuleException(Strings.getConfigPayUndefined(pay));

        if (!isPayEnable(pay))
            throw new ModuleException(Strings.getConfigPayNotActive(pay));

        return loadAllWeChatPayService().get(pay);
    }
}
